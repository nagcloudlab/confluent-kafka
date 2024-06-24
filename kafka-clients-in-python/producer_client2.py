

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
    value_serializer=lambda v: v.encode('utf-8'),
    acks="all",
)

topic = "numbers"

try:
    for i in range(1000000):  # Use a smaller number for testing
        future = producer.send(topic,  value=str(i))
        # Synchronous send, to make the example simpler
        try:
            record_metadata = future.get(timeout=1000)
        except KafkaError as e:
            logger.error(f"Error while producing: {e}")
        
        time.sleep(1)

finally:
    producer.close()
