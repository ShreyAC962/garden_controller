package com.example.AutomatedGarden.View;
import javafx.application.Application;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) {
        String filePath = "garden_logs.txt";
        try {
            Files.newBufferedWriter(Paths.get(filePath)).close();
            System.out.println("File got cleared");
        } catch (IOException e) {
            System.err.println("Error clearing the file: " + e.getMessage());
        }
        launch(args);
    }

    private static void launch(String[] args) {
        Application.launch(UserInterface.class, args);
    }
}
