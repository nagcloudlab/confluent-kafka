package com.example.ktable;

import com.example.StreamsUtils;
import org.apache.kafka.clients.admin.Admin;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;

import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

public class TopicLoader {

    public static void main(String[] args) throws IOException {
        runProducer();
    }

    public static void runProducer() throws IOException {
        Properties properties = StreamsUtils.loadProperties();
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

        try(Admin adminClient = Admin.create(properties);
            Producer<String, String> producer = new KafkaProducer<>(properties)) {
            final String inputTopic = properties.getProperty("ktable.input.topic");
            final String outputTopic = properties.getProperty("ktable.output.topic");
            var topics = List.of(StreamsUtils.createTopic(inputTopic), StreamsUtils.createTopic(outputTopic));
            adminClient.createTopics(topics);

            Callback callback = (metadata, exception) -> {
                if(exception != null) {
                    System.out.printf("Producing records encountered error %s %n", exception);
                } else {
                    System.out.printf("Record produced - offset - %d timestamp - %d %n", metadata.offset(), metadata.timestamp());
                }

            };

            var rawRecords = List.of("orderNumber-1001",
                                              "orderNumber-5000",
                                               "orderNumber-999",
                                               "orderNumber-9000",
                                               "bogus-1",
                                               "bogus-2",
                                               "orderNumber-6000");
            var producerRecords = rawRecords.stream().map(r -> new ProducerRecord<String, String>(inputTopic,"foo", r)).collect(Collectors.toList());
            producerRecords.forEach((pr -> producer.send(pr, callback)));


        }
    }
}
