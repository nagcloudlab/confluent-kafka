

from kafka import KafkaProducer
from kafka.errors import KafkaError
import logging
import time

# Set up logging
logging.basicConfig(level=logging.INFO)
logger = logging.getLogger("ProducerClient")

# Kafka configuration
bootstrap_servers = ["localhost:9092", "localhost:9093", "localhost:9094"]
producer = KafkaProducer(
    bootstrap_servers=bootstrap_servers,
    key_serializer=lambda k: k.encode('utf-8'),
    value_serializer=lambda v: v.encode('utf-8'),
    acks="all",
)

topic = "topic1"
languages = ["en", "es", "fr", "de", "it", "pt", "ru", "zh", "ja", "ko"]

try:
    for i in range(1000000):  # Use a smaller number for testing
        key = languages[i % len(languages)]
        value = "Hey Kafka!" * 100  # 1kb message
        future = producer.send(topic, key=key, value=value)
        # Synchronous send, to make the example simpler
        try:
            record_metadata = future.get(timeout=10)
            logger.info(f"Received new metadata \nTopic: {record_metadata.topic}\nKey: {key}\nPartition: {record_metadata.partition}\nOffset: {record_metadata.offset}\nTimestamp: {record_metadata.timestamp}")
        except KafkaError as e:
            logger.error(f"Error while producing: {e}")
        
        time.sleep(1)

finally:
    producer.close()
