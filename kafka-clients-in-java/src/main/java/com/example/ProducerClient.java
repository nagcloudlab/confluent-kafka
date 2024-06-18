package com.example;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;

import java.util.List;
import java.util.Properties;

public class ProducerClient {

    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(ProducerClient.class);

    public static void main(String[] args) {

        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092,localhost:9093,localhost:9094");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                "org.apache.kafka.common.serialization.StringSerializer");
        // props.put(ProducerConfig.PARTITIONER_CLASS_CONFIG,
        // "com.example.CustomPartitioner");

        KafkaProducer<String, String> producer = new KafkaProducer<>(props);

        String topic = "topic1";
        for (int i = 0; i < 32; i++) {
            // List<String> languages = List.of("en", "es", "fr", "de", "it", "pt", "ru",
            // "zh", "ja", "ko");
            // String key = languages.get(i % languages.size());
            String value = "Hey Kafka!".repeat(100); // 1kb message
            ProducerRecord<String, String> record = new ProducerRecord<>(topic, value);
            producer.send(record, (recordMetadata, exception) -> {
                if (exception == null) {
                    logger.info("Received new metadata \nTopic: {}\nKey: {}\nPartition: {}\nOffset: {}\nTimestamp: {}",
                            recordMetadata.topic(),
                            null,
                            recordMetadata.partition(),
                            recordMetadata.offset(),
                            recordMetadata.timestamp());
                } else {
                    logger.error("Error while producing: {}", exception.getMessage());
                }
            });
        }

        producer.close();

    }
}
