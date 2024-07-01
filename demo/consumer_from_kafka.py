from confluent_kafka import DeserializingConsumer
from confluent_kafka.schema_registry import SchemaRegistryClient
from confluent_kafka.schema_registry.avro import AvroDeserializer
from confluent_kafka.serialization import StringDeserializer
import json

# Schema Registry configuration
schema_registry_conf = {
    'url': 'http://localhost:8081'  # Replace with your Schema Registry URL if different
}

schema_registry_client = SchemaRegistryClient(schema_registry_conf)

# Define the Avro schema for deserialization
value_schema_str = """
{
    "namespace": "example.avro",
    "type": "record",
    "name": "Transaction",
    "fields": [
        {"name": "TransactionID", "type": "int"},
        {"name": "TransactionDate", "type": "string"},
        {"name": "Amount", "type": "float"},
        {"name": "Currency", "type": "string"},
        {"name": "Description", "type": "string"},
        {"name": "TransactionType", "type": "string"}
    ]
}
"""

# Initialize AvroDeserializer
value_deserializer = AvroDeserializer(schema_str=value_schema_str, schema_registry_client=schema_registry_client)
key_deserializer = StringDeserializer('utf_8')

# Consumer configuration
consumer_conf = {
    'bootstrap.servers': 'localhost:9093',  # Replace with your Kafka broker(s) if different
    'key.deserializer': key_deserializer,
    'value.deserializer': value_deserializer,
    'group.id': 'transactions_group',
    'auto.offset.reset': 'earliest',
    'security.protocol': 'SSL',
    # 'ssl.truststore.location': '/home/nag/confluent-kafka/demo/security/kafka.client.truststore.jks',
    # 'ssl.truststore.password': 'changeit',
    'ssl.ca.location': '/home/nag/confluent-kafka/demo/security/ca-cert',
    'ssl.keystore.location': '/home/nag/confluent-kafka/demo/security/kafka.client.keystore.jks',
    'ssl.keystore.password': 'changeit'
}

# Initialize the consumer
consumer = DeserializingConsumer(consumer_conf)
consumer.subscribe(['transactions_topic'])

# Consume messages
try:
    while True:
        msg = consumer.poll(1.0)  # Timeout set to 1 second
        if msg is None:
            continue
        if msg.error():
            print(f"Consumer error: {msg.error()}")
            continue

        key = msg.key()
        value = msg.value()
        print(f"Consumed record with key {key} and value {json.dumps(value, indent=2)}")
except KeyboardInterrupt:
    pass
finally:
    # Close the consumer
    consumer.close()
