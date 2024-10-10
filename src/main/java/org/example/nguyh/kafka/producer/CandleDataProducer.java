package org.example.nguyh.kafka.producer;

import org.example.nguyh.domain.Candle;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class CandleDataProducer {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    private static final String TOPIC = "candle_data";
    private ObjectMapper objectMapper = new ObjectMapper();

    public void sendCandle(Candle candle) {
        if (candle == null) {
            System.out.println("Candle is null, no data sent to Kafka topic.");
            return;  // Don't send a message if the candle is null
        }

        try {
            String candleJson = objectMapper.writeValueAsString(candle);
            kafkaTemplate.send(TOPIC, candleJson);
            System.out.println("Candle data sent to Kafka topic: " + candleJson);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}