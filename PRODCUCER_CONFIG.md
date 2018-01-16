# Kafka Producer Configuration Notes

## [Official Doc](http://kafka.apache.org/0110/documentation/#producerconfigs)

## [Configuring Kafka for Performance and Resource Management](https://www.cloudera.com/documentation/kafka/latest/topics/kafka_performance.html)

### [Tuning Kafka Producers](https://www.cloudera.com/documentation/kafka/latest/topics/kafka_performance.html#kafka_performance_tuning_producers)
    ProducerConfig.BATCH_SIZE_CONFIG
    ProducerConfig.LINGER_MS_CONFIG
    ProducerConfig.COMPRESSION_TYPE_CONFIG
#### Tuning Kafka Consumers
    set more threads for stream thread. The default is 1. Please add more partitions before increasing the thread number. It is mapping with the number of topic partition.
    StreamsConfig.NUM_STREAM_THREADS_CONFIG

#### Tuning Kafka Brokers
    Set enough java heap size for a single broker 
    add more partitions for the specific topics. for example, 
    kafka-topics --zookeeper {zookeeper}:2181 --alter --partitions 32 --topic {topic} 
 
    Garbage Collection  - G1
    
