package cn.chendihao;

import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;

/**
 * From kafka official docs in https://cwiki.apache.org/confluence/display/KAFKA/Consumer+Group+Example.
 */
public class ConsumerTest implements Runnable {
    private KafkaStream stream;
    private int threadNum;

    public ConsumerTest(KafkaStream a_stream, int a_threadNumber) {
        stream = a_stream;
        threadNum = a_threadNumber;
    }

    @Override
    public void run() {
        ConsumerIterator<byte[], byte[]> it = stream.iterator();
        while (it.hasNext()) {
            System.out.println("Thread " + threadNum + ": " + new String(it.next().message()));
        }
        System.out.println("Shutting down Thread: " + threadNum);
    }
}