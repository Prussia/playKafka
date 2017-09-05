package com.prussia.play.kafka.test;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.IntegerDeserializer;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.listener.config.ContainerProperties;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TestHelloWorld {

	@Test
	public void testAutoCommit() throws Exception {
		log.info("Start auto");
		String topic1 = "helloworld"; 
		ContainerProperties containerProps = new ContainerProperties(topic1);
		final CountDownLatch latch = new CountDownLatch(4);
		containerProps.setMessageListener(new MessageListener<Integer, String>() {

			@Override
			public void onMessage(ConsumerRecord<Integer, String> message) {
				log.info(latch.getCount() + ". app received: " + message);
				latch.countDown();
			}

		});
		KafkaMessageListenerContainer<Integer, String> container = createContainer(containerProps);
		container.setBeanName("testAuto");
		container.start();
		Thread.sleep(1000); // wait a bit for the container to start
		KafkaTemplate<Integer, String> template = createTemplate();
		
		template.setDefaultTopic(topic1);
		
		template.sendDefault(0, "foo0");
		template.sendDefault(1, "bar1");
		template.sendDefault(2, "baz2");
		template.sendDefault(3, "qux3");
		
		template.flush();
		Assert.assertTrue(latch.await(60, TimeUnit.SECONDS));
		container.stop();
		log.info("Stop auto");

	}

	private KafkaMessageListenerContainer<Integer, String> createContainer(ContainerProperties containerProps) {
		Map<String, Object> props = consumerProps();
		DefaultKafkaConsumerFactory<Integer, String> cf = new DefaultKafkaConsumerFactory<Integer, String>(props);
		KafkaMessageListenerContainer<Integer, String> container = new KafkaMessageListenerContainer<>(cf,
				containerProps);
		return container;
	}

	private KafkaTemplate<Integer, String> createTemplate() {
		Map<String, Object> senderProps = senderProps();
		ProducerFactory<Integer, String> pf = new DefaultKafkaProducerFactory<Integer, String>(senderProps);
		KafkaTemplate<Integer, String> template = new KafkaTemplate<>(pf);
		return template;
	}

	private Map<String, Object> consumerProps() {
		Map<String, Object> props = new HashMap<>();
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "insight-platform-cdh-slave1.eastus.cloudapp.azure.com:9092");
		String group = "prussia"; // should be a static value
		props.put(ConsumerConfig.GROUP_ID_CONFIG, group);
		props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
		props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "100");
		props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "15000");
		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, IntegerDeserializer.class);
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		return props;
	}

	private Map<String, Object> senderProps() {
		Map<String, Object> props = new HashMap<>();
		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "insight-platform-cdh-slave1.eastus.cloudapp.azure.com:9092");
		props.put(ProducerConfig.RETRIES_CONFIG, 0);
		props.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384);
		props.put(ProducerConfig.LINGER_MS_CONFIG, 1);
		props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432);
		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, IntegerSerializer.class);
		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		return props;
	}
}
