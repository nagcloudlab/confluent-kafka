package com.example;

import java.time.Duration;
import java.util.List;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConsumerClient {
    private static final Logger logger = LoggerFactory.getLogger(ConsumerClient.class);

    public static void main(String[] args) {

        // Properties object to hold all necessary configuration settings
        Properties props = new Properties();

        // The list of broker addresses in your Kafka cluster
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092,localhost:9093");
        // Unique string that identifies the consumer group this consumer belongs to
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "consumer-group-1");

        // Deserializer class for key that implements the Deserializer interface
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        // Deserializer class for value that implements the Deserializer interface
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());

        // Create a KafkaConsumer instance
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(List.of("topic1"));

        try {
            // Poll for new messages
            while (true) {
                ConsumerRecords<String, String> consumerRecords = consumer.poll(Duration.ofSeconds(1)); // fetch request
                                                                                                        // to
                for (var record : consumerRecords) {
                    logger.info(
                            "Received new record\nTopic: {}\nPartition:{}\nOffset: {}\nTimestamp: {}",
                            record.topic(),
                            // record.key(),
                            // record.value(),
                            record.partition(),
                            record.offset(),
                            record.timestamp());
                }
            }
        } catch (WakeupException e) {
            System.out.println("Wake up exception! " + e);
        } catch (Exception e) {
            System.out.println("Unexpected exception " + e);
        } finally {
            consumer.close(); // Leaving Request to Group Coordinator
            System.out.println("The consumer is now gracefully closed");
        }

        final Thread mainThread = Thread.currentThread();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Detected a shutdown, let's exit by calling consumer.wakeup()...");
            consumer.wakeup();
            // join the main thread to allow the execution of the code in the main thread
            try {
                mainThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }));

    }
}
