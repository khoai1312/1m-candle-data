package org.example.nguyh.domain;

import java.util.NavigableMap;
import java.util.TreeMap;

public class OrderBook {
    private TreeMap<Double, Double> bids = new TreeMap<>();
    private TreeMap<Double, Double> asks = new TreeMap<>();

    public void updateBid(double price, double quantity) {
        if (quantity == 0) {
            bids.remove(price);
        } else {
            bids.put(price, quantity);
        }
    }

    public void updateAsk(double price, double quantity) {
        if (quantity == 0) {
            asks.remove(price);
        } else {
            asks.put(price, quantity);
        }
    }

    public double getHighestBid() {
        return bids.isEmpty() ? 0 : bids.lastKey();
    }

    public double getLowestAsk() {
        return asks.isEmpty() ? 0 : asks.firstKey();
    }

    public boolean isValid() {
        return !bids.isEmpty() && !asks.isEmpty() && getHighestBid() < getLowestAsk();
    }

    public NavigableMap<Double, Double> getBids() {
        return bids;
    }

    public NavigableMap<Double, Double> getAsks() {
        return asks;
    }
}
