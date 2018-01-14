# Kafka Command

- produce messages with key:value
```
kafka-console-producer.sh --broker-list localhost:9092 --topic error_topic --property "parse.key=true" --property "key.separator=:"
```

- set Kafka config
```
bash kafka-configs.sh --zookeeper localhost:2181 --alter --entity-type topics --entity-name processed_topic_0 --add-config retention.ms=1000

bash kafka-configs.sh --zookeeper localhost:2181 --alter --entity-type topics --entity-name processed_topic_0 --delete-config retention.ms
```

```

kafka-console-producer.sh --broker-list localhost:9092 --topic test

kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic test --from-beginning

kafka-topics --list --zookeeper localhost:2181

kafka-topics --describe --zookeeper localhost:2181 --topic my-replicated-topic

kafka-topics --describe --zookeeper localhost:2181 --topic test

kafka-topics --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic test

kafka-topics --create --zookeeper localhost:2181 --replication-factor 3 --partitions 1 --topic my-replicated-topic

kafka-topics --list --zookeeper localhost:2181

kafka-topics --zookeeper localhost:2181 --alter --partitions 64 --topic my_topic_name  

```
