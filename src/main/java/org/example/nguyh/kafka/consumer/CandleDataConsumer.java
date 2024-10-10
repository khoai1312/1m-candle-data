package org.example.nguyh.kafka.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class CandleDataConsumer {

    @KafkaListener(topics = "candle_data", groupId = "candle_consumer_group")
    public void consume(String candleData) {
        System.out.println("Candle data consumed from Kafka topic: " + candleData);
    }
}