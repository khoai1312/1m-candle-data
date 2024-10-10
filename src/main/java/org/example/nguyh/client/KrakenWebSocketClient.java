package org.example.nguyh.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.nguyh.domain.Candle;
import org.example.nguyh.domain.OrderBook;
import org.example.nguyh.kafka.producer.CandleDataProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import java.net.URI;

@Component
@ClientEndpoint
public class KrakenWebSocketClient {

    private OrderBook orderBook = new OrderBook();
    private Candle currentCandle;
    private long nextMinuteTimestamp;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private CandleDataProducer kafkaProducer;

    // Initialize WebSocket
    public KrakenWebSocketClient() {
        this.currentCandle = new Candle(System.currentTimeMillis());
        this.nextMinuteTimestamp = System.currentTimeMillis() + 60000; // 1-minute window
    }

    @OnOpen
    public void onOpen(Session session) {
        System.out.println("Connected to Kraken WebSocket");

        // Subscribe to Kraken WebSocket for order book data
        String subscribeMessage = "{ \"event\": \"subscribe\", \"pair\": [\"XBT/USD\"], \"subscription\": { \"name\": \"book\" }}";
        session.getAsyncRemote().sendText(subscribeMessage);
    }

    @OnMessage
    public void onMessage(String message) {
        System.out.println("Received message: " + message);

        try {
            // Parse the WebSocket message using Jackson
            JsonNode rootNode = objectMapper.readTree(message);

            // Only process messages that are arrays and contain at least 2 elements
            if (rootNode.isArray() && rootNode.size() > 1) {
                JsonNode bookData = rootNode.get(1); // Get the actual book data (bids and asks)
                processBookData(bookData);
            }

            // Log the candle every 1 minute, send to Kafka, and reset the candle for the next minute
            if (System.currentTimeMillis() >= nextMinuteTimestamp) {
                kafkaProducer.sendCandle(currentCandle);  // Send candle data to Kafka

                currentCandle = new Candle(nextMinuteTimestamp);
                nextMinuteTimestamp += 60000; // Set timestamp for the next minute
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void processBookData(JsonNode bookData) {
        if (bookData.has("b")) { // Process bids
            JsonNode bids = bookData.get("b");
            for (JsonNode bid : bids) {
                double price = bid.get(0).asDouble();
                double quantity = bid.get(1).asDouble();
                orderBook.updateBid(price, quantity);
            }
        }

        if (bookData.has("a")) { // Process asks
            JsonNode asks = bookData.get("a");
            for (JsonNode ask : asks) {
                double price = ask.get(0).asDouble();
                double quantity = ask.get(1).asDouble();
                orderBook.updateAsk(price, quantity);
            }
        }

        // After updating, ensure that the order book is valid
        if (!orderBook.isValid()) {
            System.out.println("Sanity check failed: highest bid >= lowest ask");
        }
    }

    public OrderBook getOrderBook() {
        return orderBook;
    }

    @OnClose
    public void onClose(Session session, CloseReason closeReason) {
        System.out.println("Session closed: " + closeReason);
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        System.out.println("Error: " + throwable.getMessage());
    }

    public long getNextMinuteTimestamp() {
        return nextMinuteTimestamp;
    }

    public void setNextMinuteTimestamp(long nextMinuteTimestamp) {
        this.nextMinuteTimestamp = nextMinuteTimestamp;
    }

    public void setKafkaProducer(CandleDataProducer kafkaProducer) {
        this.kafkaProducer = kafkaProducer;
    }

    public static void main(String[] args) {
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        try {
            URI uri = new URI("wss://ws.kraken.com");
            container.connectToServer(KrakenWebSocketClient.class, uri);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}