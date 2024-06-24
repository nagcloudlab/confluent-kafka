


start worker-1 in distributed mode
```bash
confluent-7.6.1/bin/connect-distributed confluent-7.6.1/etc/kafka/connect-distributed-worker-1.properties
```

start worker-2 in distributed mode
```bash
confluent-7.6.1/bin/connect-distributed confluent-7.6.1/etc/kafka/connect-distributed-worker-2.properties
```

list all connectors
```bash
curl -s http://localhost:8083/connectors | jq
```


create/deploy a file source connector

```bash
curl -X POST -H "Content-Type: application/json" --data @/home/nag/confluent-kafka/lab/connectors/file-source-connector.json http://localhost:8083/connectors | jq
```


create/deploy a file sink connector

```bash
curl -X POST -H "Content-Type: application/json" --data @/home/nag/confluent-kafka/lab/connectors/file-sink-connector.json http://localhost:8083/connectors | jq
```

get connector status
```bash
curl -s http://localhost:8083/connectors/file-source-connector/status | jq
```

get connector config
```bash
curl -s http://localhost:8083/connectors/file-source-connector | jq
```

get connector tasks
```bash
curl -s http://localhost:8083/connectors/file-source-connector/tasks | jq
```

get connector task status
```bash
curl -s http://localhost:8083/connectors/file-source-connector/tasks/0/status | jq
```

pause connector
```bash
curl -X PUT http://localhost:8083/connectors/file-source-connector/pause
```

resume connector
```bash
curl -X PUT http://localhost:8083/connectors/file-source-connector/resume
```

restart connector
```bash
curl -X POST http://localhost:8083/connectors/file-source-connector/restart
```

stop connector
```bash
curl -X DELETE http://localhost:8083/connectors/file-source-connector
```



deploy sqlserver source connector
```bash
curl -X POST -H "Content-Type: application/json" --data @/home/nag/confluent-kafka/lab/connectors/sqlserver-source-connector.json http://localhost:8083/connectors | jq
```


deploy snowflake sink connector
```bash
curl -X POST -H "Content-Type: application/json" --data @/home/nag/confluent-kafka/lab/connectors/snowflake-sink-connector.json http://localhost:8083/connectors | jq
```