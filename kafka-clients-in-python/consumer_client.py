from kafka import KafkaConsumer
from kafka.errors import KafkaError, WakeupException
import logging
import threading
import time

# Set up logging
logging.basicConfig(level=logging.INFO)
logger = logging.getLogger("ConsumerClient")

# Kafka configuration
bootstrap_servers = ["localhost:9092", "localhost:9093"]
group_id = "consumer-group-1"
topic = "topic1"

# Create Kafka consumer
consumer = KafkaConsumer(
    topic,
    bootstrap_servers=bootstrap_servers,
    group_id=group_id,
    key_deserializer=lambda k: k.decode('utf-8'),
    value_deserializer=lambda v: v.decode('utf-8'),
    auto_offset_reset='earliest',
    auto_commit_interval_ms=1000,
)

# Thread for handling graceful shutdown
shutdown_event = threading.Event()

def consume():
    try:
        while not shutdown_event.is_set():
            consumer.poll(timeout_ms=1000)
            for message in consumer:
                logger.info(
                    f"Received new record\nTopic: {message.topic}\nPartition: {message.partition}\nOffset: {message.offset}\nTimestamp: {message.timestamp}"
                )
    except WakeupException as e:
        logger.info(f"WakeupException caught: {e}")
    except Exception as e:
        logger.error(f"Unexpected error: {e}")
    finally:
        consumer.close()
        logger.info("The consumer is now gracefully closed")

# Thread to handle consumer logic
consumer_thread = threading.Thread(target=consume)
consumer_thread.start()

def shutdown():
    shutdown_event.set()
    consumer.wakeup()
    consumer_thread.join()

# Handle graceful shutdown
try:
    while True:
        time.sleep(1)
except KeyboardInterrupt:
    logger.info("Detected shutdown, let's exit by calling consumer.wakeup()...")
    shutdown()
