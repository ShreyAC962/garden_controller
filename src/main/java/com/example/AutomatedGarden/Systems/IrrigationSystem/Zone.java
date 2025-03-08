package com.example.AutomatedGarden.Systems.IrrigationSystem;

import com.example.AutomatedGarden.Model.Plant;

import java.util.ArrayList;
import java.util.HashMap;

public class Zone {
    // ------ Instance Variables ------
    private final int id;
    private ArrayList<int[]> tiles;
    private ArrayList<Plant> plants = new ArrayList<>();
    private String type;
    private int interval;
    private int lastWatered;
    private Boolean isEnabled = true;
    private static final HashMap<String, Integer> irrigationType = new HashMap<>();

    static {
        irrigationType.put("Drip", 5);
        irrigationType.put("Sprinkler", 10);
    }

    // ------ Constructors ------
    public Zone(int id, ArrayList<int[]> tiles, String type, int interval) {
        this.id = id;
        this.tiles = tiles;
        this.type = type;
        this.interval = interval;
    }

    // ------ Accessor Methods ------
    public ArrayList<int[]> getTiles() {
        return tiles;
    }

    public String getType() {
        return type;
    }

    public int getInterval() {
        return interval;
    }

    public ArrayList<Plant> getPlants() {
        return plants;
    }

    public int getId() {
        return id;
    }

    public boolean getEnabled() {
        return isEnabled;
    }

    public HashMap<String, Integer> getIrrigationTypes() {
        return irrigationType;
    }

    // ------ Mutator Methods ------
    public void setType(String type) {
        this.type = type;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public void addPlant(Plant plant) {
        plants.add(plant);
    }

    public void deletePlant(Plant plant) {
        plants.remove(plant);
    }

    public void setEnabled(Boolean enabled) {
        this.isEnabled = enabled;
    }

    // ------ Other Methods ------
    public void water() {
        lastWatered += 1;
        if (lastWatered == interval) {
            lastWatered = 0;
            for (Plant plant : plants) {
                plant.water(irrigationType.get(type));
            }
        }
    }
}
