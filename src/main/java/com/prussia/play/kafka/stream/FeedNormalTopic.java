package com.prussia.play.kafka.stream;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.streams.StreamsConfig;
import org.json.simple.JSONObject;

public class FeedNormalTopic {

	public static void main(String[] args) {

		Properties props = new Properties();
		props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
		props.put("client.id", "MockStatsProducer");
		props.put("key.serializer", "org.apache.kafka.common.serialization.IntegerSerializer");
		props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

		KafkaProducer<Integer, String> producer = new KafkaProducer<>(props);
		try {
			JSONObject metricObj = new JSONObject();
			metricObj.put("name", "Arron18abcd");
			metricObj.put("execId", new Integer(18));
			producer.send(new ProducerRecord<Integer, String>("test", new Integer(102), metricObj.toString()));
		} finally {
			producer.flush();
			producer.close();
		}

	}
}
