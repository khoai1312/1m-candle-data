package org.example.nguyh.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CandleTest {

    private Candle candle;

    @BeforeEach
    public void setUp() {
        candle = new Candle(System.currentTimeMillis());
    }

    @Test
    public void testAddFirstTick() {
        // Add the first tick
        candle.addTick(5000.5, 1.0);

        // Assert that all values (open, high, low, close) are set to the first price
        assertEquals(5000.5, candle.getOpen());
        assertEquals(5000.5, candle.getHigh());
        assertEquals(5000.5, candle.getLow());
        assertEquals(5000.5, candle.getClose());
        assertEquals(1, candle.getTicks());
    }

    @Test
    public void testAddMultipleTicks() {
        // Add the first tick
        candle.addTick(5000.5, 1.0);
        // Add a second tick with a higher price
        candle.addTick(5001.0, 1.0);
        // Add a third tick with a lower price
        candle.addTick(4999.0, 1.0);

        // Verify open, high, low, and close values after multiple ticks
        assertEquals(5000.5, candle.getOpen());  // Open should stay the same
        assertEquals(5001.0, candle.getHigh());  // High should be the highest price
        assertEquals(4999.0, candle.getLow());   // Low should be the lowest price
        assertEquals(4999.0, candle.getClose()); // Close should be the last price
        assertEquals(3, candle.getTicks());      // There should be 3 ticks
    }

    @Test
    public void testHighUpdatesCorrectly() {
        // Add ticks to test high updates
        candle.addTick(5000.0, 1.0);
        candle.addTick(5002.0, 1.0);  // High should update to 5002.0
        candle.addTick(5001.5, 1.0);  // High should stay at 5002.0

        assertEquals(5002.0, candle.getHigh());
    }

    @Test
    public void testLowUpdatesCorrectly() {
        // Add ticks to test low updates
        candle.addTick(5000.0, 1.0);
        candle.addTick(4998.0, 1.0);  // Low should update to 4998.0
        candle.addTick(4999.0, 1.0);  // Low should stay at 4998.0

        assertEquals(4998.0, candle.getLow());
    }

    @Test
    public void testCloseUpdatesOnEachTick() {
        // Add ticks to test close updates
        candle.addTick(5000.0, 1.0);
        assertEquals(5000.0, candle.getClose());  // Close should be 5000.0

        candle.addTick(5001.0, 1.0);
        assertEquals(5001.0, candle.getClose());  // Close should update to 5001.0

        candle.addTick(5002.0, 1.0);
        assertEquals(5002.0, candle.getClose());  // Close should update to 5002.0
    }

    @Test
    public void testSingleTickCandle() {
        // Test the behavior of a candle with a single tick
        candle.addTick(5000.0, 1.0);

        assertEquals(5000.0, candle.getOpen());
        assertEquals(5000.0, candle.getHigh());
        assertEquals(5000.0, candle.getLow());
        assertEquals(5000.0, candle.getClose());
        assertEquals(1, candle.getTicks());
    }

    @Test
    public void testMultipleTicksWithSamePrice() {
        // Add multiple ticks with the same price
        candle.addTick(5000.0, 1.0);
        candle.addTick(5000.0, 1.0);
        candle.addTick(5000.0, 1.0);

        // Verify all values remain the same
        assertEquals(5000.0, candle.getOpen());
        assertEquals(5000.0, candle.getHigh());
        assertEquals(5000.0, candle.getLow());
        assertEquals(5000.0, candle.getClose());
        assertEquals(3, candle.getTicks());
    }
}
