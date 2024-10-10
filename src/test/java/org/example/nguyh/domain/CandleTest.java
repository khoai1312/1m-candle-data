package org.example.nguyh.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CandleTest {

    @Test
    public void testAddTick() {
        Candle candle = new Candle(System.currentTimeMillis());
        candle.addTick(5000.0, 5001.0); // Mid price = 5000.5

        assertEquals(5000.5, candle.getOpen(), 0.001);
        assertEquals(5000.5, candle.getHigh(), 0.001);
        assertEquals(5000.5, candle.getLow(), 0.001);
        assertEquals(5000.5, candle.getClose(), 0.001);
    }

    @Test
    public void testMultipleTicks() {
        Candle candle = new Candle(System.currentTimeMillis());
        candle.addTick(5000.0, 5001.0); // Mid price = 5000.5
        candle.addTick(5000.5, 5001.5); // Mid price = 5001.0
        candle.addTick(4999.5, 5000.5); // Mid price = 5000.0

        assertEquals(5000.5, candle.getOpen(), 0.001);
        assertEquals(5001.0, candle.getHigh(), 0.001);
        assertEquals(5000.0, candle.getLow(), 0.001);
        assertEquals(5000.0, candle.getClose(), 0.001);
    }

    @Test
    public void testTicksCount() {
        Candle candle = new Candle(System.currentTimeMillis());
        candle.addTick(5000.0, 5001.0); // Tick 1
        candle.addTick(5000.5, 5001.5); // Tick 2
        assertEquals(2, candle.getTicks());
    }
}
