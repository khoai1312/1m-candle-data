package org.example.nguyh.kafka.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CandleDataConsumerTest {

    private CandleDataConsumer candleKafkaConsumer;

    @BeforeEach
    public void setup() {
        candleKafkaConsumer = new CandleDataConsumer();
    }

    @Test
    public void testConsumeValidMessage() {
        // Create a mock ConsumerRecord
        ConsumerRecord<String, String> record = new ConsumerRecord<>("candle_data", 0, 0, "key", "{\"open\":5000.0,\"high\":5000.5,\"low\":4999.5,\"close\":5000.0,\"ticks\":100}");

        // Call the consume method with the ConsumerRecord
        candleKafkaConsumer.consume(record);

        // Verify that the message was consumed (if additional processing logic exists, you can add assertions here)
        System.out.println("Consumed valid message: " + record.value());
    }

    // Edge case: Consume a null message
    @Test
    public void testConsumeNullMessage() {
        // Create a ConsumerRecord with a null value
        ConsumerRecord<String, String> record = new ConsumerRecord<>("candle_data", 0, 0, "key", null);

        // Call the consume method with the null message
        candleKafkaConsumer.consume(record);

        // Ensure it doesn't throw any exceptions
    }
}



