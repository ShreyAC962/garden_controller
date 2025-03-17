package com.example.ComputerizedGarden.Controllers;
import com.example.ComputerizedGarden.Model.Plant;
import com.example.ComputerizedGarden.Systems.Sensor;
import com.example.ComputerizedGarden.Model.Insect;
import javafx.scene.layout.GridPane;
import com.example.ComputerizedGarden.View.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ControllerForGarden {
    private final List<Plant> plants = new ArrayList<>();
    private final List<Insect> insects = new ArrayList<>();
    private final ControllerForPest controllerForPest;
    private final ControllerForSensor controllerForSensor;
    private final Logger logger;

    private int dayCount;
    private static final String[] WEATHER_TYPES = {"Sunny", "Rainy", "Cold", "Snowy", "Cloudy", "Windy"};
    private ControllerForFertilizer controllerForFertilizer;

    private final GridPane gardenGrid;

    public void simulateDay() {
        dayCount++;
        logger.addDayLogEntry("Day " + dayCount + ": simulation started.");

        // Determine the current weather
        String weather = getCurrentWeather();
        logger.addDayLogEntry("Weather: " + weather);

        // Clear dead plants
        plants.removeIf(Plant::isDead);

        // Update plant conditions
        for (Plant plant : plants) {
            if (!plant.isDead()) {
                plant.decrementDaysToLive();
                plant.setCurrentWater(plant.getCurrentWater() - 5);
            }
        }

        // Manage pests
        insects.clear();
        controllerForPest.managePests(plants, insects, logger, dayCount);

        // Log watering activity
        logger.addWateringLogEntry("Day " + dayCount + ": Plants watered.");

        // Create a sensor with the current weather
        Sensor currentSensor = new Sensor(weather, 10); // Example: fixed temperature 10°C

        // Trigger animations and weather-specific effects
        controllerForSensor.generateAnimation(currentSensor, gardenGrid, plants);

        // Additional actions based on weather
        switch (weather) {
            case "Cold", "Snowy" -> logger.addHeatingLogEntry("Day " + dayCount + ": Heating system activated.");
            case "Cloudy" -> logger.addDayLogEntry("Day " + dayCount + ": Cloudy weather observed. No special actions taken.");
            case "Windy" -> logger.addDayLogEntry("Day " + dayCount + ": Wind protection shield activated.");
            case "Sunny" -> logger.addDayLogEntry("Day " + dayCount + ": Sprinklers activated for dry sunny weather.");
            case "Rainy" -> logger.addDayLogEntry("Day " + dayCount + ": Plants received natural watering from rain.");
        }

        logger.addDayLogEntry("Day " + dayCount + ": simulation completed.");
    }

    public ControllerForGarden(ControllerForPest controllerForPest, ControllerForFertilizer fertilizer, ControllerForSensor controllerForSensor, GridPane gardenGrid) {
        this.controllerForPest = controllerForPest;
        this.controllerForFertilizer = fertilizer;
        this.controllerForSensor = controllerForSensor;
        this.gardenGrid = gardenGrid; // Add GridPane reference

        logger = new Logger();
        dayCount = 0;
    }

    public void addPlant(Plant plant) {
        plants.add(plant);
        logger.addDayLogEntry("Added plant: " + plant.getName() + " at grid (" + plant.getRow() + "," + plant.getCol() + ")");
    }

    public List<Plant> getPlants() {
        return plants;
    }

    public List<Insect> getInsects() {
        return insects;
    }

    public Logger getLogger() {
        return logger;
    }

    public int getDay() {
        return dayCount;
    }

    public String getCurrentWeather() {
        if (WEATHER_TYPES == null || WEATHER_TYPES.length == 0) {
            System.err.println("Error: WEATHER_TYPES is not configured. Defaulting to 'Sunny'.");
            return "Sunny";
        }
        String weather = WEATHER_TYPES[new Random().nextInt(WEATHER_TYPES.length)];
        if (weather == null) {
            System.err.println("Error: Randomly selected weather is null. Defaulting to 'Sunny'.");
            return "Sunny";
        }
        return weather;
    }


    public void setDay(int i) {
        this.dayCount = 0;
    }
}
