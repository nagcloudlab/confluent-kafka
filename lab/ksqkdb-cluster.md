


start ksqldb-server-1
```bash
confluent-7.6.1/bin/ksql-server-start confluent-7.6.1/etc/ksqldb/ksql-server-1.properties
```

start ksqldb-server-2
```bash
confluent-7.6.1/bin/ksql-server-start confluent-7.6.1/etc/ksqldb/ksql-server-2.properties
```


open ksql-cli
```bash
confluent-7.6.1/bin/ksql http://localhost:8088
```
