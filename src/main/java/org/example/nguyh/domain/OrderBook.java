package org.example.nguyh.domain;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.TreeMap;

public class OrderBook {

    private TreeMap<Double, Double> bids = new TreeMap<>();
    private TreeMap<Double, Double> asks = new TreeMap<>();
    private ObjectMapper objectMapper = new ObjectMapper();

    public void updateBid(double price, double volume) {
        if (volume == 0) {
            bids.remove(price);
        } else {
            bids.put(price, volume);
        }
    }

    public void updateAsk(double price, double volume) {
        if (volume == 0) {
            asks.remove(price);
        } else {
            asks.put(price, volume);
        }
    }

    public double getHighestBid() {
        return bids.isEmpty() ? 0.0 : bids.lastKey();
    }

    public double getLowestAsk() {
        return asks.isEmpty() ? Double.MAX_VALUE : asks.firstKey();
    }

    public int getTickCount() {
        return bids.size() + asks.size();  // Example of tick count
    }

    public TreeMap<Double, Double> getBids() {
        return bids;
    }

    public void setBids(TreeMap<Double, Double> bids) {
        this.bids = bids;
    }

    public TreeMap<Double, Double> getAsks() {
        return asks;
    }

    public void setAsks(TreeMap<Double, Double> asks) {
        this.asks = asks;
    }

    /**
     * Parses and updates the order book based on the incoming WebSocket message.
     *
     * @param message the WebSocket message in JSON format
     */
    public void updateFromMessage(String message) {
        try {
            // Parse the JSON message
            JsonNode rootNode = objectMapper.readTree(message);

            // Extract the "b" and "a" arrays (bids and asks)
            JsonNode bidNode = rootNode.get(1).get("b");
            JsonNode askNode = rootNode.get(1).get("a");

            // Update bids
            if (bidNode != null) {
                for (JsonNode bid : bidNode) {
                    double price = Double.parseDouble(bid.get(0).asText());
                    double volume = Double.parseDouble(bid.get(1).asText());
                    updateBid(price, volume);
                }
            }

            // Update asks
            if (askNode != null) {
                for (JsonNode ask : askNode) {
                    double price = Double.parseDouble(ask.get(0).asText());
                    double volume = Double.parseDouble(ask.get(1).asText());
                    updateAsk(price, volume);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Failed to parse and update order book: " + message);
        }
    }
}

