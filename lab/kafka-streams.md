


create console producer to send number messages to numbers topic
```bash
confluent-7.6.1/bin/kafka-console-producer --broker-list localhost:9092 --topic numbers
```

create console consumer to read messages from odd-numbers topic
```bash
confluent-7.6.1/bin/kafka-console-consumer --bootstrap-server localhost:9092 --topic odd-numbers --from-beginning
```

create console consumer to read messages from even-numbers topic
```bash
confluent-7.6.1/bin/kafka-console-consumer --bootstrap-server localhost:9092 --topic even-numbers --from-beginning
```