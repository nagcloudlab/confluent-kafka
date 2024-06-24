

to list all consumer groups in a Kafka cluster, run the following command:

```bash
confluent-7.6.1/bin/kafka-consumer-groups --bootstrap-server localhost:9092 --list
```

To describe a specific consumer group, run the following command:

```bash
confluent-7.6.1/bin/kafka-consumer-groups --bootstrap-server localhost:9092 --describe --group <group_name>
```

To reset the offset of a consumer group to the earliest offset, run the following command:

```bash
confluent-7.6.1/bin/kafka-consumer-groups --bootstrap-server localhost:9092 --group <group_name> --reset-offsets --to-earliest --execute --topic <topic_name>
```

To reset the offset of a consumer group to the latest offset, run the following command:

```bash
confluent-7.6.1/bin/kafka-consumer-groups --bootstrap-server localhost:9092 --group <group_name> --reset-offsets --to-latest --execute --topic <topic_name>
```

To reset the offset of a consumer group to a specific offset, run the following command:

```bash
confluent-7.6.1/bin/kafka-consumer-groups --bootstrap-server localhost:9092 --group <group_name> --reset-offsets --to-offset <offset> --execute --topic <topic_name>
```

To reset the offset of a consumer group to a specific timestamp, run the following command:

```bash
confluent-7.6.1/bin/kafka-consumer-groups --bootstrap-server localhost:9092 --group <group_name> --reset-offsets --to-offset <timestamp> --execute --topic <topic_name>
```

To delete a consumer group, run the following command:

```bash
confluent-7.6.1/bin/kafka-consumer-groups --bootstrap-server localhost:9092 --group <group_name> --delete
```
