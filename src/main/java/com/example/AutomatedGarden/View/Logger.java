package com.example.AutomatedGarden.View;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Logger {
    private final List<String> dayLogEntries;
    private final List<String> wateringLogEntries;
    private final List<String> heatingLogEntries;
    private final List<String> insectLogEntries;
    private final List<String> cleanerLogEntries;
    private final List<String> fertilizerLogEntries;
    private static final String LOG_FILE_NAME = "garden_logs.txt";

    public Logger() {
        dayLogEntries = new ArrayList<>();
        wateringLogEntries = new ArrayList<>();
        heatingLogEntries = new ArrayList<>();
        insectLogEntries = new ArrayList<>();
        cleanerLogEntries = new ArrayList<>();
        fertilizerLogEntries = new ArrayList<>();
    }

    public void addDayLogEntry(String entry) {
        dayLogEntries.add(entry);
        saveLogToFile("Day Log: " + entry);
    }

    public void addWateringLogEntry(String entry) {
        wateringLogEntries.add(entry);
        saveLogToFile("Watering Log: " + entry);
    }

    public void addHeatingLogEntry(String entry) {
        heatingLogEntries.add(entry);
        saveLogToFile("Heating Log: " + entry);
    }

    public void addInsectLogEntry(String entry) {
        insectLogEntries.add(entry);
        saveLogToFile("Insect Log: " + entry);
    }

    public void addCleanerLogEntry(String entry) {
        cleanerLogEntries.add(entry);
        saveLogToFile("Cleaner Log: " + entry);
    }

    public void addFertilizerLogEntry(String entry) {
        fertilizerLogEntries.add(entry);
        saveLogToFile("Fertilizer Log: " + entry);
    }

    public List<String> getDayLogEntries() {
        return dayLogEntries;
    }

    public List<String> getWateringLogEntries() {
        return wateringLogEntries;
    }

    public List<String> getHeatingLogEntries() {
        return heatingLogEntries;
    }

    public List<String> getInsectLogEntries() {
        return insectLogEntries;
    }

    public List<String> getFertilizerLogEntries() {
        return fertilizerLogEntries;
    }

    public List<String> getCleanerLogEntries() {
        return cleanerLogEntries;
    }

    private void saveLogToFile(String entry) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(LOG_FILE_NAME, true))) {
            writer.write(entry);
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Error writing to log file: " + e.getMessage());
        }
    }
}

