

deploy sqlserver-source connector
```bash
curl -X POST -H "Content-Type: application/json" --data @/home/nag/confluent-kafka/lab/connectors/sqlserver-source-cdc-connector.json http://localhost:8083/connectors | jq
```

status of sqlserver-source connector
```bash
curl -X GET http://localhost:8083/connectors/sqlserver-source-cdc-connector/status | jq
```

delete sqlserver-source connector
```bash
curl -X DELETE http://localhost:8083/connectors/sqlserver-source-cdc-connector | jq
```

list connectors
```bash
curl -X GET http://localhost:8083/connectors | jq
```

deploy snowflake-sink connector
```bash
curl -X POST -H "Content-Type: application/json" --data @/home/nag/confluent-kafka/lab/connectors/snowflake-sink-connector.json http://localhost:8083/connectors | jq
```

status of snowflake-sink connector
```bash
curl -X GET http://localhost:8083/connectors/snowflake-sink-connector/status | jq
```

delete snowflake-sink connector
```bash
curl -X DELETE http://localhost:8083/connectors/snowflake-sink-connector | jq
```