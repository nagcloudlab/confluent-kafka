package com.example;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class ConsumerClient_3 {
    private static final Logger logger = LoggerFactory.getLogger(ConsumerClient_3.class);

    public static void main(String[] args) {

        // Properties object to hold all necessary configuration settings
        Properties props = new Properties();

        // The list of broker addresses in your Kafka cluster
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092,localhost:9093");
        // Unique string that identifies the consumer group this consumer belongs to
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "consumer-group-2");
        props.put(ConsumerConfig.GROUP_INSTANCE_ID_CONFIG, "consumer-instance-3"); // unique id for the consumer instance in the group ( static membership)

        // Deserializer class for key that implements the Deserializer interface
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        // Deserializer class for value that implements the Deserializer interface
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());

        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");

        props.put(ConsumerConfig.PARTITION_ASSIGNMENT_STRATEGY_CONFIG,"org.apache.kafka.clients.consumer.CooperativeStickyAssignor");

        // Create a KafkaConsumer instance
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(List.of("topic1"));


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

        try {
            // Poll for new messages
            while (true) {
//                logger.info("Polling for new messages...");
                ConsumerRecords<String, String> consumerRecords = consumer.poll(Duration.ofSeconds(1)); // fetch request
//                logger.info("Received records "+consumerRecords.count());
                //Map<TopicPartition, OffsetAndMetadata> currentProcessedOffsets = new HashMap<>();
                for (var record : consumerRecords) {
                    logger.info(
                            "Received new record\nTopic: {}\nPartition:{}\nOffset: {}\nTimestamp: {}",
                            record.topic(),
                            // record.key(),
                            // record.value(),
                            record.partition(),
                            record.offset(),
                            record.timestamp());

                    TimeUnit.MILLISECONDS.sleep(2);

                    //currentProcessedOffsets.put(new TopicPartition(record.topic(), record.partition()), new OffsetAndMetadata(record.offset() + 1));
                    //consumer.commitSync(currentProcessedOffsets); // record by record commit
                }
                //consumer.commitSync(); // commit the offset
            }
        } catch (WakeupException e) {
            System.out.println("Wake up exception! " + e);
        } catch (Exception e) {
            System.out.println("Unexpected exception " + e);
        } finally {
            consumer.close(); // Leaving Request to Group Coordinator
            System.out.println("The consumer is now gracefully closed");
        }

    }
}
