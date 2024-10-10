package org.example.nguyh.kafka.consumer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

class CandleDataConsumerTest {

    @InjectMocks
    private CandleDataConsumer consumer;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testConsumeValidCandleData() {
        // Simulate a valid candle data string in JSON format
        String candleData = "{\"timestamp\":123456789,\"open\":5000.0,\"high\":5001.0,\"low\":5000.0,\"close\":5001.0,\"ticks\":1}";

        // Call the consume method with the candle data
        consumer.consume(candleData);

        // Since we're not interacting with any other services in this test (e.g., no database),
        // we don't need to verify anything here.
    }

    @Test
    public void testConsumeEmptyCandleData() {
        // Simulate an empty candle data string
        String emptyCandleData = "";

        // Call the consume method with empty data
        consumer.consume(emptyCandleData);

        // Ensure that nothing crashes, even if the data is empty
    }

    @Test
    public void testConsumeMalformedCandleData() {
        // Simulate malformed candle data
        String malformedData = "not_a_valid_json";

        // Call the consume method with malformed data
        consumer.consume(malformedData);

        // Ensure that the consumer handles the malformed data gracefully without throwing exceptions
    }
}

