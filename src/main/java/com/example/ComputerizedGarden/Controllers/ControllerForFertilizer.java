package com.example.ComputerizedGarden.Controllers;
import com.example.ComputerizedGarden.View.Logger;
import javafx.util.Duration;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.animation.TranslateTransition;
import javafx.animation.ScaleTransition;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.Node;
import javafx.scene.image.Image;
import java.io.File;
import java.util.List;
import java.util.Random;
import com.example.ComputerizedGarden.Systems.Fertilizer;
import com.example.ComputerizedGarden.Model.Plant;

public class ControllerForFertilizer {
    private static final String FERTILIZER_IMAGE_PATH = "src/main/images/farmer_walk.gif"; // Fertilizer image

    private final GridPane gridPane;
    private final Image fertilizerImage;
    private List<Fertilizer> fertilizers;
    private final Random random;
//    private static final int HIGHLIGHT_DURATION_MS = 2000;

    public ControllerForFertilizer(GridPane gridPane) {
        this.gridPane = gridPane;
        this.fertilizerImage = new Image(new File(FERTILIZER_IMAGE_PATH).toURI().toString());
        fertilizers = List.of(
                new Fertilizer("Organic Fertilizer", 13, 1),
                new Fertilizer("Chemical Fertilizer", 13, 2)
        ); // Initialize with 2 types of fertilizers
        random = new Random();
    }


    public EventHandler<ActionEvent> manageFertilizers(List<Plant> plants, Logger logger, int dayCount) {
        if (dayCount % 3 == 0) { // Every 3 days, restock fertilizers
            restockFertilizer("Organic Fertilizer", 8, logger);
            restockFertilizer("Chemical Fertilizer", 8, logger);
        }

        for (Plant plant : plants) {
            if (plant.isDead()) continue; // Skip dead plants
            Fertilizer fertilizer = fertilizers.get(random.nextInt(fertilizers.size()));

            if (fertilizer.isInStock()) {
                fertilizer.reduceStock(1); // Use one unit of fertilizer
                fertilizerApply(plant, fertilizer, logger, dayCount);
            } else {
                logger.addFertilizerLogEntry("Day " + dayCount + ": No stock available for " + fertilizer.getName());
            }
        }
        return null;
    }

    public void restockFertilizer(String fertilizerName, int amount, Logger logger) {
        for (Fertilizer fertilizer : fertilizers) {
            if (fertilizer.getName().equals(fertilizerName)) {
                fertilizer.increaseStock(amount);
                System.out.println("Restocked " + amount + " units of " + fertilizerName);
                logger.addFertilizerLogEntry("Restocked " + amount + " units of " + fertilizerName);
                return;
            }
        }
        System.out.println("Fertilizer " + fertilizerName + " not found.");
    }

    public void fertilizerApply(Plant plant, Fertilizer fertilizer, Logger logger, int dayCount) {
        plant.incrementDaysSinceLastFertilized();
        if (plant.getDaysSinceLastFertilized() >= plant.getFertilizingFrequency()) {
            plant.boostGrowth();


            logger.addFertilizerLogEntry("Day " + dayCount + ": Applied " + fertilizer.getName()
                    + " to plant: " + plant.getName() + " at grid (" + plant.getRow() + "," + plant.getCol() + ")");
            toHighlightPlant(plant);
            showFertilizerAnimation(plant);
            plant.resetDaysSinceLastFertilized();
        } else {
            logger.addFertilizerLogEntry("Day " + dayCount + ": Skipped fertilizing " + plant.getName()
                    + " at grid (" + plant.getRow() + "," + plant.getCol() + "). Days since last fertilized: "
                    + plant.getDaysSinceLastFertilized() + ". Frequency: " + plant.getFertilizingFrequency());
        }
    }

    public void toHighlightPlant(Plant plant) {

        Node plantNode = getNodebyGridPane(gridPane, plant.getCol(), plant.getRow());

        if (plantNode != null) {

            ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(0.5), plantNode);
            scaleTransition.setToX(1.5);
            scaleTransition.setToY(1.5);
            scaleTransition.setAutoReverse(true);
            scaleTransition.setCycleCount(2);

            scaleTransition.play();
        }
    }

    public List<Fertilizer> getFertilizers() {
        return fertilizers;
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

    private Node getNodebyGridPane(GridPane gridPane, int col, int row) {
        for (Node node : gridPane.getChildren()) {
            if (GridPane.getColumnIndex(node) == col && GridPane.getRowIndex(node) == row) {
                return node;
            }
        }
        return null;
    }





}

