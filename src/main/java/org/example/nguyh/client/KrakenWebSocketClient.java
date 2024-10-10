package org.example.nguyh.client;

import org.example.nguyh.service.OrderBookProcessor;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.util.logging.Logger;


@ClientEndpoint
public class KrakenWebSocketClient {

    private static final Logger LOGGER = Logger.getLogger(KrakenWebSocketClient.class.getName());
    private Session session;
    private OrderBookProcessor orderBookProcessor;

    public void connect(URI uri) throws IOException, DeploymentException {
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        container.connectToServer(this, uri);
        LOGGER.info("Connected to Kraken WebSocket API.");
    }

    public void setOrderBookProcessor(OrderBookProcessor processor) {
        this.orderBookProcessor = processor;
    }

    @OnMessage
    public void onMessage(String message) {
        LOGGER.info("Received message: " + message);
        if (orderBookProcessor != null) {
            orderBookProcessor.processOrderBookUpdate(message);
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {
        LOGGER.severe("WebSocket error: " + error.getMessage());
    }

    @OnClose
    public void onClose(Session session, CloseReason reason) {
        LOGGER.info("WebSocket closed. Reason: " + reason.getReasonPhrase());
    }

    public void close() throws IOException {
        if (session != null) {
            session.close();
            LOGGER.info("WebSocket connection closed.");
        }
    }
}