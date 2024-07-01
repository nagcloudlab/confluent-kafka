import pandas as pd
from faker import Faker
from confluent_kafka import SerializingProducer
from confluent_kafka.schema_registry import SchemaRegistryClient
from confluent_kafka.schema_registry.avro import AvroSerializer
from confluent_kafka.serialization import StringSerializer

# Initialize Faker
fake = Faker()

# Define possible transaction types
transaction_types = ['DEBIT', 'CREDIT']

# Generate a list of random transactions
def generate_transactions(num_transactions):
    transactions = []
    for _ in range(num_transactions):
        transaction = {
            'TransactionID': fake.unique.random_int(min=1, max=100000),
            'TransactionDate': fake.date_time_this_year().strftime('%Y-%m-%d %H:%M:%S'),
            'Amount': round(fake.random_number(digits=4) / 100, 2),
            'Currency': fake.currency_code(),
            'TransactionType': fake.random_element(transaction_types),
            'Description': fake.sentence(nb_words=6)
            
        }
        transactions.append(transaction)
    return transactions

# Create a DataFrame
num_transactions = 100
transactions = generate_transactions(num_transactions)
df = pd.DataFrame(transactions)

# Print a sample of the generated data
print(df.head())

# Define the Avro schemas
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

key_schema_str = """
{
    "type": "string"
}
"""

# Schema Registry configuration
schema_registry_conf = {
    'url': 'http://localhost:8081'  # Replace with your Schema Registry URL if different
}

# Initialize Schema Registry client
schema_registry_client = SchemaRegistryClient(schema_registry_conf)

# Initialize AvroSerializer
key_serializer = StringSerializer('utf_8')
value_serializer = AvroSerializer(schema_registry_client, value_schema_str)

# Kafka configuration
producer_conf = {
    'bootstrap.servers': 'localhost:9093',  # Replace with your Kafka broker(s) if different
    'key.serializer': key_serializer,
    'value.serializer': value_serializer,
    'acks': 'all',
    'security.protocol': 'SSL',
    # 'ssl.truststore.location': '/home/nag/confluent-kafka/demo/security/kafka.client.truststore.jks',
    # 'ssl.truststore.password': 'changeit',
    'ssl.ca.location': '/home/nag/confluent-kafka/demo/security/ca-cert',
    'ssl.keystore.location': '/home/nag/confluent-kafka/demo/security/kafka.client.keystore.jks',
    'ssl.keystore.password': 'changeit'
}

# Initialize Kafka producer
producer = SerializingProducer(producer_conf)

# Function to send record to Kafka
def send_to_kafka(topic, key, value):
    producer.produce(topic=topic, key=key, value=value)
    producer.flush()

# Send transactions to Kafka
for index, row in df.iterrows():
    transaction = {
        "TransactionID": row['TransactionID'],
        "TransactionDate": row['TransactionDate'],
        "Amount": row['Amount'],
        "Currency": row['Currency'],
        "Description": row['Description'],
        "TransactionType": row['TransactionType']
    }
    send_to_kafka('transactions_topic', str(row['TransactionID']), transaction)

print("Transactions sent to Kafka successfully.")
