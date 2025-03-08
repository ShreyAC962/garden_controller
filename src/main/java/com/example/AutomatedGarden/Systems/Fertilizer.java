package com.example.AutomatedGarden.Systems;

public class Fertilizer {
    private String name;
    private int effectiveness;
    private int frequency;
    private int stock=10;


    public Fertilizer(String name, int effectiveness, int frequency) {
        this.name = name;
        this.effectiveness = effectiveness;
        this.frequency = frequency;
    }

    public String getName() {
        return name;
    }

    public int getEffectiveness() {
        return effectiveness;
    }

    public int getFrequency() {
        return frequency;
    }

    // Reduce the stock by a given amount
    public void reduceStock(int amount) {
        if (stock >= amount) {
            stock -= amount;
        } else {
            throw new IllegalStateException("Not enough stock for " + name);
        }
    }

    // Increase the stock by a given amount
    public void increaseStock(int amount) {
        stock += amount;
    }

    // Check if the fertilizer is in stock
    public boolean isInStock() {
        return stock > 0;
    }
}

