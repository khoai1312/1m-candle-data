package org.example.nguyh.domain;

import java.util.ArrayList;
import java.util.List;

public class Candle {
    private long timestamp;
    private double open;
    private double high;
    private double low;
    private double close;
    private int ticks;

    private List<Double> midPrices = new ArrayList<>();

    public Candle(long timestamp) {
        this.timestamp = timestamp;
    }

    public void addTick(double highestBid, double lowestAsk) {
        double midPrice = (highestBid + lowestAsk) / 2;
        if (midPrices.isEmpty()) {
            open = midPrice;
            high = midPrice;
            low = midPrice;
        } else {
            if (midPrice > high) high = midPrice;
            if (midPrice < low) low = midPrice;
        }
        close = midPrice;
        midPrices.add(midPrice);
        ticks++;
    }

    public void logCandle() {
        System.out.println("Timestamp: " + timestamp);
        System.out.println("Open: " + open + ", High: " + high + ", Low: " + low + ", Close: " + close);
        System.out.println("Ticks: " + ticks);
    }

    // Getter methods for the test cases
    public double getOpen() {
        return open;
    }

    public double getHigh() {
        return high;
    }

    public double getLow() {
        return low;
    }

    public double getClose() {
        return close;
    }

    public int getTicks() {
        return ticks;
    }
}
