



# Start the Debezium SQL Server connector

```bash
curl -X POST -H "Content-Type: application/json" --data @/home/nag/confluent-kafka/demo/sqlserver-and-snowflake-connectors/sqlserver-source-connector.json http://localhost:8083/connectors | jq
```

# status of sqlserver-source connector
```bash
curl -X GET http://localhost:8083/connectors/sqlserver-source-cdc-connector/status | jq


# update the sqlserver-source connector


# restart the sqlserver-source connector
```bash
curl -X POST http://localhost:8083/connectors/sqlserver-source-cdc-connector/restart | jq
```

--------------------------------------------------------------------------------------------


# Start the Snowflake connector
```bash
curl -X POST -H "Content-Type: application/json" --data @/home/nag/confluent-kafka/demo/sqlserver-and-snowflake-connectors/snowflake-sink-connector.json http://localhost:8083/connectors | jq
```


# status of snowflake-sink connector
```bash
curl -X GET http://localhost:8083/connectors/snowflake-sink-connector/status | jq
```

--------------------------------------------------------------------------------------------

# list connectors
```bash
curl -X GET http://localhost:8083/connectors | jq
```

# Delete the connectors
```bash
curl -X DELETE http://localhost:8083/connectors/sqlserver-source-cdc-connector
curl -X DELETE http://localhost:8083/connectors/snowflake-sink-connector
```


--------------------------------------------------------------------------------------------

