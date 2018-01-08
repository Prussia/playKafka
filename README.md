# play[Kafka](http://kafka.apache.org/)

## [Kafka入门教程](http://blog.csdn.net/hmsiwtv/article/details/46960053)
## [W3C school Kafka](https://www.w3cschool.cn/apache_kafka/apache_kafka_quick_guide.html)
## [Kafka 中文教程](http://orchome.com/kafka/index)
## A few concepts
- Kafka is run as a cluster on one or more servers.
- The Kafka cluster stores streams of records in categories called topics.
- Each record consists of a key, a value, and a timestamp.

## Four core APIs
- The [Producer API](http://kafka.apache.org/documentation.html#producerapi) allows an application to publish a stream of records to one or more Kafka topics.
- The [Consumer API](http://kafka.apache.org/documentation.html#consumerapi) allows an application to subscribe to one or more topics and process the stream of records produced to them.
- The [Streams API](http://kafka.apache.org/documentation/streams) allows an application to act as a stream processor, consuming an input stream from one or more topics and producing an output stream to one or more output topics, effectively transforming the input streams to output streams.
- The [Connector API](http://kafka.apache.org/documentation.html#connect) allows building and running reusable producers or consumers that connect Kafka topics to existing applications or data systems. For example, a connector to a relational database might capture every change to a table.


```
./kafka-run-class kafka.tools.GetOffsetShell --broker-list localhost:9092 --topic helloworld --time -1
| while IFS=: read topic_name partition_id number; do echo "$number"; done
```
```
./kafka-console-producer --broker-list localhost:9092  --topic helloworld
```
```
./kafka-console-consumer --bootstrap-server localhost:9092 --from-beginning --topic helloworld

```

```
./kafka-console-producer --broker-list localhost:9092 --topic $topic --property "parse.key=true" --property "key.separator=:"

key1:value1

```
