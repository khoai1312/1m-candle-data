package org.example.nguyh.kafka.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class CandleDataConsumer {

    /**
     * Listens for messages on the "candle_data" Kafka topic and processes the candle data.
     *
     * @param record the Kafka record containing candle data
     */
    @KafkaListener(topics = "candle_data", groupId = "candle_group")
    public void consume(ConsumerRecord<String, String> record) {
        String candleData = record.value();  // Extract the actual message from the record
        System.out.println("Consumed candle data: " + candleData);
    }
}
