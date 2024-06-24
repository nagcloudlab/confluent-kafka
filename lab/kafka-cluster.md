

confluent community-dition setup
=================================

verify java version
----------------------------
```bash
java -version
```

download confluent community edition
----------------------------------------------
```bash
curl -O https://packages.confluent.io/archive/7.6/confluent-7.6.1.tar.gz
```

extract confluent community edition
----------------------------------------------
```bash
tar -xvf confluent-7.6.1.tar.gz
```


start zookeeper
---------------------
```bash
confluent-7.6.1/bin/zookeeper-server-start confluent-7.6.1/etc/kafka/zookeeper.properties
```

start zookeeper-shell
---------------------
```bash
confluent-7.6.1/bin/zookeeper-shell localhost:2181
```

start kafka server 101
---------------------
```bash
confluent-7.6.1/bin/kafka-server-start confluent-7.6.1/etc/kafka/server-101.properties
```

start kafka server 102
---------------------
```bash
confluent-7.6.1/bin/kafka-server-start confluent-7.6.1/etc/kafka/server-102.properties
```

start kafka server 103
---------------------
```bash
confluent-7.6.1/bin/kafka-server-start confluent-7.6.1/etc/kafka/server-103.properties
```

