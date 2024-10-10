package org.example.nguyh.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class OrderBookTest {

    @Test
    public void testUpdateBid() {
        OrderBook orderBook = new OrderBook();
        orderBook.updateBid(5000.0, 1.0);
        assertEquals(5000.0, orderBook.getHighestBid(), 0.001);
        assertEquals(1.0, orderBook.getBids().get(5000.0)); // Use getBids() method
    }

    @Test
    public void testUpdateAsk() {
        OrderBook orderBook = new OrderBook();
        orderBook.updateAsk(5001.0, 1.5);
        assertEquals(5001.0, orderBook.getLowestAsk(), 0.001);
        assertEquals(1.5, orderBook.getAsks().get(5001.0)); // Use getAsks() method
    }

    @Test
    public void testRemoveBid() {
        OrderBook orderBook = new OrderBook();
        orderBook.updateBid(5000.0, 1.0);
        orderBook.updateBid(5000.0, 0.0); // Removing bid
        assertTrue(orderBook.getBids().isEmpty()); // Use getBids() method
    }

    @Test
    public void testRemoveAsk() {
        OrderBook orderBook = new OrderBook();
        orderBook.updateAsk(5001.0, 1.5);
        orderBook.updateAsk(5001.0, 0.0); // Removing ask
        assertTrue(orderBook.getAsks().isEmpty()); // Use getAsks() method
    }

    @Test
    public void testSanityCheck() {
        OrderBook orderBook = new OrderBook();
        orderBook.updateBid(5000.0, 1.0);
        orderBook.updateAsk(5001.0, 1.5);
        assertTrue(orderBook.isValid());

        // Invalid case: highest bid >= lowest ask
        orderBook.updateAsk(4999.0, 1.5);
        assertFalse(orderBook.isValid());
    }
}