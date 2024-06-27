
https://github.com/provectus/kafka-ui


kafka ui
=================================


```bash
mkdir kafka-ui
curl -L https://github.com/provectus/kafka-ui/releases/download/v0.7.2/kafka-ui-api-v0.7.2.jar --output kafka-ui/kafka-ui-api-v0.7.2.jar
```


```properties
kafka:
  clusters:
    - name: local
      bootstrapServers: localhost:9092
```

```bash
touch kafka-ui/application.yml
```

```bash
java -Dspring.config.additional-location=application.yml --add-opens java.rmi/javax.rmi.ssl=ALL-UNNAMED -jar kafka-ui-api-v0.7.2.jar
```




# Avro schema for purchase event

```json
{
	"type": "record",
	"name": "Purchase",
	"namespace": "io.confluent.developer.avro",
	"fields": [
		{
			"name": "item",
			"type": {
				"type": "string",
				"avro.java.string": "String"
			}
		},
		{
			"name": "total_cost",
			"type": "double"
		},
		{
			"name": "customer_id",
			"type": {
				"type": "string",
				"avro.java.string": "String"
			}
		}
	]
}
```

# Register the schema

```bash
curl -X POST -H "Content-Type: application/vnd.schemaregistry.v1+json" \
  --data '{"schema": "{\"type\":\"record\",\"name\":\"Purchase\",\"namespace\":\"io.confluent.developer.avro\",\"fields\":[{\"name\":\"item\",\"type\":{\"type\":\"string\",\"avro.java.string\":\"String\"}},{\"name\":\"total_cost\",\"type\":\"double\"},{\"name\":\"customer_id\",\"type\":{\"type\":\"string\",\"avro.java.string\":\"String\"}}]}"}' \
  http://localhost:8081/subjects/purchases-value/versions | jq
```

# Update the schema

```json
{
  "type": "record",
  "name": "Purchase",
  "namespace": "io.confluent.developer.avro",
  "fields": [
    {
      "name": "item",
      "type": {
        "type": "string",
        "avro.java.string": "String"
      }
    },
    {
      "name": "total_cost",
      "type": "double"
    },
    {
      "name": "customer_id",
      "type": {
        "type": "string",
        "avro.java.string": "String"
      }
    },
    {
      "name": "purchase_date",
      "type": "string",
      "default": "2021-01-01"
    }
  ]
}
```

# Register the updated schema

```bash
curl -X POST -H "Content-Type: application/vnd.schemaregistry.v1+json" \
  --data '{"schema": "{\"type\":\"record\",\"name\":\"Purchase\",\"namespace\":\"io.confluent.developer.avro\",\"fields\":[{\"name\":\"item\",\"type\":{\"type\":\"string\",\"avro.java.string\":\"String\"}},{\"name\":\"total_cost\",\"type\":\"double\"},{\"name\":\"customer_id\",\"type\":{\"type\":\"string\",\"avro.java.string\":\"String\"}},{\"name\":\"purchase_date\",\"type\":\"string\",\"default\":\"2021-01-01\"}]}"}' \
  http://localhost:8081/subjects/purchases-value/versions | jq
```

