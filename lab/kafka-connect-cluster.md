

set classpath ms sql server jdbc driver
```bash
export CLASSPATH=/home/nag/confluent-kafka/lab/jdbc-driver/mssql-jdbc-12.6.3.jre11.jar
```

start worker-1 in distributed mode
```bash
confluent-7.6.1/bin/connect-distributed confluent-7.6.1/etc/kafka/connect-distributed-worker-1.properties
```

start worker-2 in distributed mode
```bash
confluent-7.6.1/bin/connect-distributed confluent-7.6.1/etc/kafka/connect-distributed-worker-2.properties
```

