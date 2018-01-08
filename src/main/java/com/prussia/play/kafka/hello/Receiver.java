package com.prussia.play.kafka.hello;

import java.util.concurrent.CountDownLatch;

import org.springframework.kafka.annotation.KafkaListener;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Receiver {


	  private CountDownLatch latch = new CountDownLatch(1);

	  public CountDownLatch getLatch() {
	    return latch;
	  }

	  @KafkaListener(topics = "${kafka.topic.helloworld}")
	  public void receive(String payload) {
	    log.info("received payload='{}'", payload);
	    latch.countDown();
	  }
	}
