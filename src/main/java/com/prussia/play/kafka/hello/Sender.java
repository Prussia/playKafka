package com.prussia.play.kafka.hello;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Sender {

	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;

	public void send(String topic, String payload) {
		log.info("sending payload='{}' to topic='{}'", payload, topic);
		kafkaTemplate.send(topic, payload);
	}
}
