package com.example.AutomatedGarden.Model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public abstract class Plant {

    private final String name;
    private final IntegerProperty daysToLive;
    private final int maxLifespan;
    private final int waterRequirement;
    private final int row;
    private final int col;
    private int pestAttacks = 0;
    private boolean isDead = false;
    private int daysSinceLastFertilized = 0;
    private int currentWater;
    private int fertilizingFrequency;


    public Plant(String name, int maxLifespan, int waterRequirement, int row, int col, int fertilizingFrequency) {
        if (maxLifespan <= 0) {
            throw new IllegalArgumentException("maxLifespan must be a positive number");
        }
        this.name = name;
        this.maxLifespan = maxLifespan;
        this.daysToLive = new SimpleIntegerProperty(maxLifespan);
        this.waterRequirement = waterRequirement;
        this.row = row;
        this.col = col;
        this.fertilizingFrequency = fertilizingFrequency;
    }


    public String getName() {
        return name;
    }

    public IntegerProperty getDaysToLiveProperty() {
        return daysToLive;
    }

    public int getDaysToLive() {
        return daysToLive.get();
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public boolean isDead() {
        return isDead;
    }

    public int getWaterRequirement() {
        return waterRequirement;
    }

    public int getPestAttacks() {
        return pestAttacks;
    }

    public int getDaysSinceLastFertilized() {
        return daysSinceLastFertilized;
    }

    public int getCurrentWater() {
        return this.currentWater;
    }

    // ------ Mutator Methods ------
    public void incrementDaysSinceLastFertilized() {
        daysSinceLastFertilized++;
    }

    public void resetDaysSinceLastFertilized() {
        daysSinceLastFertilized = 0;
    }

    public void setCurrentWater(int water) {
        this.currentWater = water;
    }

    public void decrementDaysToLive() {
        if (!isDead && daysToLive.get() > 0) {
            daysToLive.set(daysToLive.get() - 1); // Decrement the property value
        }
        if (daysToLive.get() <= 0) {
            isDead = true;
        }
    }

    public void boostGrowth() {
        if (!isDead) {
            daysToLive.set(Math.min(daysToLive.get() + 10, maxLifespan));  // Optionally increase lifespan
        }
    }

    public void incrementPestAttacks() {
        if (isDead) {
            return;
        }
        pestAttacks++;
        if (pestAttacks == 7) { // Reduce lifespan by 1 after 7 pest attacks
            daysToLive.set(daysToLive.get() - 1);
            if (daysToLive.get() <= 0) {
                isDead = true;
            }
        }
        if (pestAttacks >= 12) {  // Plant dies after 12 pest attacks
            isDead = true;
        }
    }

    public void reducePestAttacks(int amount) {
        pestAttacks = Math.max(pestAttacks - amount, 0);  // Reduces pest attacks by the given amount
    }

    public void adjustLifespanForWeather(String weather) {
        if (!isDead) {
            // No additional days for sunny weather
            if (weather.equals("Rainy")) {
                // Rainy days are beneficial, increase lifespan by 1
                daysToLive.set(Math.min(daysToLive.get() + 1, maxLifespan));
            }
            if (weather.equals("Cloudy")) {
                // Cloudy days are harmful, decrease lifespan by 2
                daysToLive.set(daysToLive.get() - 2);
                if (daysToLive.get() <= 0) {
                    isDead = true;
                }
            }
            if (weather.equals("Sunny")) {
                // Sunny days are beneficial, increase lifespan by 1
                daysToLive.set(Math.min(daysToLive.get() + 1, maxLifespan));
            }
        }
    }

    // Getter and setter methods for the fertilizing frequency
    public int getFertilizingFrequency() {
        return this.fertilizingFrequency;
    }

    public void setFertilizingFrequency(int fertilizingFrequency) {
        this.fertilizingFrequency = fertilizingFrequency;
    }

    // ------ Abstract Methods ------
    public abstract int getCount();

    public abstract void decrementCount();

    // ------ Other Methods ------
    public void water(int amount) {
        if (!isDead) {
            currentWater += amount;
            if (currentWater >= waterRequirement) {
                daysToLive.set(Math.min(daysToLive.get() + 1, maxLifespan));// Increase lifespan by 1 day if properly wateredd
            } else {
                daysToLive.set(daysToLive.get() - 1);
            }
        }
    }

    public void heat() {
        if (!isDead) {
            daysToLive.set(daysToLive.get() + 1); // Increase lifespan by 1 day when heated
        }
    }

    public void cool() {
        if (!isDead) {
            daysToLive.set(daysToLive.get() + 1); // Increase lifespan by 1 day when cooled
        }
    }
}
