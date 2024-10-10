package org.example.nguyh;

import org.example.nguyh.client.KrakenWebSocketClient;
import org.example.nguyh.domain.OrderBook;
import org.example.nguyh.kafka.producer.CandleDataProducer;
import org.example.nguyh.service.OrderBookProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.websocket.DeploymentException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

@SpringBootApplication
public class MainApplication {

    @Autowired
    private CandleDataProducer candleKafkaProducer;

    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class, args);
        new MainApplication().startWebSocketClient();
    }

    public void startWebSocketClient() {
        try {
            // Create the WebSocket client
            KrakenWebSocketClient client = new KrakenWebSocketClient();

            // Set up the order book and Kafka producer
            OrderBook orderBook = new OrderBook();

            // Initialize the order book processor
            OrderBookProcessor orderBookProcessor = new OrderBookProcessor(orderBook, candleKafkaProducer);
            client.setOrderBookProcessor(orderBookProcessor);

            // Connect to Kraken WebSocket API
            URI krakenUri = new URI("wss://ws.kraken.com");
            client.connect(krakenUri);

            // Keep the client running
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                try {
                    client.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }));

        } catch (IOException | DeploymentException | URISyntaxException e) {
            e.printStackTrace();
        }
    }
}
