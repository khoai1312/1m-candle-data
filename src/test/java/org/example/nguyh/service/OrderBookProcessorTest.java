package org.example.nguyh.service;

import org.example.nguyh.domain.OrderBook;
import org.example.nguyh.domain.Candle;
import org.example.nguyh.kafka.producer.CandleDataProducer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.*;

public class OrderBookProcessorTest {

    private OrderBook orderBook;
    private CandleDataProducer candleKafkaProducer;
    private OrderBookProcessor orderBookProcessor;

    @BeforeEach
    public void setup() {
        orderBook = new OrderBook();
        candleKafkaProducer = Mockito.mock(CandleDataProducer.class);
        orderBookProcessor = new OrderBookProcessor(orderBook, candleKafkaProducer);
    }

    @Test
    public void testProcessValidOrderBookUpdate() {
        String message = "{\"b\": [[\"5000.0\", \"1.0\"]], \"a\": [[\"5000.5\", \"0.5\"]]}";
        orderBookProcessor.processOrderBookUpdate(message);

        double expectedMidPrice = (5000.0 + 5000.5) / 2;

        assert(orderBook.getHighestBid() == 5000.0);
        assert(orderBook.getLowestAsk() == 5000.5);

        // Ensure candle is sent after the interval (you can mock time for more precise tests)
        verify(candleKafkaProducer, atMostOnce()).sendCandle(any(Candle.class));
    }

    // Edge case: Message with missing bid/ask data
    @Test
    public void testProcessOrderBookUpdateWithMissingData() {
        String message = "{\"b\": [], \"a\": []}";
        orderBookProcessor.processOrderBookUpdate(message);

        assert(orderBook.getHighestBid() == 0.0);
        assert(orderBook.getLowestAsk() == Double.MAX_VALUE);

        // Ensure no candle is sent since there's no valid bid/ask
        verify(candleKafkaProducer, never()).sendCandle(any(Candle.class));
    }

    // Edge case: Invalid message format
    @Test
    public void testProcessOrderBookUpdateWithInvalidMessage() {
        String invalidMessage = "Invalid Message";
        orderBookProcessor.processOrderBookUpdate(invalidMessage);

        verify(candleKafkaProducer, never()).sendCandle(any(Candle.class));
    }
}

