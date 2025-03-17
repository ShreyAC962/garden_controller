package com.example.ComputerizedGarden.View;

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
    private static final String LOG_FILE_NAME = "OOAD_ComputarizedGarden_Logs.txt";

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
        saveLog("Day Log: " + entry);
    }

    public void addWateringLogEntry(String entry) {
        wateringLogEntries.add(entry);
        saveLog("Watering Log: " + entry);
    }

    public void addHeatingLogEntry(String entry) {
        heatingLogEntries.add(entry);
        saveLog("Heating Log: " + entry);
    }

    public void addInsectLogEntry(String entry) {
        insectLogEntries.add(entry);
        saveLog("Insect Log: " + entry);
    }

    public void addCleanerLogEntry(String entry) {
        cleanerLogEntries.add(entry);
        saveLog("Cleaner Log: " + entry);
    }

    public void addFertilizerLogEntry(String entry) {
        fertilizerLogEntries.add(entry);
        saveLog("Fertilizer Log: " + entry);
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

    private void saveLog(String entry) {
        System.out.println(entry);  // ✅ Print log to console

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(LOG_FILE_NAME, true))) {
            writer.write(entry);
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Error writing to log file: " + e.getMessage());
        }
    }
}
