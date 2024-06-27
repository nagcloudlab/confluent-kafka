#!/usr/bin/env python

from random import choice
from confluent_kafka import Producer
from confluent_kafka.schema_registry import SchemaRegistryClient, Schema
from confluent_kafka.schema_registry.avro import AvroSerializer
from confluent_kafka.serialization import StringSerializer, SerializationContext, MessageField

# Define the Avro schema for the value
value_schema_str = """
{
   "type": "record",
   "name": "Purchase",
   "fields": [
       {"name": "product", "type": "string"},
       {"name": "qty", "type": "int"}
   ]
}
"""

# Configure Schema Registry
schema_registry_conf = {'url': 'http://localhost:8081'}
schema_registry_client = SchemaRegistryClient(schema_registry_conf)

# Create a Schema object
value_schema = Schema(value_schema_str, schema_type="AVRO")

# Register the schema
subject = "python.purchases-value"
schema_id = schema_registry_client.register_schema(subject, value_schema)

# Create AvroSerializer instance
avro_serializer = AvroSerializer(schema_registry_client, value_schema_str)

# String serializer for the key
string_serializer = StringSerializer('utf_8')

# Configuration for Kafka Producer
producer_conf = {
    'bootstrap.servers': 'localhost:9092',
    'acks': 'all'
}

# Create Producer instance
producer = Producer(producer_conf)

# Optional per-message delivery callback (triggered by poll() or flush())
# when a message has been successfully delivered or permanently
# failed delivery (after retries).
def delivery_callback(err, msg):
    if err:
        print('ERROR: Message failed delivery: {}'.format(err))
    else:
        print("Produced event to topic {topic}: key = {key:12} value = {value:12}".format(
            topic=msg.topic(), key=msg.key().decode('utf-8'), value=msg.value().decode('utf-8')))

# Produce data by selecting random values from these lists.
topic = "python.purchases"
user_ids = ['eabara', 'jsmith', 'sgarcia', 'jbernard', 'htanaka', 'awalther']
products = ['book', 'alarm clock', 't-shirts', 'gift card', 'batteries']
quantities = [1, 2, 3, 4, 5]

count = 0
for _ in range(10):
    user_id = choice(user_ids)
    product = choice(products)
    qty = choice(quantities)
    value = {'product': product, 'qty': qty}
    
    # Serialize the key and value
    serialized_key = string_serializer(user_id, SerializationContext(topic, MessageField.KEY))
    serialized_value = avro_serializer(value, SerializationContext(topic, MessageField.VALUE))
    
    producer.produce(topic=topic, key=serialized_key, value=serialized_value, callback=delivery_callback)
    count += 1

# Block until the messages are sent.
producer.poll(10000)
producer.flush()
