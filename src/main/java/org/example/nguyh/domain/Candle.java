package org.example.nguyh.domain;

public class Candle {
    private double open;
    private double high;
    private double low;
    private double close;
    private int ticks;

    public Candle(long timestamp) {
        this.ticks = 0; // Initialize the tick count
    }

    // Adds a new tick to the candle and updates the prices accordingly
    public void addTick(double price, double volume) {
        if (ticks == 0) {
            // First tick: set open, high, low, and close to the first price
            this.open = price;
            this.high = price;
            this.low = price;
            this.close = price;
        } else {
            // Update high and low based on the new price
            this.high = Math.max(this.high, price);
            this.low = Math.min(this.low, price);
            // Always set close to the last price
            this.close = price;
        }
        this.ticks++;
    }

    // Getters for candle properties
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

    public void setOpen(double open) {
        this.open = open;
    }

    public void setHigh(double high) {
        this.high = high;
    }

    public void setLow(double low) {
        this.low = low;
    }

    public void setClose(double close) {
        this.close = close;
    }

    public void setTicks(int ticks) {
        this.ticks = ticks;
    }
}
