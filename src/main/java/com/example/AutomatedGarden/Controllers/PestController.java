package com.example.AutomatedGarden.Controllers;

import com.example.AutomatedGarden.View.Logger;
import com.example.AutomatedGarden.Systems.Cleaner;
import com.example.AutomatedGarden.Model.BeneficialInsect;
import com.example.AutomatedGarden.Model.Insect;
import com.example.AutomatedGarden.Model.Pest;
import com.example.AutomatedGarden.Model.Plant;
import javafx.animation.PauseTransition;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;

import java.io.File;
import java.util.List;
import java.util.Random;

public class PestController {
    private static final String[] PESTS = {"Spider", "Caterpillar"};
    private static final String[] BENEFICIAL_INSECTS = {"Beetle", "Butterfly"};
    private final GridPane gridPane;
    private List<Cleaner> cleaners;
    private final Image cleanerImage;

    public PestController(GridPane gridPane) {
        this.gridPane = gridPane;
        this.cleanerImage = new Image(new File("src/main/images/cleaner.gif").toURI().toString());
        cleaners = List.of(new Cleaner(), new Cleaner(), new Cleaner()); // Initialize with 3 cleaners
    }

    public List<Cleaner> getCleaners() {
        return cleaners;
    }

    public void managePests(List<Plant> plants, List<Insect> insects, Logger logger, int dayCount) {
        Random random = new Random();
        for (Plant plant : plants) {
            if (plant.isDead()) continue;
            if (random.nextBoolean()) {
                String pestName = PESTS[random.nextInt(PESTS.length)];
                Insect pest = new Pest(pestName, plant.getRow(), plant.getCol());
                if (!isInsectPresent(plant, insects)) {
                    insects.add(pest);
                    plant.incrementPestAttacks();
                    logger.addInsectLogEntry("Day " + dayCount + ": Pest attack by " + pestName + " on plant: " + plant.getName() + " at grid (" + plant.getRow() + "," + plant.getCol() + ")");
                }
            } else {
                String insectName = BENEFICIAL_INSECTS[random.nextInt(BENEFICIAL_INSECTS.length)];
                Insect beneficialInsect = new BeneficialInsect(insectName, plant.getRow(), plant.getCol());
                if (!isInsectPresent(plant, insects)) {
                    insects.add(beneficialInsect);
                    logger.addInsectLogEntry("Day " + dayCount + ": Beneficial insect " + insectName + " found near plant: " + plant.getName() + " at grid (" + plant.getRow() + "," + plant.getCol() + ")");
                }
            }
            if (plant.getPestAttacks() > 1 && !plant.isDead()) {
                Cleaner availableCleaner = cleaners.stream().filter(cleaner -> !cleaner.isBusy()).findFirst().orElse(null);
                if (availableCleaner != null) {
                    availableCleaner.visitPlant(plant);
                    if(plant.getPestAttacks()==0)
                    {
                        insects.remove(insects);
                    }
                    logger.addCleanerLogEntry("Day " + dayCount + " : Cleaner" + availableCleaner + " cleaning pests near plant: " + plant.getName() + " at grid (" + plant.getRow() + "," + plant.getCol() + ")");

                    handleCleanerGif(availableCleaner, plant.getRow(), plant.getCol(), logger, dayCount, plant.getName());
                }
            }
        }

        // Finish cleaner visits
        for (Cleaner cleaner : cleaners) {
            if (cleaner.isBusy()) {
                cleaner.finishVisit();
            }
        }
    }

    private boolean isInsectPresent(Plant plant, List<Insect> insects) {
        for (Insect insect : insects) {
            if (insect.getRow() == plant.getRow() && insect.getCol() == plant.getCol()) {
                return true;
            }
        }
        return false;
    }

    private void handleCleanerGif(Cleaner cleaner, int row, int col, Logger logger, int dayCount, String plantName) {
        ImageView imageView = new ImageView(cleanerImage);
        imageView.setFitWidth(50);
        imageView.setFitHeight(50);
        gridPane.add(imageView, col, row);
        logger.addCleanerLogEntry("Day " + dayCount + ": Cleaner visiting plant: " + plantName + " at grid (" + row + "," + col + ")");
        PauseTransition pause = new PauseTransition(Duration.seconds(2));
        pause.setOnFinished(_ -> gridPane.getChildren().remove(imageView));
        pause.play();
    }
}