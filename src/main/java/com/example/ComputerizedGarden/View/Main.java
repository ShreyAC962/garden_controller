package com.example.ComputerizedGarden.View;

import javafx.application.Application;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) {
        String logFile = "OOAD_ComputarizedGarden_Logs.txt";
        try {
            Files.newBufferedWriter(Paths.get(logFile)).close();
            System.out.println("Log file successfully cleared.");
        } catch (IOException exception) {
            System.err.println("An error occurred while clearing the log file: " + exception.getMessage());
        }
        initiateApplication(args);
    }

    private static void initiateApplication(String[] args) {
        Application.launch(UserInterface.class, args);
    }
}
