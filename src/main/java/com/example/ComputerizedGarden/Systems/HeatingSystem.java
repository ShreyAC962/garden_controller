package com.example.ComputerizedGarden.Systems;

import com.example.ComputerizedGarden.Model.Plant;

import java.util.List;

public class HeatingSystem {
    private int currentTemperature;

    public HeatingSystem() {
        this.currentTemperature = 20; // Default temperature
    }

    public void increaseTemperature(List<Plant> plants) {
        currentTemperature += 5;
        for (Plant plant : plants) {
            if (!plant.isDead()) {
                plant.boostGrowth(); // Increase plant lifespan significantly
            }
        }
    }

    public void decreaseTemperature(List<Plant> plants) {
        currentTemperature -= 5;
        for (Plant plant : plants) {
            if (!plant.isDead()) {
                plant.getDaysToLiveProperty().set(plant.getDaysToLive() - 1); // Slightly decrease lifespan
            }
        }
    }
}


