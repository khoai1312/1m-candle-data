package org.example.nguyh.kafka.producer;

import org.example.nguyh.domain.Candle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.kafka.core.KafkaTemplate;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class CandleDataProducerTest {

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    @InjectMocks
    private CandleDataProducer candleKafkaProducer;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSendCandleWithValidData() {
        // Create a mock Candle object with valid data
        Candle candle = new Candle(System.currentTimeMillis());

        // Add the first tick
        candle.addTick(5000.5, 1.0);  // First tick (sets open, high, low, close to 5000.5)

        // Add the second tick to update high and close
        candle.addTick(5001.5, 1.0);  // Second tick (updates high to 5001.5, close to 5001.5)

        // Print the candle state for debugging
        System.out.println("Candle state: open=" + candle.getOpen() +
                ", high=" + candle.getHigh() +
                ", low=" + candle.getLow() +
                ", close=" + candle.getClose());

        // Call the sendCandle method
        candleKafkaProducer.sendCandle(candle);

        // Verify that KafkaTemplate sends the candle data to the correct topic
        verify(kafkaTemplate, times(1)).send(eq("candle_data"), eq("{\"open\":5000.5,\"high\":5001.5,\"low\":5000.5,\"close\":5001.5,\"ticks\":2}"));
    }

    @Test
    public void testSendCandleWithNullCandle() {
        // Call sendCandle with a null candle (should handle gracefully)
        candleKafkaProducer.sendCandle(null);

        // Verify that KafkaTemplate is never called
        verify(kafkaTemplate, never()).send(anyString(), anyString());
    }

    @Test
    public void testSendCandleWithEmptyCandle() {
        // Create an empty candle (no tick data)
        Candle emptyCandle = new Candle(System.currentTimeMillis());

        // Call sendCandle with an empty candle
        candleKafkaProducer.sendCandle(emptyCandle);

        // Verify that KafkaTemplate sends the empty candle data to the correct topic
        verify(kafkaTemplate, times(1)).send(eq("candle_data"), eq("{\"open\":0.0,\"high\":0.0,\"low\":0.0,\"close\":0.0,\"ticks\":0}"));
    }
}