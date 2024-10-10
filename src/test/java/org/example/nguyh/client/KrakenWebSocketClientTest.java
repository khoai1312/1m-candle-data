package org.example.nguyh.client;

import org.example.nguyh.service.OrderBookProcessor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.websocket.Session;

import static org.mockito.Mockito.*;

public class KrakenWebSocketClientTest {

    private KrakenWebSocketClient webSocketClient;
    private OrderBookProcessor orderBookProcessor;

    @BeforeEach
    public void setup() {
        orderBookProcessor = Mockito.mock(OrderBookProcessor.class);
        webSocketClient = new KrakenWebSocketClient();
        webSocketClient.setOrderBookProcessor(orderBookProcessor);
    }

    @Test
    public void testOnMessageReceivesValidMessage() {
        String message = "{\"b\": [[\"5000.0\", \"1.0\"]], \"a\": [[\"5000.5\", \"0.5\"]]}";
        webSocketClient.onMessage(message);

        verify(orderBookProcessor, times(1)).processOrderBookUpdate(message);
    }

    // Edge case: Invalid JSON message
    @Test
    public void testOnMessageWithInvalidJson() {
        String invalidMessage = "Invalid Message";
        webSocketClient.onMessage(invalidMessage);

        // Ensure the processor is not called for invalid JSON
        verify(orderBookProcessor, never()).processOrderBookUpdate(invalidMessage);
    }

    // Edge case: Null message
    @Test
    public void testOnMessageWithNullMessage() {
        webSocketClient.onMessage(null);
        verify(orderBookProcessor, never()).processOrderBookUpdate(null);
    }

    @Test
    public void testOnClose() {
        Session mockSession = Mockito.mock(Session.class);
        webSocketClient.onClose(mockSession, new javax.websocket.CloseReason(javax.websocket.CloseReason.CloseCodes.NORMAL_CLOSURE, "Test close"));
        // No assertions needed, just check that no exceptions are thrown
    }
}
