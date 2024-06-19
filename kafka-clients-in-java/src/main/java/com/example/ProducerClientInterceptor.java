package com.example;

import java.util.Map;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.slf4j.Logger;

public class ProducerClientInterceptor
        implements org.apache.kafka.clients.producer.ProducerInterceptor<String, String> {

    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(ProducerClientInterceptor.class);

    @Override
    public ProducerRecord<String, String> onSend(ProducerRecord<String, String> record) {
        // modify record before sending
        record.headers().add(new RecordHeader("header-key", "header-value".getBytes()));
        return record;
    }

    @Override
    public void onAcknowledgement(RecordMetadata metadata, Exception exception) {
        if (exception == null) {
            logger.info("Received new metadata \nTopic: {}\nPartition: {}\nOffset: {}\nTimestamp: {}",
                    metadata.topic(),
                    metadata.partition(),
                    metadata.offset(),
                    metadata.timestamp());
        } else {
            logger.error("Error while producing: {}", exception.getMessage());
        }
    }

    @Override
    public void close() {

    }

    @Override
    public void configure(Map<String, ?> configs) {

    }
}