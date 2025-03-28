package com.example.ComputerizedGarden.View;

import javafx.scene.layout.*;
import com.example.ComputerizedGarden.Controllers.ControllerForGarden;
import com.example.ComputerizedGarden.Controllers.ControllerForPest;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import com.example.ComputerizedGarden.Controllers.ControllerForSensor;
import com.example.ComputerizedGarden.Systems.Sensor;
import com.example.ComputerizedGarden.Systems.Cleaner;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.stage.Stage;
import com.example.ComputerizedGarden.Systems.IrrigationSystem.Zone;
import javafx.animation.*;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.util.Duration;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import com.example.ComputerizedGarden.Controllers.ControllerForFertilizer;
import com.example.ComputerizedGarden.Model.*;
import com.example.ComputerizedGarden.Systems.IrrigationSystem.IrrigationSystem;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class UserInterface extends Application {
    protected GridPane gardenGrid;
    protected ListView<TextFlow> dayLogList;
    protected ListView<TextFlow> wateringLogList;
    protected ListView<TextFlow> heatingLogList;
    protected ListView<TextFlow> insectLogList;
    protected ListView<TextFlow> fertilizerLogList;
    protected ListView<TextFlow> cleanerLogList;
    protected Random random = new Random();
    protected ImageView weatherImageView;
    protected Label weatherLabel;
    protected ListView<String> directoryListView;
    protected Timeline simulationTimeline;
    protected ControllerForPest controllerForPest;
//    protected TableView<Plant> plantTable;
    protected String currentWeather;
    protected ProgressBar waterProgressBar;
    protected Label waterProgressLabel;
    protected ProgressBar temperatureProgressBar;
    protected Label temperatureProgressLabel;
    private ControllerForGarden controllerForGarden;
    protected ControllerForFertilizer controllerForFertilizer;
    protected Button startSimulationButton;
    protected Button pauseSimulationButton;
//    protected Button modifyIrrigation;
    private final HashMap<String, Image> imageCache = new HashMap<>();
    private Label fertilizerStockLabel;
    private ProgressBar fertilizerStockProgressBar;
    private int fertilizerStock = 100;
    private ControllerForSensor controllerForSensor;


    //------ Other Methods ------
    @Override
    public void start(Stage primaryStage) {
        controllerForSensor = new ControllerForSensor(gardenGrid);

        VBox root = new VBox();
        //root.setSpacing(10);
        root.setStyle(" -fx-background-image: url('file:src/main/images/garden_bg.jpg');"
                + "-fx-background-size: cover;");
        createGardenGrid();
        VBox gridBox = new VBox();
        VBox plantBox = createPlantBox();
//        plantBox.setSpacing(140);

        ImageView bird = createBirdImage();
        Pane birdPane = new Pane(); // Use Pane for free movement
        birdPane.getChildren().add(bird);
        bird.setLayoutY(50); // Set initial Y position

// Start flying animation
//        animateBird(bird);p----------------------
//        scatterButterflies(root);p-----------------

// Add the new birdPane to gridBox
//        gridBox.getChildren().addAll(birdPane);
        gridBox.getChildren().addAll(plantBox, gardenGrid);

//        ImageView bird = createBirdImage();
//        if (gardenGrid != null) {
//            gridBox.getChildren().addAll(bird,plantBox, gardenGrid);
//        }

        //controllerForSensor.placeSensorInGrid();

        VBox.setVgrow(gardenGrid, Priority.ALWAYS);

        createLogLists();
        Accordion logAccordion = createLogList();
        logAccordion.setVisible(false);

        HBox topBox = createTopBox(gridBox);

        VBox weatherBox = createWeatherBox();
        VBox progressBox = createProgressBox();

//        ImageView farmer = createFarmerImage();p--------------------------



        HBox tempAndWaterBox = createTempAndWaterBox(progressBox);

        Label directoryLabel = createDirectoryLabel();
        createDirectoryListView();

        VBox directoryBox = createDirectoryBox(directoryLabel);
        // Set up fertilizer stock label and progress bar
        fertilizerStockLabel = new Label("Fertilizer Stock: " + fertilizerStock);
        fertilizerStockLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        fertilizerStockLabel.setTextFill(Color.GREEN);

        fertilizerStockProgressBar = new ProgressBar(1.0);  // Full stock at the start
        fertilizerStockProgressBar.setPrefWidth(200);

        // Add to your layout (root, VBox, etc.)
//        root.getChildren().addAll(fertilizerStockLabel, fertilizerStockProgressBar);
        root.getChildren().add(directoryBox);
        // Add Fertilizer Button
        // Add Fertilizer Button Below Weather Report
        VBox rightPane = createRightPane(weatherBox, tempAndWaterBox, directoryLabel);
//        createPlantTable();

        HBox mainBox = createMainBox(topBox, rightPane);
        HBox buttonBox = createButtonBox();
        //gridBox.getChildren().add(buttonBox);

        // Button to toggle logs
        Button toggleLogsButton = new Button("Show Logs");
        toggleLogsButton.setStyle("-fx-background-color: black; -fx-text-fill: white; -fx-border-color: white;");
        toggleLogsButton.setOnAction(event -> {
            if (logAccordion.isVisible()) {
                logAccordion.setVisible(false); // Hide logs
                root.getChildren().remove(logAccordion);
                toggleLogsButton.setText("Show Logs");
                toggleLogsButton.setStyle("-fx-background-color: black; -fx-text-fill: white; -fx-border-color: white;");
            } else {
                logAccordion.setVisible(true); // Show logs
                root.getChildren().add(logAccordion);
                toggleLogsButton.setText("Hide Logs");
                toggleLogsButton.setStyle("-fx-background-color: black; -fx-text-fill: white; -fx-border-color: white;");
            }
        });

        buttonBox.getChildren().add(toggleLogsButton);
        gridBox.getChildren().add(buttonBox);
//        gridBox.getChildren().add(farmer);p--------------------------
        root.getChildren().addAll(mainBox);

        //root.getChildren().addAll(mainBox, logAccordion);

        Scene scene = new Scene(root);
        primaryStage.setTitle("Automated Gardening System");
        primaryStage.setScene(scene);
        primaryStage.setFullScreen(true);
        primaryStage.show();

        controllerForPest = new ControllerForPest(gardenGrid);
        controllerForFertilizer = new ControllerForFertilizer(gardenGrid);
        controllerForGarden = new ControllerForGarden(controllerForPest, controllerForFertilizer, controllerForSensor, gardenGrid);
        controllerForSensor = new ControllerForSensor(gardenGrid);

        startAnimation();
    }

    private ImageView createFarmerImageWalk() {
        // Load the GIF of the farmer (replace with your actual file path)
        Image farmerImage = new Image("file:src/main/images/farmer_walk.gif");
        ImageView farmerImageView = new ImageView(farmerImage);

        // Set initial size for the farmer image
        farmerImageView.setFitHeight(600); // Adjust the height as needed
        farmerImageView.setFitWidth(600);  // Adjust the width as needed

        return farmerImageView;
    }

    private void animateFarmerImage(ImageView farmerImageView, VBox root) {
        // Create the Timeline animation
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(0), e -> {
                    // Initial position of the farmer (just below the garden grid)
                    farmerImageView.setX(0);  // Starting from the left side
                    farmerImageView.setY(gardenGrid.getLayoutBounds().getMaxY() + 10); // 10px below the garden grid
                }),
                new KeyFrame(Duration.seconds(5), e -> {
                    // Final position of the farmer (moving to the right side)
                    farmerImageView.setX(root.getWidth() - farmerImageView.getFitWidth()); // Right side of the screen
                })
        );

        // Set the animation to repeat indefinitely and auto-reverse
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.setAutoReverse(true); // Moves back and forth
        timeline.play();  // Start the animation
    }


    private void createGardenGrid() {
        gardenGrid = new GridPane();
        gardenGrid.setPadding(new Insets(20));
        gardenGrid.setHgap(5);
        gardenGrid.setVgap(5);

        String imagePath = "file:src/main/images/soil.png";

        for (int i = 0; i < 12; i++) {
            for (int j = 0; j < 7; j++) {
                StackPane cell = new StackPane();
                cell.setMinSize(45, 45); // Adjust cell size

                // Set image background
                cell.setStyle("-fx-border-color: black; -fx-background-image: url('" + imagePath + "');"
                        + "-fx-background-size: cover; -fx-background-position: center;");

                // Add hover effect (optional - slightly brighten the cell)
                cell.setOnMouseEntered(e -> cell.setStyle("-fx-border-color: black; -fx-background-image: url('" + imagePath + "'); -fx-opacity: 0.8;"));
                cell.setOnMouseExited(e -> cell.setStyle("-fx-border-color: black; -fx-background-image: url('" + imagePath + "'); -fx-opacity: 1.0;"));

                // Add cells to the grid
                gardenGrid.add(cell, i, j);
            }
        }

        // Fade-in animation
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(1), gardenGrid);
        fadeTransition.setFromValue(0);
        fadeTransition.setToValue(1);
        fadeTransition.play();

        HBox.setHgrow(gardenGrid, Priority.ALWAYS);
    }



    private void createLogLists() {
        dayLogList = new ListView<>();
        VBox.setVgrow(dayLogList, Priority.ALWAYS);

        wateringLogList = new ListView<>();
        VBox.setVgrow(wateringLogList, Priority.ALWAYS);

        heatingLogList = new ListView<>();
        VBox.setVgrow(heatingLogList, Priority.ALWAYS);

        insectLogList = new ListView<>();
        VBox.setVgrow(insectLogList, Priority.ALWAYS);

        cleanerLogList = new ListView<>();
        VBox.setVgrow(cleanerLogList, Priority.ALWAYS);

        fertilizerLogList = new ListView<>();
        VBox.setVgrow(fertilizerLogList, Priority.ALWAYS);
    }

    private Accordion createLogList() {
        // Accordion for Logs
        dayLogList = createStyledListView("Day Logs");
        wateringLogList = createStyledListView("Watering Logs");
        heatingLogList = createStyledListView("Heating Logs");
        insectLogList = createStyledListView("Insect Attack Logs");
        cleanerLogList = createStyledListView("Cleaner Logs");
        fertilizerLogList = createStyledListView("Fertilizer Logs");

        TitledPane dayLogPane = createStyledTitledPane("Day Logs", dayLogList);
        TitledPane wateringLogPane = createStyledTitledPane("Watering Logs", wateringLogList);
        TitledPane heatingLogPane = createStyledTitledPane("Heating Logs", heatingLogList);
        TitledPane insectLogPane = createStyledTitledPane("Insect Attack Logs", insectLogList);
        TitledPane cleanerLogPane = createStyledTitledPane("Cleaner Logs", cleanerLogList);
        TitledPane fertilizerLogPane = createStyledTitledPane("Fertilizer Logs", fertilizerLogList);

        Accordion logAccordion = new Accordion();
        logAccordion.getPanes().addAll(dayLogPane, wateringLogPane, heatingLogPane, insectLogPane, cleanerLogPane, fertilizerLogPane);
        VBox.setVgrow(logAccordion, Priority.ALWAYS);
        logAccordion.setStyle("-fx-background-color: transparent;");
        return logAccordion;
    }

    private HBox createTopBox(VBox gridBox) {
        HBox topBox = new HBox();
        topBox.setSpacing(10);
        topBox.getChildren().addAll(gridBox);
        HBox.setHgrow(gridBox, Priority.ALWAYS);
        return topBox;
    }

    private VBox createPlantBox() {
        VBox plantBox = new VBox();
        plantBox.setSpacing(10);

        Label plantLabel = new Label("Select Plants");
        plantLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        plantLabel.setTextFill(Color.BLACK);  // Set text color to white for better visibility
        plantLabel.setPadding(new Insets(220, 0, 0, 0)); // 10px padding on top


        HBox plantImagesBox = new HBox();
        plantImagesBox.setSpacing(10);

        VBox cornBox = createPlantOption("src/main/images/corn.gif", "Corn");
        VBox pineappleBox = createPlantOption("src/main/images/pineapple.gif", "Pineapple");
        VBox watermelonBox = createPlantOption("src/main/images/watermelon.gif", "Watermelon");
        VBox carrotBox = createPlantOption("src/main/images/carrot.gif", "Carrot");

        plantImagesBox.getChildren().addAll(cornBox, pineappleBox, watermelonBox,carrotBox);
        plantBox.getChildren().addAll(plantLabel, plantImagesBox);

        VBox.setVgrow(plantBox, Priority.ALWAYS);
        //plantBox.setStyle("-fx-background-color: transparent;");
        return plantBox;
    }

    private VBox createPlantOption(String fileName, String plantType) {
        ImageView imageView = getImageView(fileName);
        Button button = new Button();
        button.setGraphic(imageView);
        button.setStyle("-fx-background-color: black; -fx-text-fill: white; -fx-font-weight: bold; -fx-border-color: #FFFFFF;");
        button.setOnAction(_ -> {
            int row, col;
            do {
                row = random.nextInt(7);
                col = random.nextInt(12);
            } while (!isCellEmpty(row, col));
            Plant plant = null;
            if ("Corn".equals(plantType)) {
                plant = new Corn(row, col);
            } else if ("Pineapple".equals(plantType)) {
                plant = new Pineapple(row, col);
            } else if ("Watermelon".equals(plantType)) {
                plant = new Watermelon(row, col);
            }else if ("Carrot".equals(plantType)) {
                plant = new Carrot(row, col);
            }
            if (plant != null) {
                controllerForGarden.addPlant(plant);
                animatePlant(plant);
                updateGardenGrid();
                updateLog();
                updateDirectory();
            }
        });

        Label nameLabel = new Label(plantType);
        nameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        nameLabel.setStyle("-fx-background-color: black; -fx-text-fill: white; -fx-border-color: white;");
//        nameLabel.setTextFill(Color.BLACK);  // Set text color to white for better visibility
//        nameLabel.setAlignment(Pos.CENTER);

        VBox plantBox = new VBox(imageView, button, nameLabel);
        plantBox.setAlignment(Pos.CENTER);
        plantBox.setSpacing(5);

        return plantBox;
    }

    private VBox createWeatherBox() {
        Label weatherReportLabel = new Label("Weather Forecast");
        weatherReportLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        weatherReportLabel.setStyle(
                "-fx-background-color: black; "  // Green background color
                        + "-fx-text-fill: white; "  // White text color
                        + "-fx-border-color: #FFFFFF; "  // White border
                        + "-fx-border-width: 2px; "  // Border width
                        + "-fx-padding: 10px; "  // Padding around the text
                        + "-fx-background-radius: 10px; "
                        + "-fx-effect: dropshadow(three-pass-box, rgba(0, 0, 0, 0.5), 5, 0.5, 0, 2); "  // Drop shadow for effect
                        + "-fx-font-weight: bold;"  // Bold font style
        );
        weatherImageView = new ImageView();
        weatherImageView.setFitWidth(100);
        weatherImageView.setFitHeight(100);
        weatherLabel = new Label("Day 1: Start");
        weatherLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
//        weatherLabel.setStyle("-fx-padding: 5;");

        VBox weatherBox = new VBox();
        weatherBox.setSpacing(10);
        weatherBox.setAlignment(Pos.CENTER);
        weatherBox.getChildren().addAll(weatherReportLabel, weatherImageView, weatherLabel);
        weatherBox.setStyle("-fx-background-color: transparent;");
        return weatherBox;
    }

    private VBox createProgressBox() {
        VBox progressBox = new VBox();
        progressBox.setSpacing(10);

        // Water Progress Bar and Label
        waterProgressBar = new ProgressBar(0);
        waterProgressBar.setPrefWidth(200); // Set preferred width of the ProgressBar
        waterProgressLabel = new Label("Water percentage: 0%");
        waterProgressLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        waterProgressLabel.setStyle("-fx-background-color: black; -fx-text-fill: white;");

        // Temperature Progress Bar and Label
        temperatureProgressBar = new ProgressBar(0);
        temperatureProgressBar.setPrefWidth(200); // Set preferred width of the ProgressBar
        temperatureProgressLabel = new Label("Temperature percentage: 0%");
        temperatureProgressLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        temperatureProgressLabel.setStyle("-fx-background-color: black; -fx-text-fill: white;");
        progressBox.getChildren().addAll(temperatureProgressBar, temperatureProgressLabel, waterProgressBar, waterProgressLabel);
        return progressBox;
    }


    private ImageView createBirdImage() {
        ImageView imageView = new ImageView(
                new Image(new File("src/main/images/bird1.gif").toURI().toString())); // Default: left
        imageView.setFitWidth(110);
        imageView.setFitHeight(110);
        return imageView;
    }


    private void animateBird(ImageView bird) {
        Image birdLeft = new Image(new File("src/main/images/bird1.gif").toURI().toString()); // Default (Left-facing)
        Image birdRight = new Image(new File("src/main/images/bird2.gif").toURI().toString()); // Right-facing (Flipped)

        // Move bird right
        TranslateTransition moveRight = new TranslateTransition(Duration.seconds(5), bird);
        moveRight.setFromX(0);
        moveRight.setToX(500);

        // Move bird left
        TranslateTransition moveLeft = new TranslateTransition(Duration.seconds(5), bird);
        moveLeft.setFromX(500);
        moveLeft.setToX(0);

        // When moving right completes, switch to left
        moveRight.setOnFinished(event -> {
            bird.setImage(birdRight); // Set flipped image
            bird.setTranslateX(0); // Reset to left side
            moveLeft.play(); // Move left
        });

        // When moving left completes, switch to right
        moveLeft.setOnFinished(event -> {
            bird.setImage(birdLeft); // Set normal image
            bird.setTranslateX(800); // Reset to right side
            moveRight.play(); // Move right
        });

        // Start moving right initially
        bird.setImage(birdLeft);
        moveRight.play();
    }


    private void scatterButterflies(Pane root) {
        String[] butterflyImages = {
                "src/main/images/butterfly1.gif",
                "src/main/images/butterfly2.gif",
                "src/main/images/butterfly3.gif",
                "src/main/images/butterfly4.gif"
        };

        Random random = new Random();
        for (String imagePath : butterflyImages) {
            ImageView butterfly = new ImageView(new Image(new File(imagePath).toURI().toString()));
            butterfly.setFitWidth(40);  // Adjust size
            butterfly.setFitHeight(40);

            // Set initial random position
            butterfly.setLayoutX(random.nextInt(800)); // Random X position
            butterfly.setLayoutY(random.nextInt(600)); // Random Y position

            root.getChildren().add(butterfly); // Add to root so they move freely

            // Create a timeline for continuous random movement
            Timeline movement = new Timeline(new KeyFrame(Duration.seconds(2), event -> {
                double newX = random.nextInt(800);
                double newY = random.nextInt(600);
                TranslateTransition move = new TranslateTransition(Duration.seconds(2), butterfly);
                move.setToX(newX);
                move.setToY(newY);
                move.play();
            }));

            movement.setCycleCount(Timeline.INDEFINITE);
            movement.play();
        }
    }




    private ImageView createFarmerImage() {
        ImageView imageView = new ImageView(
                new Image(new File("src/main/images/happy_farmer.gif").toURI().toString()));
        imageView.setFitWidth(100);
        imageView.setFitHeight(100);
        return imageView;
    }

    private HBox createTempAndWaterBox(VBox progressBox) {
        HBox tempAndWaterBox = new HBox();
        tempAndWaterBox.setSpacing(10);
        tempAndWaterBox.setAlignment(Pos.CENTER);
        tempAndWaterBox.getChildren().addAll(progressBox);
        tempAndWaterBox.setStyle("-fx-background-color: transparent;");
        return tempAndWaterBox;
    }

    private Label createDirectoryLabel() {
        Label directoryLabel = new Label("MONITORING TABLE");
        directoryLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        directoryLabel.setStyle("-fx-background-color: black; -fx-text-fill: white; -fx-border-color: white;");
//        directoryLabel.setStyle("-fx-text-fill: #FFFFFF; -fx-padding: 10; -fx-alignment: center;");

        VBox directoryLabelBox = new VBox();
        directoryLabelBox.setStyle(
                "-fx-border-color: #4682B4; "
                        + "-fx-border-width: 2px; "
                        + "-fx-background-color: #f5f5f5; "
                        + "-fx-padding: 10px;");
        return directoryLabel;
    }


private void createDirectoryListView() {
    directoryListView = new ListView<>();
    directoryListView.setStyle(

                    "-fx-border-color: black;"
                    + "-fx-border-width: 2px;"
                    + "-fx-padding: 10px;"
                    + "-fx-spacing: 5px;"
                    + "-fx-background-size: cover;"
                    + "-fx-background-position: center center;"
                    + "-fx-background-repeat: no-repeat;"
    );

    // Set preferred and maximum sizes
    directoryListView.setPrefSize(400, 300);
    directoryListView.setMaxSize(300, 200);

    // Apply a cell factory for custom cell styling and text content formatting
    directoryListView.setCellFactory(_ -> new ListCell<>() {
        @Override
        protected void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);

            if (empty || item == null) {
                setText(null);
                setGraphic(null);
                setStyle(""); // Clear styles for empty cells
            } else {
                // Create a styled label for the content
                Label label = new Label(item);
                label.setStyle(
                        "-fx-text-fill: #2E8B57;" // Sea green text color
                                + "-fx-font-family: 'Arial';" // Font family
                                + "-fx-font-size: 14px;" // Font size
                                + "-fx-font-weight: bold;" // Bold text
                                + "-fx-padding: 5px;" // Padding inside label
                );

                // Apply alternating row colors
                int index = getIndex();
                if (index % 2 == 0) {
                    setStyle("-fx-background-color: #D3D3D3;"); // Light GREY for even rows
                } else {
                    setStyle("-fx-background-color: white;"); // White for odd rows
                }

                setGraphic(label);
                setText(null); // Remove default text to avoid duplication
            }
        }
    });

    // Ensure the ListView grows with the VBox
    VBox.setVgrow(directoryListView, Priority.ALWAYS);
}




    private VBox createDirectoryBox(Label directoryLabel) {
        VBox directoryBox = new VBox(10);
        directoryBox.setStyle("-fx-padding: 10; -fx-background-color: transparent;");
        directoryBox.getChildren().addAll(directoryLabel, directoryListView);
        directoryBox.setAlignment(Pos.CENTER);
        return directoryBox;
    }

    private VBox createRightPane(VBox weatherBox, HBox tempAndWaterBox, Label directoryLabel) {
        VBox rightPane = new VBox();
        rightPane.setSpacing(10);
        rightPane.getChildren().addAll(
                weatherBox,
//                new Separator(),
                tempAndWaterBox,
//                new Separator(),
                directoryLabel,
                directoryListView);
        HBox.setHgrow(rightPane, Priority.ALWAYS);
        rightPane.setStyle("-fx-background-color: transparent;");
        return rightPane;
    }


//private void createPlantTable() {
//    // Create TableView
//    plantTable = new TableView<>();
//    plantTable.setStyle(
//            "-fx-background-image: url('file:src/main/images/vegetables.jpg');"
//                    + "-fx-background-size: cover; "
//                    + "-fx-background-position: center center; "
//                    + "-fx-background-repeat: no-repeat; "
//    );
//
//
//    Label placeholderLabel = new Label("No plants available");
//    placeholderLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: #4CAF50;");
//    plantTable.setPlaceholder(placeholderLabel);
//    TableColumn<Plant, String> nameColumn = new TableColumn<>("Plant");
//    nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
//    nameColumn.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-alignment: center;");
//
//    TableColumn<Plant, Integer> countColumn = new TableColumn<>("Count");
//    countColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getCount()).asObject());
//    countColumn.setStyle("-fx-font-size: 14px; -fx-alignment: center;");
//
//    TableColumn<Plant, Integer> lifespanColumn = new TableColumn<>("Lifespan");
//    lifespanColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getDaysToLive()).asObject());
//    lifespanColumn.setStyle("-fx-font-size: 14px; -fx-alignment: center;");
//    TableColumn<Plant, Integer> pestCountColumn = new TableColumn<>("Pest Attacks");
//    pestCountColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getPestAttacks()).asObject());
//    pestCountColumn.setStyle("-fx-font-size: 14px; -fx-alignment: center;");
//    plantTable.getColumns().addAll(List.of(nameColumn, countColumn, lifespanColumn, pestCountColumn));
//
//    VBox tableContainer = new VBox();
//    tableContainer.setStyle("-fx-padding: 10; -fx-background-color:#98FB98 ;");
//    tableContainer.getChildren().add(plantTable);
//    VBox.setVgrow(tableContainer, Priority.ALWAYS);
//
//}


    private HBox createMainBox(HBox topBox, VBox rightPane) {
        HBox mainBox = new HBox();
        mainBox.setSpacing(10);
        mainBox.getChildren().addAll(rightPane,topBox);
        HBox.setHgrow(topBox, Priority.ALWAYS);
        return mainBox;
    }

    private HBox createButtonBox() {
        HBox buttonBox = new HBox();
        buttonBox.setSpacing(10);
        startSimulationButton = new Button("Start Simulation");
//        startSimulationButton.setStyle("-fx-background-color: #32CD32; -fx-text-fill: white; -fx-font-weight: bold;");
        startSimulationButton.setStyle("-fx-background-color: black; -fx-text-fill: white; -fx-border-color: white;");
        startSimulationButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        startSimulationButton.setOnAction(_ -> startSimulation());

        pauseSimulationButton = new Button("Pause Simulation");
//        pauseSimulationButton.setStyle("-fx-background-color: #FF0000; -fx-text-fill: white; -fx-font-weight: bold;");
        pauseSimulationButton.setStyle("-fx-background-color: black; -fx-text-fill: white; -fx-border-color: white;");
        pauseSimulationButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        pauseSimulationButton.setOnAction(_ -> pauseSimulation());
        pauseSimulationButton.setDisable(true);

//        modifyIrrigation = new Button("Modify Irrigation");
////        modifyIrrigation.setStyle("-fx-background-color: #000000; -fx-text-fill: white; -fx-font-weight: bold;");
//        modifyIrrigation.setStyle("-fx-background-color: black; -fx-text-fill: white; -fx-border-color: white;");
//        modifyIrrigation.setFont(Font.font("Arial", FontWeight.BOLD, 14));
//        modifyIrrigation.setOnAction(_ -> openModifyIrrigationPanel());

        buttonBox.getChildren().addAll(startSimulationButton, pauseSimulationButton);

        buttonBox.setStyle("-fx-background-color: transparent;");
        return buttonBox;
    }

//    private void openModifyIrrigationPanel() {
//        boolean needToStartAgain = false;
//        if (this.simulationTimeline != null) {
//            needToStartAgain = true;
//            pauseSimulation();
//        }
//        Stage irrigationPanel = new Stage();
//        irrigationPanel.initModality(Modality.APPLICATION_MODAL);
//        irrigationPanel.setTitle("Modify Irrigation Zones");
//        VBox layout = new VBox(10);
//        layout.setPadding(new Insets(10));
//        layout.setAlignment(Pos.CENTER);
//        Label titleLabel = new Label("Modify Irrigation Zones");
//        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
//        GridPane gridPane = new GridPane();
//        gridPane.setHgap(10);
//        gridPane.setVgap(10);
//        gridPane.setPadding(new Insets(10));
//
//        int row = 0;
//        int col = 0;
//        HashMap<Integer, Integer> intervals = new HashMap<>();
//        for (Zone zone : IrrigationSystem.getZones()) {
//            addZone(gridPane, zone, row, col, intervals);
//            col++;
//            if (col == 3) {
//                col = 0;
//                row++;
//            }
//        }
//
//        // Create Save and Cancel Buttons
//        HBox buttonBox = createSaveAndCancelBox(irrigationPanel, intervals);
//        layout.getChildren().addAll(titleLabel, gridPane, buttonBox);
//
//        Scene scene = new Scene(layout, 400, 600);
//        irrigationPanel.setScene(scene);
//        irrigationPanel.showAndWait();
//        if (needToStartAgain) {
//            startSimulation();
//        }
//    }

    private void addZone(GridPane gridPane, Zone zone, int row, int col, HashMap<Integer, Integer> intervals) {
        VBox zoneSettings = new VBox(10);
        zoneSettings.setPadding(new Insets(10));
        zoneSettings.setStyle("-fx-border-color: lightgray; -fx-border-width: 1; -fx-padding: 10;");
        zoneSettings.setAlignment(Pos.CENTER);

        // Zone ID
        Label zoneLabel = new Label("Zone " + zone.getId());
        zoneLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        // Checkbox to enable/disable the Zone
        CheckBox enableZone = new CheckBox("Enable Zone");
        enableZone.setSelected(zone.getEnabled());
        enableZone.selectedProperty().addListener((_, _, newEnable) -> zone.setEnabled(newEnable));

        // Drop selector to set the Zone's irrigation type
        Label irrigationTypeLabel = new Label("Irrigation Type:");
        ComboBox<String> irrigationTypeDropdown = new ComboBox<>();
        irrigationTypeDropdown.getItems().addAll(zone.getIrrigationTypes().keySet());
        irrigationTypeDropdown.setValue(zone.getType());
        irrigationTypeDropdown.valueProperty().addListener((_, _, newType) -> zone.setType(newType));

        //Textbox to input interval
        TextField intervalInput = new TextField(String.valueOf(zone.getInterval()));
        intervalInput.textProperty().addListener((_, _, newInterval) -> {
            if (!newInterval.isBlank()) {
                try {
                    intervals.put(zone.getId(), Integer.parseInt(newInterval));
                } catch (NumberFormatException _) {
                    System.err.println("An invalid interval was entered.");
                }
            }
        });

        zoneSettings.getChildren().addAll(zoneLabel, intervalInput, enableZone, irrigationTypeLabel, irrigationTypeDropdown);
        gridPane.add(zoneSettings, col, row);
    }

    private HBox createSaveAndCancelBox(Stage irrigationPanel, HashMap<Integer, Integer> intervals) {
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);

        // Save button
        Button saveButton = new Button("Save");
        saveButton.setStyle("-fx-background-color: #32CD32; -fx-text-fill: white;");
        saveButton.setOnAction(_ -> {
            intervals.forEach((key, value) -> IrrigationSystem.getZone(key).setInterval(value));
            System.out.println("Irrigation settings saved!");
            irrigationPanel.close();
        });

        // Cancel button
        Button cancelButton = new Button("Cancel");
        cancelButton.setStyle("-fx-background-color: #FF0000; -fx-text-fill: white;");
        cancelButton.setOnAction(_ -> irrigationPanel.close());

        buttonBox.getChildren().addAll(saveButton, cancelButton);
        return buttonBox;
    }

    private TitledPane createStyledTitledPane(String title, ListView<TextFlow> listView) {
        TitledPane pane = new TitledPane(title, listView);
        pane.setStyle("-fx-font-weight: bold; -fx-background-color: #f5f5f5;");
        return pane;
    }

    private ListView<TextFlow> createStyledListView(String placeholder) {
        ListView<TextFlow> listView = new ListView<>();
        listView.setPlaceholder(new Label(placeholder));
        listView.setStyle("-fx-background-color: rgba(255, 255, 255, 0.8); -fx-border-radius: 5;");
        VBox.setVgrow(listView, Priority.ALWAYS);
        return listView;
    }

    private void updateWeatherImage(String weather) {
        String weatherImagePath = switch (weather) {
            case "Sunny" -> "src/main/images/sunny1.gif";
            case "Rainy" -> "src/main/images/rain.gif";
            case "Cold" -> "src/main/images/cold1.gif";
            case "Snowy" -> "src/main/images/snowflakes1.gif";
            case "Cloudy" -> "src/main/images/cloudy1.gif";
            case "Windy" -> "src/main/images/windy.gif";
            default -> null;
        };
        if (weatherImagePath != null) {
            weatherImageView.setImage(new Image(new File(weatherImagePath).toURI().toString()));
        }



        weatherLabel.setText("Day " + (controllerForGarden.getDay()) + ": " + weather);

        int temperature = getTemperatureForDay(weather);
        temperatureProgressLabel.setText(temperature + " F");
        temperatureProgressLabel.setStyle(getLabelFontForWeather(weather));

        waterProgressLabel.setText(getWaterLevelStringForLabelFontForWeather(weather));
        waterProgressLabel.setStyle("-fx-text-fill: black;");

        // Update the progress bars
        updateProgressBars(temperature);
    }

    private int getTemperatureForDay(String currentWeather) {
        return switch (currentWeather) {
            case "Sunny" -> 90;
            case "Rainy" -> 50;
            case "Cold" -> 10;
            case "Snowy" -> 2;
            case "Cloudy" -> 70;
            case "Windy" -> 14;
            default -> 100;
        };
    }

    private String getLabelFontForWeather(String currentWeather) {
        return switch (currentWeather) {
            case "Sunny" -> "-fx-text-fill: yellow;";
            case "Rainy" -> "-fx-text-fill: purple;";
            case "Cloudy" -> "-fx-text-fill: green;";
            case "Cold" -> "-fx-text-fill: lavender;";
            case "Snowy" -> "-fx-text-fill: white;";
            case "Windy" -> "-fx-text-fill: blue;";
            default -> "-fx-text-fill: black;";
        };
    }

    private String getWaterLevelStringForLabelFontForWeather(String currentWeather) {
        return switch (currentWeather) {
            case "Sunny" -> "80 %";
            case "Rainy" -> "10 %";
            case "Cloudy" -> "40 %";
            case "Cold" -> "20%";
            case "Snowy" -> "10%";
            case "Windy" -> "30%";
            default -> "25 %";
        };
    }

    private void updateDirectory() {
        directoryListView.getItems().clear();
        directoryListView.getItems().add("Plants:");

        long cornCount = controllerForGarden.getPlants().stream().filter(p -> p.getName().equals("Corn")).count();
        long pineappleCount = controllerForGarden.getPlants().stream().filter(p -> p.getName().equals("Pineapple")).count();
        long watermelonCount = controllerForGarden.getPlants().stream().filter(p -> p.getName().equals("Watermelon")).count();
        long carrotCount = controllerForGarden.getPlants().stream().filter(p -> p.getName().equals("Carrot")).count();


        directoryListView.getItems().add("  - Corn: " + cornCount);
        directoryListView.getItems().add("  - Pineapple: " + pineappleCount);
        directoryListView.getItems().add("  - Watermelon: " + watermelonCount);
        directoryListView.getItems().add("  - Carrot: " + carrotCount);

        directoryListView.getItems().add("Good Insects:");

        long beetleCount = controllerForGarden.getInsects().stream().filter(i -> i.getName().equals("Beetle")).count();
        long butterflyCount = controllerForGarden.getInsects().stream().filter(i -> i.getName().equals("Butterfly")).count();

        directoryListView.getItems().add("  - Beetle: " + beetleCount);
        directoryListView.getItems().add("  - Butterfly: " + butterflyCount);

        directoryListView.getItems().add("Pests:");

        long spiderCount = controllerForGarden.getInsects().stream().filter(i -> i.getName().equals("Spider")).count();
        long caterpillarCount = controllerForGarden.getInsects().stream().filter(i -> i.getName().equals("Caterpillar")).count();

        directoryListView.getItems().add("  - Spider: " + spiderCount);
        directoryListView.getItems().add("  - Caterpillar: " + caterpillarCount);

//        plantTable.getItems().clear();
//        plantTable.getItems().addAll(controllerForGarden.getPlants());

    }




    private void animatePlant(Plant plant) {
        javafx.scene.Node node = getNodeByRowColumnIndex(plant.getRow(), plant.getCol(), gardenGrid);
        if (node != null) {
            ScaleTransition st = new ScaleTransition(Duration.millis(500), node);
            st.setByX(1.2);
            st.setByY(1.2);
            st.setCycleCount(2);
            st.setAutoReverse(true);
            st.play();
        }
    }

    private void updateGardenGrid() {
        for (int i = 0; i < 12; i++) {
            for (int j = 0; j < 7; j++) {
                StackPane cell = (StackPane) getNodeByRowColumnIndex(i, j, gardenGrid);
                if (cell != null) {
                    cell.setStyle(cell.getStyle() + " -fx-alignment: center;");
                }
            }
        }

        for (Plant plant : controllerForGarden.getPlants()) {
            StackPane cell = (StackPane) getNodeByRowColumnIndex(plant.getRow(), plant.getCol(), gardenGrid);
            if (!plant.isDead()) {
                ImageView plantImage = getImageView("src/main/images/" + plant.getName().toLowerCase() + ".gif");
                if (plantImage != null) {
                    plantImage.setFitWidth(40);  // Increase plant image size for better visibility
                    plantImage.setFitHeight(40); // Increase plant image size for better visibility
                    StackPane.setAlignment(plantImage, Pos.TOP_CENTER);
                    if (cell != null) {
                        cell.getChildren().clear();
                        cell.getChildren().add(plantImage);
                    }
                }
                Tooltip tooltip = new Tooltip(plant.getName());
                Tooltip.install(cell, tooltip);
            } else {
                Label deadLabel = new Label("X");
                deadLabel.setStyle("-fx-font-size: 24px; -fx-text-fill: brown;");
                if (cell != null && !cell.getChildren().contains(deadLabel)) {
                    cell.getChildren().add(deadLabel);
                    cell.setStyle("-fx-border-color: black; -fx-alignment: center; -fx-background-color: brown;");
                }
            }
        }

        for (Insect insect : controllerForGarden.getInsects()) {
            StackPane cell = (StackPane) getNodeByRowColumnIndex(insect.getRow(), insect.getCol(), gardenGrid);
            if (cell != null && cell.getChildren().size() < 2) { // Ensure only one insect at a time
                ImageView insectImage = getImageView("src/main/images/" + insect.getName().toLowerCase() + ".gif");
                if (insectImage != null) {
                    insectImage.setFitWidth(30);  // Adjust insect image size for better visibility
                    insectImage.setFitHeight(30); // Adjust insect image size for better visibility
                    StackPane.setAlignment(insectImage, Pos.BOTTOM_CENTER);
                    cell.getChildren().add(insectImage);
                    Tooltip tooltip = new Tooltip(insect.getName());
                    Tooltip.install(cell, tooltip);
                    if (insect.isPest()) {
                        cell.setStyle("-fx-border-color: black; -fx-alignment: center; -fx-background-color: red;");
                    } else {
                        cell.setStyle("-fx-border-color: black; -fx-alignment: center; -fx-background-color: blue;");
                    }
                }
            }
        }

        for (Cleaner cleaner : controllerForPest.getCleaners()) {
            if (cleaner.isBusy()) {
                StackPane cell = (StackPane) getNodeByRowColumnIndex(cleaner.getRow(), cleaner.getCol(), gardenGrid);
                ImageView cleanerImage = getImageView("src/main/images/cleaner.png"); // Ensure cleaner image is in your directory
                if (cleanerImage != null) {
                    cleanerImage.setFitWidth(30);
                    cleanerImage.setFitHeight(30);
                    StackPane.setAlignment(cleanerImage, Pos.CENTER);
                    if (cell != null) {
                        cell.getChildren().add(cleanerImage);
                    }
                }
            }
        }
    }

    private boolean isCellEmpty(int row, int col) {
        StackPane cell = (StackPane) getNodeByRowColumnIndex(row, col, gardenGrid);
        if (cell != null) {
            return cell.getChildren().isEmpty();
        }
        return true;
    }

    private Image getImage(String fileName) {
        return imageCache.computeIfAbsent(fileName, key -> {
            try {
                FileInputStream input = new FileInputStream(key);
                return new Image(input);
            } catch (FileNotFoundException e) {
                System.out.println("File is not found: " + key);
                return null;
            }
        });
    }

    private ImageView getImageView(String fileName) {
        Image image = getImage(fileName);
        if (image != null) {
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(30);
            imageView.setFitHeight(30);
            return imageView;
        }
        return null;
    }

    private void updateLog() {
        dayLogList.getItems().clear();
        wateringLogList.getItems().clear();
        heatingLogList.getItems().clear();
        insectLogList.getItems().clear();
        cleanerLogList.getItems().clear();
        fertilizerLogList.getItems().clear();

        addLogEntries(dayLogList, controllerForGarden.getLogger().getDayLogEntries(), Color.BLUE);
        addLogEntries(wateringLogList, controllerForGarden.getLogger().getWateringLogEntries(), Color.GREEN);
        addLogEntries(heatingLogList, controllerForGarden.getLogger().getHeatingLogEntries(), Color.ORANGE);
        addLogEntries(insectLogList, controllerForGarden.getLogger().getInsectLogEntries(), Color.RED);
        addLogEntries(cleanerLogList, controllerForGarden.getLogger().getCleanerLogEntries(), Color.PURPLE);
        addLogEntries(fertilizerLogList, controllerForGarden.getLogger().getFertilizerLogEntries(), Color.YELLOW);
    }

    private void addLogEntries(ListView<TextFlow> logList, List<String> logEntries, Color color) {
        for (String entry : logEntries) {
            String[] parts = entry.split(": ");
            if (parts.length > 1) {
                String timestamp = parts[0];
                String message = parts[1];

                Text timestampText = new Text(timestamp + ": ");
                timestampText.setFill(Color.GRAY);
                timestampText.setFont(Font.font("Arial", FontWeight.BOLD, 14));

                Text messageText = new Text(message + "\n");
                messageText.setFill(color);
                messageText.setFont(Font.font("Arial", FontWeight.NORMAL, 14));

                TextFlow textFlow = new TextFlow(timestampText, messageText);
                textFlow.setPadding(new Insets(5, 10, 5, 10));
                textFlow.setStyle("-fx-background-color: #F0F0F0; -fx-background-radius: 5; -fx-border-color: #D0D0D0; -fx-border-radius: 5; -fx-border-width: 1;");
                logList.getItems().add(textFlow);
            } else {
                Text text = new Text(entry + "\n");
                text.setFill(color);
                text.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
                TextFlow textFlow = new TextFlow(text);
                textFlow.setPadding(new Insets(5, 10, 5, 10));
                textFlow.setStyle("-fx-background-color: #F0F0F0; -fx-background-radius: 5; -fx-border-color: #D0D0D0; -fx-border-radius: 5; -fx-border-width: 1;");
                logList.getItems().add(textFlow);
            }
        }
    }

    private void startSimulation() {
        startSimulationButton.setDisable(true);
        if (controllerForGarden.getDay() == 0) {
            simulateDay();
            simulationTimeline = new Timeline(new KeyFrame(Duration.seconds(4), _ -> simulateDay()));
            simulationTimeline.setCycleCount(Timeline.INDEFINITE);
        }
        simulationTimeline.play();
        pauseSimulationButton.setDisable(false);
    }

    private void pauseSimulation() {
        pauseSimulationButton.setDisable(true);
        simulationTimeline.pause();
        startSimulationButton.setDisable(false);
    }

    private void simulateDay() {
        String weather = controllerForGarden.getCurrentWeather();
        if (weather == null) {
            System.err.println("Error: Weather is null. Defaulting to 'Sunny'.");
            weather = "Sunny"; // Fallback to a default value
        }
        currentWeather = weather;
        controllerForGarden.simulateDay();
        controllerForPest.managePests(controllerForGarden.getPlants(), controllerForGarden.getInsects(), controllerForGarden.getLogger(), controllerForGarden.getDay());

        controllerForFertilizer.manageFertilizers(
                controllerForGarden.getPlants(),
                controllerForGarden.getLogger(),
                controllerForGarden.getDay()
        );
        updateLog();
        updateGardenGrid();
        updateWeatherImage(weather);
        updateDirectory();
        Sensor currentSensor = new Sensor(weather, 10); // Example fixed temperature
        controllerForSensor.generateAnimation(currentSensor, gardenGrid, controllerForGarden.getPlants());
        for (Plant plant : controllerForGarden.getPlants()) {
            plant.adjustLifespanForWeather(weather);
        }

    }

    private void startAnimation() {
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                updateGardenGrid();
            }
        };
        timer.start();
    }

    private static javafx.scene.Node getNodeByRowColumnIndex(final int row, final int column, GridPane gridPane) {
        for (javafx.scene.Node node : gridPane.getChildren()) {
            if (GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == column) {
                return node;
            }
        }
        return null;
    }

    private void updateProgressBars(int temperature) {
        // Update temperature progress bar
        double temperatureProgress = temperature / 100.0; // Assuming temperature ranges from 0 to 100
        temperatureProgressBar.setProgress(temperatureProgress);
        temperatureProgressLabel.setText("Temperature: " + temperature + " F");

        if (temperature <= 50) {
            temperatureProgressBar.setStyle("-fx-accent: blue;");
            temperatureProgressLabel.setTextFill(Color.BLUE);
        } else if (temperature <= 70) {
            temperatureProgressBar.setStyle("-fx-accent: lightblue;");
            temperatureProgressLabel.setTextFill(Color.LIGHTBLUE);
        } else if (temperature <= 90) {
            temperatureProgressBar.setStyle("-fx-accent: orange;");
            temperatureProgressLabel.setTextFill(Color.ORANGE);
        } else {
            temperatureProgressBar.setStyle("-fx-accent: red;");
            temperatureProgressLabel.setTextFill(Color.RED);
        }

        // Update water progress bar based on temperature
        if (temperature <= 50) {
            waterProgressBar.setProgress(0.25);
            waterProgressBar.setStyle("-fx-accent: blue;");
            waterProgressLabel.setText("Water Level: 25%");
            waterProgressLabel.setTextFill(Color.BLUE);
        } else if (temperature <= 70) {
            waterProgressBar.setProgress(0.5);
            waterProgressBar.setStyle("-fx-accent: lightblue;");
            waterProgressLabel.setText("Water Level: 50%");
            waterProgressLabel.setTextFill(Color.LIGHTBLUE);
        } else if (temperature <= 90) {
            waterProgressBar.setProgress(0.75);
            waterProgressBar.setStyle("-fx-accent: orange;");
            waterProgressLabel.setText("Water Level: 75%");
            waterProgressLabel.setTextFill(Color.ORANGE);
        } else {
            waterProgressBar.setProgress(1.0);
            waterProgressBar.setStyle("-fx-accent: red;");
            waterProgressLabel.setText("Water Level: 100%");
            waterProgressLabel.setTextFill(Color.RED);
        }
    }
}
