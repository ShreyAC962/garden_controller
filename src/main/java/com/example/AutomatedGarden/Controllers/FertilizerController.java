package com.example.AutomatedGarden.Controllers;
import com.example.AutomatedGarden.View.Logger;
import com.example.AutomatedGarden.Systems.Fertilizer;
import com.example.AutomatedGarden.Model.Plant;
import javafx.animation.TranslateTransition;
import javafx.animation.ScaleTransition;
import javafx.scene.Node;
import javafx.util.Duration;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.image.Image;

import java.io.File;
import java.util.List;
import java.util.Random;

public class FertilizerController {
    private static final String FERTILIZER_IMAGE_PATH = "src/main/images/farmer_walk.gif"; // Fertilizer image

    private final GridPane gridPane;
    private final Image fertilizerImage;
    private List<Fertilizer> fertilizers;
    private final Random random;
    private static final int HIGHLIGHT_DURATION_MS = 2000;

    public FertilizerController(GridPane gridPane) {
        this.gridPane = gridPane;
        this.fertilizerImage = new Image(new File(FERTILIZER_IMAGE_PATH).toURI().toString());
        fertilizers = List.of(
                new Fertilizer("Organic Fertilizer", 3, 1),
                new Fertilizer("Chemical Fertilizer", 5, 2)
        ); // Initialize with 2 types of fertilizers
        random = new Random();
    }

    public List<Fertilizer> getFertilizers() {
        return fertilizers;
    }


    public EventHandler<ActionEvent> manageFertilizers(List<Plant> plants, Logger logger, int dayCount) {
        for (Plant plant : plants) {
            if (plant.isDead()) continue; // Skip dead plants
            Fertilizer fertilizer = fertilizers.get(random.nextInt(fertilizers.size()));

            if (fertilizer.isInStock()) {
                fertilizer.reduceStock(1); // Use one unit of fertilizer
                applyFertilizer(plant, fertilizer, logger, dayCount);
            } else {
                logger.addFertilizerLogEntry("Day " + dayCount + ": No stock available for " + fertilizer.getName());
            }

        }
        return null;
    }

    public void applyFertilizer(Plant plant, Fertilizer fertilizer, Logger logger, int dayCount) {
        plant.incrementDaysSinceLastFertilized();
        if (plant.getDaysSinceLastFertilized() >= plant.getFertilizingFrequency()) {
            plant.boostGrowth();


            logger.addFertilizerLogEntry("Day " + dayCount + ": Applied " + fertilizer.getName()
                    + " to plant: " + plant.getName() + " at grid (" + plant.getRow() + "," + plant.getCol() + ")");
            highlightPlant(plant);
            showFertilizerAnimation(plant);
            plant.resetDaysSinceLastFertilized();
        } else {
            logger.addFertilizerLogEntry("Day " + dayCount + ": Skipped fertilizing " + plant.getName()
                    + " at grid (" + plant.getRow() + "," + plant.getCol() + "). Days since last fertilized: "
                    + plant.getDaysSinceLastFertilized() + ". Frequency: " + plant.getFertilizingFrequency());
        }
    }

    public void showFertilizerAnimation(Plant plant) {
        ImageView imageView = new ImageView(fertilizerImage);
        imageView.setFitWidth(30);
        imageView.setFitHeight(30);
        gridPane.add(imageView, 0, 0);
        int targetCol = plant.getCol();
        int targetRow = plant.getRow();
        double cellWidth = gridPane.getWidth() / gridPane.getColumnCount();
        double cellHeight = gridPane.getHeight() / gridPane.getRowCount();
        double targetX = targetCol * cellWidth + cellWidth / 2 - imageView.getFitWidth() / 2;
        double targetY = targetRow * cellHeight + cellHeight / 2 - imageView.getFitHeight() / 2;
        TranslateTransition transition = new TranslateTransition(Duration.seconds(2), imageView);
        transition.setToX(targetX - imageView.getLayoutX()); // Adjust based on the current layout position
        transition.setToY(targetY - imageView.getLayoutY()); // Adjust based on the current layout position
        transition.setOnFinished(event -> {
            System.out.println("Removing fertilizer animation");
            gridPane.getChildren().remove(imageView);
        });

        transition.play();
    }

    private Node getNodeFromGridPane(GridPane gridPane, int col, int row) {
        for (Node node : gridPane.getChildren()) {
            if (GridPane.getColumnIndex(node) == col && GridPane.getRowIndex(node) == row) {
                return node;
            }
        }
        return null;
    }

    public void highlightPlant(Plant plant) {

        Node plantNode = getNodeFromGridPane(gridPane, plant.getCol(), plant.getRow());

        if (plantNode != null) {

            ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(0.5), plantNode);
            scaleTransition.setToX(1.5);
            scaleTransition.setToY(1.5);
            scaleTransition.setAutoReverse(true);
            scaleTransition.setCycleCount(2);

            scaleTransition.play();
        }
    }


    public void restockFertilizer(String fertilizerName, int amount) {
        for (Fertilizer fertilizer : fertilizers) {
            if (fertilizer.getName().equals(fertilizerName)) {
                fertilizer.increaseStock(amount);
                System.out.println("Restocked " + amount + " units of " + fertilizerName);
                return;
            }
        }
        System.out.println("Fertilizer " + fertilizerName + " not found.");
    }
}

