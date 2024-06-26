

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