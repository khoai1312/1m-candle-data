package org.example.nguyh.kafka.producer;

import org.springframework.kafka.core.KafkaTemplate;
import org.example.nguyh.domain.Candle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.*;

public class CandleDataProducerTest {

    private KafkaTemplate<String, String> kafkaTemplate;
    private CandleDataProducer candleKafkaProducer;

    @BeforeEach
    public void setup() {
        kafkaTemplate = Mockito.mock(KafkaTemplate.class);
        candleKafkaProducer = new CandleDataProducer(kafkaTemplate);
    }

    @Test
    public void testSendCandleWithValidData() {
        Candle candle = new Candle(System.currentTimeMillis());
        candle.setOpen(5000.5);
        candle.setHigh(5001.5);
        candle.setLow(4999.5);
        candle.setClose(5000.5);
        candle.setTicks(100);

        candleKafkaProducer.sendCandle(candle);

        String expectedMessage = "{\"open\":5000.5,\"high\":5001.5,\"low\":4999.5,\"close\":5000.5,\"ticks\":100}";
        verify(kafkaTemplate, times(1)).send("candle_data", expectedMessage);
    }

    // Edge case: Send a null candle
    @Test
    public void testSendCandleWithNullCandle() {
        candleKafkaProducer.sendCandle(null);
        verify(kafkaTemplate, never()).send(anyString(), anyString());
    }
}
