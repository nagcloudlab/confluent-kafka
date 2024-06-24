



kafka topic management
======================

Create a topic
-------------

```bash
confluent-7.6.1/bin/kafka-topics --bootstrap-server localhost:9092 --create --topic topic1 --partitions 1
confluent-7.6.1/bin/kafka-topics --bootstrap-server localhost:9092 --create --topic topic2 --partitions 2
confluent-7.6.1/bin/kafka-topics --bootstrap-server localhost:9092 --create --topic topic3 --partitions 3
confluent-7.6.1/bin/kafka-topics --bootstrap-server localhost:9092 --create --topic topic4 --partitions 12
```

List topics
-----------
```bash
confluent-7.6.1/bin/kafka-topics --bootstrap-server localhost:9092 --list
```

Describe a topic
----------------
```bash
confluent-7.6.1/bin/kafka-topics --bootstrap-server localhost:9092 --describe --topic topic1
```

Delete a topic
--------------
```bash
confluent-7.6.1/bin/kafka-topics --bootstrap-server localhost:9092 --delete --topic topic1
```


Increase partitions
-------------------
```bash
confluent-7.6.1/bin/kafka-topics --bootstrap-server localhost:9092 --alter --topic topic1 --partitions 3
```

Decrease partitions
-------------------
```bash
confluent-7.6.1/bin/kafka-topics --bootstrap-server localhost:9092 --alter --topic topic1 --partitions 1
```

