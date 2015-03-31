package cn.chendihao;

import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Kafka example code for smoke test.
 */
public class SmokeKafka {

    public static void main(String[] argv) {

        System.out.println("Start smoke test for kafka");

        // Basic kafka configuration and change for your kafka cluster
        String broker = "localhost:9092"; 
        String zookeeper = "localhost:2181";
        String topic = "test-topic";
        
        // New kafka producer
        Properties props = new Properties();
        props.put("metadata.broker.list", broker);
        props.put("serializer.class", "kafka.serializer.StringEncoder");
        props.put("partitioner.class", "cn.chendihao.SimplePartitioner");
        props.put("request.required.acks", "1");
        ProducerConfig config = new ProducerConfig(props);
        Producer<String, String> producer = new Producer<String, String>(config);


        // Produce data in kafka
        long events = 5;
        Random random = new Random();
        for (long nEvents = 0; nEvents < events; nEvents++) {
            long runtime = new Date().getTime();
            String ip = "192.168.2." + random.nextInt(255);
            String msg = runtime + ", www.example.com, " + ip;
            KeyedMessage<String, String> data = new KeyedMessage<String, String>(topic, ip, msg);
            producer.send(data);
            System.out.println("Success to insert message " + msg);
        }

        // Close producer
        producer.close();

        // New consumer
        String groupId = "test-group";
        Properties props2 = new Properties();
        props2.put("zookeeper.connect", zookeeper);
        props2.put("group.id", groupId);
        props2.put("zookeeper.session.timeout.ms", "400");
        props2.put("zookeeper.sync.time.ms", "200");
        props2.put("auto.commit.interval.ms", "1000");
        ConsumerConnector consumer = Consumer.createJavaConsumerConnector(new ConsumerConfig(props2));
        Map<String, Integer> topicCountMap = new HashMap<String, Integer>();

        // Consume data from kafka
        int threads = 1;
        topicCountMap.put(topic, threads);
        Map<String, List<KafkaStream<byte[], byte[]>>> consumerMap = consumer.createMessageStreams(topicCountMap);
        List<KafkaStream<byte[], byte[]>> streams = consumerMap.get(topic);
        //now launch all threads
        ExecutorService executor = Executors.newFixedThreadPool(threads);
        //now create an object to consume the messages
        int threadNum = 0;
        for (final KafkaStream stream : streams) {
            executor.submit(new ConsumerTest(stream, threadNum));
            threadNum++;
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Recycle the resources
        if (consumer != null) {
            consumer.shutdown();
        }
        if (executor != null) {
            executor.shutdown();
        }

        System.out.println("Stop smoke test for kafka");

    }

}

