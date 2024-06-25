package com.example;

import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.KStream;

import java.util.Properties;

public class StreamsClient {
    public static void main(String[] args) {

        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("application.id", "streams-client");
        props.put("default.key.serde", "org.apache.kafka.common.serialization.Serdes$StringSerde");
        props.put("default.value.serde", "org.apache.kafka.common.serialization.Serdes$StringSerde");
        props.put("auto.offset.reset", "earliest");
        props.put(StreamsConfig.NUM_STREAM_THREADS_CONFIG, 3);

        // create a stream builder
        StreamsBuilder builder = new StreamsBuilder();

        // create a stream from numbers topic
        KStream<String,String> kStream= builder.stream("numbers");


        // filter out even numbers
        KStream<String,String> evenNumbers = kStream.filter((k,v) -> Integer.parseInt(v) % 2 == 0);

        // filter out odd numbers
        KStream<String,String> oddNumbers = kStream.filter((k,v) -> Integer.parseInt(v) % 2 != 0);

        // write even numbers to even-numbers topic
        evenNumbers
                .peek((k,v) -> System.out.println("Even number: " + v))
                .to("even-numbers");

        // write odd numbers to odd-numbers topic
        oddNumbers
                .peek((k,v) -> System.out.println("Odd number: " + v))
                .to("odd-numbers");

        // build the topology
        Topology topology = builder.build();

        // create a streams client
        KafkaStreams streams = new KafkaStreams(topology, props);

        // start the streams client
        streams.start();

        // shutdown hook to close the streams client
        Runtime.getRuntime().addShutdownHook(new Thread(streams::close));


    }
}
