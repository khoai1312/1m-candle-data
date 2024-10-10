package org.example.nguyh.client;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class KrakenWebSocketClientTest {

    @Test
    public void testOnMessageValidBookUpdate() {
        KrakenWebSocketClient client = new KrakenWebSocketClient();

        // Simulate a valid WebSocket message with bid/ask update
        String message = "[1234, {\"b\": [[\"5000.0\", \"1.0\"]], \"a\": [[\"5001.0\", \"2.0\"]]}, \"book-25\", \"XBT/USD\"]";
        client.onMessage(message);

        // Validate that the order book is updated correctly
        assertEquals(5000.0, client.getOrderBook().getHighestBid()); // Access using getter
        assertEquals(5001.0, client.getOrderBook().getLowestAsk());  // Access using getter
    }

    @Test
    public void testOnMessageInvalidBookUpdate() {
        KrakenWebSocketClient client = new KrakenWebSocketClient();

        // Simulate an invalid WebSocket message where highest bid >= lowest ask
        String message = "[1234, {\"b\": [[\"5001.0\", \"1.0\"]], \"a\": [[\"5000.0\", \"2.0\"]]}, \"book-25\", \"XBT/USD\"]";
        client.onMessage(message);

        // Validate that the order book fails the sanity check
        assertFalse(client.getOrderBook().isValid()); // Access using getter
    }
}
