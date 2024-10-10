package org.example.nguyh.client;

import org.example.nguyh.domain.Candle;
import org.example.nguyh.kafka.producer.CandleDataProducer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.websocket.Session;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class KrakenWebSocketClientTest {

    @Mock
    private CandleDataProducer kafkaProducer;  // Mock Kafka producer

    @InjectMocks
    private KrakenWebSocketClient client;  // Inject mock producer into client

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);  // Initialize mocks
        client = new KrakenWebSocketClient();  // Initialize WebSocket client
        client.setKafkaProducer(kafkaProducer);  // Manually inject the Kafka producer
    }

    @Test
    public void testEmptyMessage() {
        String emptyMessage = "";

        // Call onMessage with an empty message
        client.onMessage(emptyMessage);

        // Ensure no interaction with the Kafka producer
        verify(kafkaProducer, never()).sendCandle(any(Candle.class));
    }

    @Test
    public void testMalformedJsonMessage() {
        String malformedMessage = "not_a_valid_json";

        // Call onMessage with a malformed JSON message
        client.onMessage(malformedMessage);

        // Ensure no interaction with the Kafka producer
        verify(kafkaProducer, never()).sendCandle(any(Candle.class));
    }

    @Test
    public void testMessageWithoutBidsOrAsks() {
        // Simulate a message that doesn't contain "b" (bids) or "a" (asks) fields
        String message = "[1234, {\"otherField\": []}, \"book-25\", \"XBT/USD\"]";

        // Call onMessage with this message
        client.onMessage(message);

        // Ensure no interaction with the Kafka producer
        verify(kafkaProducer, never()).sendCandle(any(Candle.class));
    }

    @Test
    public void testMessageWithEmptyBidsAndAsks() {
        // Simulate a message with empty "b" (bids) and "a" (asks) fields
        String message = "[1234, {\"b\": [], \"a\": []}, \"book-25\", \"XBT/USD\"]";

        // Call onMessage with this message
        client.onMessage(message);

        // Ensure no interaction with the Kafka producer
        verify(kafkaProducer, never()).sendCandle(any(Candle.class));
    }

    @Test
    public void testMessageWithNonNumericValues() {
        // Simulate a message where bids and asks contain non-numeric values
        String message = "[1234, {\"b\": [[\"not_a_number\", \"1.0\"]], \"a\": [[\"5001.0\", \"not_a_number\"]]}, \"book-25\", \"XBT/USD\"]";

        // Call onMessage with this message
        client.onMessage(message);

        // Ensure no interaction with the Kafka producer due to invalid data
        verify(kafkaProducer, never()).sendCandle(any(Candle.class));
    }

    @Test
    public void testMessageWithMultipleBidsAndAsks() {
        // Simulate a message with multiple bids and asks
        String message = "[1234, {\"b\": [[\"4999.0\", \"1.5\"], [\"5000.0\", \"1.0\"]], \"a\": [[\"5001.0\", \"2.0\"], [\"5002.0\", \"3.0\"]]}, \"book-25\", \"XBT/USD\"]";

        // Call onMessage with this message
        client.onMessage(message);

        // Verify the order book updates to reflect the highest bid and lowest ask
        assertEquals(5000.0, client.getOrderBook().getHighestBid());
        assertEquals(5001.0, client.getOrderBook().getLowestAsk());

        // Ensure no Kafka interaction yet (until 1 minute has passed)
        verify(kafkaProducer, never()).sendCandle(any(Candle.class));
    }

    @Test
    public void testOnMessageSendsCandleToKafkaAfterOneMinute() throws Exception {
        // Simulate receiving a valid message
        client.onMessage("[1234, {\"b\": [[\"5000.0\", \"1.0\"]], \"a\": [[\"5001.0\", \"2.0\"]]}, \"book-25\", \"XBT/USD\"]");

        // Set nextMinuteTimestamp to simulate that 1 minute has passed
        client.setNextMinuteTimestamp(System.currentTimeMillis() - 1000);  // Simulate time has passed

        // Simulate another message to trigger the candle data to be sent
        client.onMessage("[1234, {\"b\": [[\"5000.0\", \"1.0\"]], \"a\": [[\"5001.0\", \"2.0\"]]}, \"book-25\", \"XBT/USD\"]");

        // Verify the Kafka producer sends the candle data
        verify(kafkaProducer, times(1)).sendCandle(any(Candle.class));
    }

    @Test
    public void testOnError() {
        // Mock a WebSocket session
        Session session = mock(Session.class);

        // Simulate an error event
        Exception error = new Exception("Test error");
        client.onError(session, error);

        // Ensure no interaction with Kafka producer on error
        verify(kafkaProducer, never()).sendCandle(any(Candle.class));
    }
}

