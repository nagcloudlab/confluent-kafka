package com.example;

import java.time.Duration;
import java.util.Collection;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConsumerClient{
    private static final Logger logger = LoggerFactory.getLogger(ConsumerClient.class);

    public static void main(String[] args) {


        // Properties object to hold all necessary configuration settings
        Properties props = new Properties();

        // The list of broker addresses in your Kafka cluster
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092,localhost:9093,localhost:9094");

        // Unique string that identifies the consumer group this consumer belongs to
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "consumer-group-2");
        // Unique string that identifies the consumer instance within the consumer group
//        props.put(ConsumerConfig.GROUP_INSTANCE_ID_CONFIG, "consumer-group-instance-1");

        // Deserializer class for key that implements the Deserializer interface
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        // Deserializer class for value that implements the Deserializer interface
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());

        // What to do when there is no initial offset in Kafka or if the current offset does not exist any more on the server
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");

        // If true, the consumer's offset will be periodically committed in the background
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
        // The frequency in milliseconds that the consumer offsets are auto-committed to Kafka
        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "5000");

        // The maximum number of records returned in a single call to poll()
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, "500");
        // The minimum amount of data the server should return for a fetch request
        props.put(ConsumerConfig.FETCH_MIN_BYTES_CONFIG, "1");
        // The maximum amount of time the server will block before answering the fetch request
        props.put(ConsumerConfig.FETCH_MAX_WAIT_MS_CONFIG, "500");
        // The maximum amount of data the server should return per partition
        props.put(ConsumerConfig.MAX_PARTITION_FETCH_BYTES_CONFIG, "1048576"); // 1 MB

        // The expected time between heartbeats to the consumer coordinator
        props.put(ConsumerConfig.HEARTBEAT_INTERVAL_MS_CONFIG, "3000");
        // The maximum amount of time the group coordinator will wait for each member to send a heartbeat
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "45000");

        // The maximum amount of time the consumer expects a message takes to be processed by the application
        props.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, "300000");

        // Partition assignment strategy
        props.put(ConsumerConfig.PARTITION_ASSIGNMENT_STRATEGY_CONFIG, CooperativeStickyAssignor.class.getName());

        // Create a KafkaConsumer instance
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(List.of("topic1"), new ConsumerRebalanceListener(){
            @Override
            public void onPartitionsAssigned(Collection<TopicPartition> partitions) {
                logger.info("Partitions assigned {}", partitions);
            }
            @Override
            public void onPartitionsRevoked(Collection<TopicPartition> partitions) {
                logger.info("Partitions revoked {}", partitions);
                consumer.commitSync(); // commit the offset
            }
        });

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
