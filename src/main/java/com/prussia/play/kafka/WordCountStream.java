package com.prussia.play.kafka;

import java.util.Arrays;
import java.util.Properties;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStreamBuilder;
import org.springframework.stereotype.Component;

@Component("wordCountStream")
public class WordCountStream {

	private String topic = "lines";
	private KafkaStreams streams;
	
	private String bootstrapServers = "localhost:9092";
	private String zookeeper = "localhost:2181";

	@PostConstruct
	public void runStream() {
		Serde<String> stringSerde = Serdes.String();

		Properties config = new Properties();
		config.put(StreamsConfig.APPLICATION_ID_CONFIG, "kafka-streams-example");
		config.put(StreamsConfig.KEY_SERDE_CLASS_CONFIG, stringSerde.getClass().getName());
		config.put(StreamsConfig.VALUE_SERDE_CLASS_CONFIG, stringSerde.getClass().getName());
		config.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
		config.put(StreamsConfig.ZOOKEEPER_CONNECT_CONFIG, zookeeper);

		config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

		Pattern pattern = Pattern.compile("\\W+", Pattern.UNICODE_CHARACTER_CLASS);
		KStreamBuilder wordLines = new KStreamBuilder();
		wordLines.stream(stringSerde, stringSerde, topic)
				.flatMapValues(value -> Arrays.asList(pattern.split(value.toLowerCase())))
				.filter((key, value) -> value.trim().length() > 0).map((key, value) -> new KeyValue<>(value, value))
				.groupByKey().count("counts").toStream().to(stringSerde, Serdes.Long(), "words-with-counts");

		streams = new KafkaStreams(wordLines, config);
		streams.start();
	}

	@PreDestroy
	public void closeStream() {
		streams.close();
	}
}
