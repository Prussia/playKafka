package com.prussia.play.kafka.stream;

import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.Serializer;
import org.apache.kafka.connect.json.JsonDeserializer;
import org.apache.kafka.connect.json.JsonSerializer;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.JoinWindows;
import org.apache.kafka.streams.kstream.KGroupedStream;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KStreamBuilder;
import org.apache.kafka.streams.kstream.KTable;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class JoinKafkaStreamTest {

	public static void main(String[] args) {
		Properties props = new Properties();
		props.put(StreamsConfig.APPLICATION_ID_CONFIG, "streams-test");
		props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
		props.put(StreamsConfig.CACHE_MAX_BYTES_BUFFERING_CONFIG, 0);
		props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
		props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.Integer().getClass().getName());

		KStreamBuilder builder = new KStreamBuilder();

		// load a simple json serializer
		final Serializer<JsonNode> jsonSerializer = new JsonSerializer();
		// load a simple json deserializer
		final Deserializer<JsonNode> jsonDeserializer = new JsonDeserializer();
		// use the simple json serializer and deserialzer we just made and load
		// a Serde for streaming data
		final Serde<JsonNode> jsonSerde = Serdes.serdeFrom(jsonSerializer, jsonDeserializer);

		final Serde<Integer> integerSerde = Serdes.Integer();

		//main topic and data structure is {key: appProfileIdValue, value: {name: "value", execId: value}}
		KStream<Integer, JsonNode> leftSource = builder.stream(integerSerde, jsonSerde, "test");
		//status topic and data structure is {key: execIdValue, value: {status: ""}}
		KTable<Integer, JsonNode> rightSource = builder.table(integerSerde, jsonSerde, "teststatus");

		//transform the main topic to set execId as the key, then left join the status topic
		KStream<Integer, JsonNode> joined = leftSource.map((k, v) -> KeyValue.pair(v.get("jobId").asInt(), v))
				.leftJoin(rightSource, (lv, rv) -> {
					//System.out.println(lv.get("name") + " & " + rv !=null?rv.get("status"):" ");
					final ObjectMapper mapper = new ObjectMapper();
					final ObjectNode root = mapper.createObjectNode();
					root.set("name", mapper.convertValue(lv.get("name"), JsonNode.class));
					root.set("status", mapper.convertValue(rv != null ? rv.get("status") : null, JsonNode.class));
					return root;
				}, integerSerde, jsonSerde);

		// filter streaming element that been killed
		KStream<Integer, JsonNode> joinedFilterKilled = joined
				.filter((k, v) -> !v.get("status").asText().equalsIgnoreCase("kill"));

		//print the result or can be sinked to other destination
		joinedFilterKilled.print();
		//group by default key which is execId, then count the each group
		joined.groupByKey().count().toStream().print();

		final KafkaStreams streams = new KafkaStreams(builder, props);
		streams.start();
	}

}
