package com.prussia.play.kafka.stream;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.streams.StreamsConfig;
import org.json.simple.JSONObject;

public class FeedStatusTopic {

	public static void main(String[] args) {

		Properties props = new Properties();
		props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
		props.put("client.id", "MockStatsProducer");
		props.put("key.serializer", "org.apache.kafka.common.serialization.IntegerSerializer");
		props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

		KafkaProducer<Integer, String> producer2 = new KafkaProducer<>(props);
		try {
			JSONObject metricObj = new JSONObject();
			metricObj.put("status", "kill");
			//metricObj.put("execId", new Integer(2));
			producer2.send(new ProducerRecord<Integer, String>("teststatus", new Integer(17), metricObj.toString()));
		} finally {
			producer2.flush();
			producer2.close();
		}

	}

}
