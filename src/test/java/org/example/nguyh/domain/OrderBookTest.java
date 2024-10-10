package org.example.nguyh.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.*;

public class OrderBookTest {

    private OrderBook orderBook;

    @BeforeEach
    public void setup() {
        orderBook = new OrderBook();
    }

    // Test for updating bids with valid data
    @Test
    public void testUpdateBid() {
        orderBook.updateBid(5000.0, 1.0);  // Add a bid
        orderBook.updateBid(5001.0, 2.0);  // Add another bid

        TreeMap<Double, Double> bids = orderBook.getBids();

        assertEquals(2, bids.size());  // Check if two bids were added
        assertEquals(1.0, bids.get(5000.0));  // Check the volume of the bid at 5000.0
        assertEquals(2.0, bids.get(5001.0));  // Check the volume of the bid at 5001.0
    }

    // Test for updating asks with valid data
    @Test
    public void testUpdateAsk() {
        orderBook.updateAsk(5002.0, 1.0);  // Add an ask
        orderBook.updateAsk(5003.0, 2.0);  // Add another ask

        TreeMap<Double, Double> asks = orderBook.getAsks();

        assertEquals(2, asks.size());  // Check if two asks were added
        assertEquals(1.0, asks.get(5002.0));  // Check the volume of the ask at 5002.0
        assertEquals(2.0, asks.get(5003.0));  // Check the volume of the ask at 5003.0
    }

    // Test removing a bid when volume is set to zero
    @Test
    public void testRemoveBid() {
        orderBook.updateBid(5000.0, 1.0);  // Add a bid
        orderBook.updateBid(5000.0, 0.0);  // Remove the bid by setting volume to 0

        TreeMap<Double, Double> bids = orderBook.getBids();

        assertEquals(0, bids.size());  // The bids map should be empty
        assertNull(bids.get(5000.0));  // No bid should exist at 5000.0
    }

    // Test removing an ask when volume is set to zero
    @Test
    public void testRemoveAsk() {
        orderBook.updateAsk(5002.0, 1.0);  // Add an ask
        orderBook.updateAsk(5002.0, 0.0);  // Remove the ask by setting volume to 0

        TreeMap<Double, Double> asks = orderBook.getAsks();

        assertEquals(0, asks.size());  // The asks map should be empty
        assertNull(asks.get(5002.0));  // No ask should exist at 5002.0
    }

    // Test for getting the highest bid
    @Test
    public void testGetHighestBid() {
        orderBook.updateBid(5000.0, 1.0);
        orderBook.updateBid(5001.0, 2.0);

        assertEquals(5001.0, orderBook.getHighestBid());  // Should return the highest bid
    }

    // Test for getting the lowest ask
    @Test
    public void testGetLowestAsk() {
        orderBook.updateAsk(5002.0, 1.0);
        orderBook.updateAsk(5003.0, 2.0);

        assertEquals(5002.0, orderBook.getLowestAsk());  // Should return the lowest ask
    }

    // Edge case: Test for highest bid when no bids exist
    @Test
    public void testGetHighestBidWhenNoBids() {
        assertEquals(0.0, orderBook.getHighestBid());  // Should return 0.0 when no bids exist
    }

    // Edge case: Test for lowest ask when no asks exist
    @Test
    public void testGetLowestAskWhenNoAsks() {
        assertEquals(Double.MAX_VALUE, orderBook.getLowestAsk());  // Should return Double.MAX_VALUE when no asks exist
    }

    // Test for updating bids and asks at the same time
    @Test
    public void testUpdateBidAndAsk() {
        orderBook.updateBid(5000.0, 1.0);
        orderBook.updateAsk(5002.0, 1.0);

        assertEquals(5000.0, orderBook.getHighestBid());  // Highest bid should be 5000.0
        assertEquals(5002.0, orderBook.getLowestAsk());   // Lowest ask should be 5002.0
    }

    // Edge case: Test removing a non-existing bid
    @Test
    public void testRemoveNonExistingBid() {
        orderBook.updateBid(5000.0, 0.0);  // Attempt to remove a non-existing bid
        assertEquals(0, orderBook.getBids().size());  // Bids map should still be empty
    }

    // Edge case: Test removing a non-existing ask
    @Test
    public void testRemoveNonExistingAsk() {
        orderBook.updateAsk(5002.0, 0.0);  // Attempt to remove a non-existing ask
        assertEquals(0, orderBook.getAsks().size());  // Asks map should still be empty
    }
}
