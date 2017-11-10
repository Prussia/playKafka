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
