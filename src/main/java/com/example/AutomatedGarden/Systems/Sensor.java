package com.example.AutomatedGarden.Systems;

public class Sensor {
    private String type; // e.g., "Rainy", "Sunny", "Cold"
    private int temperature;

    public Sensor(String type, int temperature) {
        this.type = type;
        this.temperature = temperature;
    }

    public String getType() {
        return type;
    }

    public int getTemperature() {
        return temperature;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }
}
