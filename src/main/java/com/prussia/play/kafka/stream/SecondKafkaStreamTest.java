package com.prussia.play.kafka.stream;

import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.Serializer;
import org.apache.kafka.connect.json.JsonDeserializer;
import org.apache.kafka.connect.json.JsonSerializer;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KStreamBuilder;

import com.fasterxml.jackson.databind.JsonNode;

public class SecondKafkaStreamTest {

	public static void main(String[] args) {
		Properties props = new Properties();
		props.put(StreamsConfig.APPLICATION_ID_CONFIG, "streams-test");
		props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
		props.put(StreamsConfig.CACHE_MAX_BYTES_BUFFERING_CONFIG, 0);
		// props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG,
		// Serdes.String().getClass().getName());
		// props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG,
		// Serdes..String().getClass().getName());

		props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

		KStreamBuilder builder = new KStreamBuilder();

		// KStream<String, String> source = builder.stream("test");

		// load a simple json serializer
		final Serializer<JsonNode> jsonSerializer = new JsonSerializer();
		// load a simple json deserializer
		final Deserializer<JsonNode> jsonDeserializer = new JsonDeserializer();
		// use the simple json serializer and deserialzer we just made and load
		// a Serde for streaming data
		final Serde<JsonNode> jsonSerde = Serdes.serdeFrom(jsonSerializer, jsonDeserializer);

		final Serde<Integer> integerSerde = Serdes.Integer();

		KStream<Integer, JsonNode> source = builder.stream(integerSerde, jsonSerde, "test");

		source.print();

		final KafkaStreams streams = new KafkaStreams(builder, props);
		streams.start();

	}

}
