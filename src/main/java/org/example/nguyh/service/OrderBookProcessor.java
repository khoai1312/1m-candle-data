package org.example.nguyh.service;

import org.example.nguyh.domain.Candle;
import org.example.nguyh.domain.OrderBook;
import org.example.nguyh.kafka.producer.CandleDataProducer;

import java.util.logging.Logger;

/**
 * OrderBookProcessor handles the processing of order book updates, mid-price calculation,
 * and sending candle data to Kafka.
 */
public class OrderBookProcessor {

    private static final Logger LOGGER = Logger.getLogger(OrderBookProcessor.class.getName());
    private OrderBook orderBook;
    private CandleDataProducer kafkaProducer;
    private long nextMinuteTimestamp;
    private double openMidPrice;
    private double highMidPrice;
    private double lowMidPrice;
    private double closeMidPrice;
    private boolean firstTick = true;

    public OrderBookProcessor(OrderBook orderBook, CandleDataProducer kafkaProducer) {
        this.orderBook = orderBook;
        this.kafkaProducer = kafkaProducer;
        this.nextMinuteTimestamp = System.currentTimeMillis() + 60_000;
    }

    /**
     * Processes incoming order book updates and calculates mid-price.
     *
     * @param message the order book update message received from the WebSocket
     */
    public void processOrderBookUpdate(String message) {
        // Update the order book with parsed data (parsing logic needs to be implemented)
        orderBook.updateFromMessage(message);

        // Calculate mid-price based on highest bid and lowest ask
        if (orderBook.getHighestBid() < orderBook.getLowestAsk()) {
            double midPrice = (orderBook.getHighestBid() + orderBook.getLowestAsk()) / 2;

            // Set open price for the first tick of the minute
            if (firstTick) {
                openMidPrice = midPrice;
                highMidPrice = midPrice;
                lowMidPrice = midPrice;
                firstTick = false;
            }

            // Update high and low mid prices
            highMidPrice = Math.max(highMidPrice, midPrice);
            lowMidPrice = Math.min(lowMidPrice, midPrice);

            // Update close price with the latest mid-price
            closeMidPrice = midPrice;
        } else {
            LOGGER.warning("Sanity check failed: highest bid >= lowest ask.");
        }

        // Send candle data to Kafka every minute
        if (System.currentTimeMillis() >= nextMinuteTimestamp) {
            sendCandleToKafka();
            firstTick = true;  // Reset for the next minute
            nextMinuteTimestamp = System.currentTimeMillis() + 60_000;  // Reset the timestamp for the next minute
        }
    }

    /**
     * Sends the calculated candle data to Kafka.
     */
    private void sendCandleToKafka() {
        if (kafkaProducer != null) {
            Candle candle = new Candle(System.currentTimeMillis());
            candle.setOpen(openMidPrice);
            candle.setHigh(highMidPrice);
            candle.setLow(lowMidPrice);
            candle.setClose(closeMidPrice);
            candle.setTicks(orderBook.getTickCount());

            kafkaProducer.sendCandle(candle);
            LOGGER.info("Candle data sent to Kafka: " + candle);
        } else {
            LOGGER.warning("Kafka producer is not set. Cannot send candle data.");
        }
    }
}

