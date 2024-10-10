package org.example.nguyh.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.NavigableMap;

import static org.junit.jupiter.api.Assertions.*;

public class OrderBookTest {

    private OrderBook orderBook;

    @BeforeEach
    public void setUp() {
        orderBook = new OrderBook();
    }

    @Test
    public void testUpdateBidAddsBid() {
        // Add a bid to the order book
        orderBook.updateBid(5000.0, 1.0);
        NavigableMap<Double, Double> bids = orderBook.getBids();

        // Assert that the bid has been added correctly
        assertEquals(1, bids.size());
        assertEquals(1.0, bids.get(5000.0));
        assertEquals(5000.0, orderBook.getHighestBid());
    }

    @Test
    public void testUpdateAskAddsAsk() {
        // Add an ask to the order book
        orderBook.updateAsk(5001.0, 2.0);
        NavigableMap<Double, Double> asks = orderBook.getAsks();

        // Assert that the ask has been added correctly
        assertEquals(1, asks.size());
        assertEquals(2.0, asks.get(5001.0));
        assertEquals(5001.0, orderBook.getLowestAsk());
    }

    @Test
    public void testRemoveBidWhenQuantityIsZero() {
        // Add and then remove a bid
        orderBook.updateBid(5000.0, 1.0);
        orderBook.updateBid(5000.0, 0.0);
        NavigableMap<Double, Double> bids = orderBook.getBids();

        // Assert that the bid has been removed
        assertTrue(bids.isEmpty());
    }

    @Test
    public void testRemoveAskWhenQuantityIsZero() {
        // Add and then remove an ask
        orderBook.updateAsk(5001.0, 2.0);
        orderBook.updateAsk(5001.0, 0.0);
        NavigableMap<Double, Double> asks = orderBook.getAsks();

        // Assert that the ask has been removed
        assertTrue(asks.isEmpty());
    }

    @Test
    public void testGetHighestBidReturnsCorrectValue() {
        // Add multiple bids and verify the highest bid is correct
        orderBook.updateBid(4999.0, 1.5);
        orderBook.updateBid(5000.0, 1.0);
        assertEquals(5000.0, orderBook.getHighestBid());
    }

    @Test
    public void testGetLowestAskReturnsCorrectValue() {
        // Add multiple asks and verify the lowest ask is correct
        orderBook.updateAsk(5002.0, 1.0);
        orderBook.updateAsk(5001.0, 2.0);
        assertEquals(5001.0, orderBook.getLowestAsk());
    }

    @Test
    public void testSanityCheckValidOrderBook() {
        // Add valid bid and ask
        orderBook.updateBid(5000.0, 1.0);
        orderBook.updateAsk(5001.0, 2.0);

        // Verify the order book is valid
        assertTrue(orderBook.isValid());
    }

    @Test
    public void testSanityCheckInvalidOrderBook() {
        // Add invalid bid and ask (bid >= ask)
        orderBook.updateBid(5001.0, 1.0);
        orderBook.updateAsk(5000.0, 2.0);

        // Verify the order book is invalid
        assertFalse(orderBook.isValid());
    }

    @Test
    public void testSanityCheckEmptyOrderBook() {
        // Empty order book should be invalid
        assertFalse(orderBook.isValid());

        // Add only bids
        orderBook.updateBid(5000.0, 1.0);
        assertFalse(orderBook.isValid());

        // Add only asks
        orderBook = new OrderBook();  // Reset the order book
        orderBook.updateAsk(5001.0, 2.0);
        assertFalse(orderBook.isValid());
    }
}
