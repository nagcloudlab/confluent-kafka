package com.example;

import org.apache.kafka.common.Cluster;

import java.util.Map;

public class CustomPartitioner implements org.apache.kafka.clients.producer.Partitioner{

    @Override
    public int partition(String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster) {
        // algorithm to determine partition
        return key.toString().hashCode() % cluster.partitionCountForTopic(topic);
    }

    @Override
    public void close() {

    }

    @Override
    public void configure(Map<String, ?> configs) {

    }
}
