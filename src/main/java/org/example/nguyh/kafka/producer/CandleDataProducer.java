package org.example.nguyh.kafka.producer;

import org.example.nguyh.domain.Candle;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class CandleDataProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public CandleDataProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    /**
     * Sends candle data to the "candle_data" Kafka topic.
     *
     * @param candle the candle object containing open, high, low, and close prices
     */
    public void sendCandle(Candle candle) {
        String candleData = serializeCandle(candle);
        kafkaTemplate.send("candle_data", candleData);
        System.out.println("Candle data sent to Kafka: " + candleData);
    }

    /**
     * Serializes the Candle object into a JSON string.
     *
     * @param candle the candle object
     * @return the serialized JSON string
     */
    private String serializeCandle(Candle candle) {
        return "{\"open\":" + candle.getOpen() +
                ",\"high\":" + candle.getHigh() +
                ",\"low\":" + candle.getLow() +
                ",\"close\":" + candle.getClose() +
                ",\"ticks\":" + candle.getTicks() + "}";
    }
}
