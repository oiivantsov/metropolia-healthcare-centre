package org.group8.view;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.group8.controller.HealthcenterController;
import org.group8.controller.IControllerForV;
import org.group8.simulator.framework.Trace;

public class HealthcenterGUI extends Application implements IHealthcenterGUI {

    private TextField setTimeField;
    private TextField setDelayField;

    private IControllerForV controller;

    Visualization checkInCanvas;
    Visualization doctorCanvas;
    Visualization labCanvas;
    Visualization xrayCanvas;
    Visualization treatmentCanvas;

    // prob sliders
    // Declare the sliders for probabilities
    Slider labProbabilitySlider;
    Slider xrayProbabilitySlider;
    Slider treatmentProbabilitySlider;

    // Labels to show the probability values
    Label labProbabilityValue;
    Label xrayProbabilityValue;
    Label treatmentProbabilityValue;

    @Override
    public void init() {
        Trace.setTraceLevel(Trace.Level.INFO);
        controller = new HealthcenterController(this);
    }

    @Override
    public void start(Stage primaryStage) {

        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
                Platform.exit();
                System.exit(0);
            }
        });

        primaryStage.setTitle("Health center simulation");

        // Menu Bar
        MenuBar menuBar = new MenuBar();
        Menu fileMenu = new Menu("File");
        Menu editMenu = new Menu("Edit");
        Menu helpMenu = new Menu("Help");
        menuBar.getMenus().addAll(fileMenu, editMenu, helpMenu);

        // How to use option
        MenuItem helpItem = new MenuItem("How to use");
        helpItem.setOnAction(e -> showHelpDialog());
        helpMenu.getItems().add(helpItem);

        // Adding menu items (optional, can be expanded later)
        MenuItem exitMenuItem = new MenuItem("Exit");
        exitMenuItem.setOnAction(e -> Platform.exit());
        fileMenu.getItems().add(exitMenuItem);

        // Upper Section
        setTimeField = new TextField();
        setTimeField.setPromptText("Set Time");

        Button setTimeButton = new Button("Set Time");

        setDelayField = new TextField();
        setDelayField.setPromptText("Set Delay");

        Button setDelayButton = new Button("Set Delay");
        Button speedUpButton = new Button("Speed Up");
        Button speedDownButton = new Button("Speed Down");

        Button runButton = new Button("Run");
        runButton.setDisable(true); // Disable the "Run" button initially
        runButton.setMinWidth(100);

        Button stopButton = new Button("Stop / Resume");
        stopButton.setMinWidth(100);

        Button statisticsButton = new Button("Statistics");
        statisticsButton.setMinWidth(100);

        setTimeField.textProperty().addListener((observable, oldValue, newValue) -> validateInput(runButton));
        setDelayField.textProperty().addListener((observable, oldValue, newValue) -> validateInput(runButton));

        // Event handlers for the buttons
        setTimeButton.setOnAction(e -> controller.setTime((int) getTime()));
        setDelayButton.setOnAction(e -> controller.setDelay(getDelay()));
        speedUpButton.setOnAction(e -> controller.speedUp());
        speedDownButton.setOnAction(e -> controller.slowDown());
        runButton.setOnAction(e -> controller.startSimulation());
        stopButton.setOnAction(e -> {
            if (controller.isRunning()) {
                controller.stopSimulation();
            } else {
                controller.resumeSimulation();
            }
        });
        statisticsButton.setOnAction(e -> controller.showStatistics("Statistics go here"));

        // Layout for the controls
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10, 10, 10, 10));
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        gridPane.add(setTimeField, 0, 0);
        gridPane.add(setTimeButton, 1, 0);
        gridPane.add(setDelayField, 0, 1);
        gridPane.add(setDelayButton, 1, 1);
        gridPane.add(speedUpButton, 2, 1);
        gridPane.add(speedDownButton, 3, 1);
        gridPane.add(runButton, 0, 2);
        gridPane.add(stopButton, 1, 2);
        gridPane.add(statisticsButton, 3, 2);

        // Service Points Section
        Label checkInLabel = new Label("Check-In");
        Label doctorLabel = new Label("Doctor");
        Label labLabel = new Label("Lab");
        Label xrayLabel = new Label("X-Ray");
        Label treatmentLabel = new Label("Treatment");

        // Visualization canvases
        checkInCanvas = new Visualization(150, 450);
        doctorCanvas = new Visualization(150, 450);
        labCanvas = new Visualization(150, 450);
        xrayCanvas = new Visualization(150, 450);
        treatmentCanvas = new Visualization(150, 450);

        // VBox for each service point
        VBox checkInBox = new VBox(10, checkInLabel, checkInCanvas);
        VBox doctorBox = new VBox(10, doctorLabel, doctorCanvas);
        VBox labBox = new VBox(10, labLabel, labCanvas);
        VBox xrayBox = new VBox(10, xrayLabel, xrayCanvas);
        VBox treatmentBox = new VBox(10, treatmentLabel, treatmentCanvas);

        checkInBox.setAlignment(Pos.CENTER);
        doctorBox.setAlignment(Pos.CENTER);
        labBox.setAlignment(Pos.CENTER);
        xrayBox.setAlignment(Pos.CENTER);
        treatmentBox.setAlignment(Pos.CENTER);

        // HBox to hold all the service point boxes
        HBox servicePointsBox = new HBox(40);
        servicePointsBox.setPadding(new Insets(10));
        servicePointsBox.setAlignment(Pos.CENTER);
        servicePointsBox.getChildren().addAll(checkInBox, doctorBox, labBox, xrayBox, treatmentBox);

        // ---------------- Probabilities sliders ----------------

        // Probability Sliders
        labProbabilitySlider = new Slider(0, 1, 0.33);
        xrayProbabilitySlider = new Slider(0, 1, 0.33);
        treatmentProbabilitySlider = new Slider(0, 1, 0.33);

        labProbabilitySlider.setShowTickLabels(true);
        labProbabilitySlider.setShowTickMarks(true);
        xrayProbabilitySlider.setShowTickLabels(true);
        xrayProbabilitySlider.setShowTickMarks(true);
        treatmentProbabilitySlider.setShowTickLabels(true);
        treatmentProbabilitySlider.setShowTickMarks(true);

        // Initialize the probability labels
        labProbabilityValue = new Label("Lab: " + String.format("%.2f", labProbabilitySlider.getValue()));
        xrayProbabilityValue = new Label("X-Ray: " + String.format("%.2f", xrayProbabilitySlider.getValue()));
        treatmentProbabilityValue = new Label("Treatment: " + String.format("%.2f", treatmentProbabilitySlider.getValue()));

        // Add listeners to sliders to update the labels
        labProbabilitySlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            updateProbabilityValues();
        });

        xrayProbabilitySlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            updateProbabilityValues();
        });

        treatmentProbabilitySlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            updateProbabilityValues();
        });


        VBox probabilityBox = new VBox(10,
                new HBox(10, new Label("Lab Probability"), labProbabilitySlider, labProbabilityValue),
                new HBox(10, new Label("X-Ray Probability"), xrayProbabilitySlider, xrayProbabilityValue),
                new HBox(10, new Label("Treatment Probability"), treatmentProbabilitySlider, treatmentProbabilityValue)
        );

        probabilityBox.setAlignment(Pos.CENTER);

        // Button to add probabilities
        Button applyProbabilitiesButton = new Button("Apply Probabilities");
        applyProbabilitiesButton.setOnAction(e -> {
            double labProbability = labProbabilitySlider.getValue();
            double xrayProbability = xrayProbabilitySlider.getValue();
            double treatmentProbability = treatmentProbabilitySlider.getValue();

            controller.setProbabilities(labProbability, xrayProbability, treatmentProbability);
        });

        // layout for box with button
        VBox applyProbButtonBox = new VBox(10,
                new Label("Apply Probabilities"),
                new Label("After Doctor Visit")
        );
        applyProbButtonBox.getChildren().addAll(applyProbabilitiesButton);
        applyProbButtonBox.setAlignment(Pos.TOP_CENTER);

        // Hbox for sliders and apply button
        HBox probBox = new HBox(10, probabilityBox, applyProbButtonBox);

        // --------------------- grid + probBox as Horizontal box ---------------------
        HBox gridAndProbBox = new HBox();
        gridAndProbBox.setSpacing(150);
        gridAndProbBox.setPadding(new Insets(40, 10, 30, 10));
        gridAndProbBox.setAlignment(Pos.CENTER);
        gridAndProbBox.getChildren().addAll(gridPane, probBox);

        // --------------------- Main layout ---------------------
        VBox mainLayout = new VBox();
        mainLayout.getChildren().addAll(menuBar, gridAndProbBox, servicePointsBox);

        Scene scene = new Scene(mainLayout, 1400, 800);

        // Apply CSS styling
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.show();

        // Request focus for the main layout
        Platform.runLater(mainLayout::requestFocus);
    }

    @Override
    public IVisualization getCheckInCanvas() {
        return checkInCanvas;
    }

    @Override
    public IVisualization getDoctorCanvas() {
        return doctorCanvas;
    }

    @Override
    public IVisualization getLabCanvas() {
        return labCanvas;
    }

    @Override
    public IVisualization getXrayCanvas() {
        return xrayCanvas;
    }

    @Override
    public IVisualization getTreatmentCanvas() {
        return treatmentCanvas;
    }

    @Override
    public double getTime() {
        return Double.parseDouble(setTimeField.getText());
    }

    @Override
    public long getDelay() {
        return Long.parseLong(setDelayField.getText());
    }  // Method to get the delay

    @Override
    public void showStatistics(String statistics) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Statistics");
        alert.setHeaderText(null);
        alert.setContentText(statistics);
        alert.showAndWait();
    }

    @Override
    public void clearDisplays() {
        // Clear all the visualization canvases
        for (IVisualization v : new Visualization[]{checkInCanvas, doctorCanvas, labCanvas, xrayCanvas, treatmentCanvas}) {
            v.clearDisplay();
        }
    }

    public void showHelpDialog() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Help");
        alert.setHeaderText("Health Center Simulation Guide");

        String helpText = """
        Welcome to the Health Center Simulation!
        
        This simulation represents the workflow of a health center with five service points:
        - Check-In
        - Doctor
        - Lab
        - X-Ray
        - Treatment
        
        Use the buttons and input fields to control the simulation:
        
        1. **Set Time**: Define the total duration of the simulation in the 'Set Time' field and click 'Set Time'.
        2. **Set Delay**: Set the delay between actions in the 'Set Delay' field and click 'Set Delay'.
        3. **Speed Up / Speed Down**: Adjust the simulation speed using the 'Speed Up' and 'Speed Down' buttons.
        4. **Run**: Start the simulation by clicking 'Run' (after setting the time and delay).
        5. **Stop / Resume**: Pause or resume the simulation by clicking 'Stop / Resume'.
        6. **Statistics**: Display simulation statistics by clicking the 'Statistics' button.
        
        Additional Features:
        - You can adjust probabilities for patient routing after doctor visits using the sliders for Lab, X-Ray, and Treatment.
        - These probabilities must sum to 1, and the values will be adjusted automatically to ensure correctness.
        
        For more information, consult the documentation or reach out to support.
    """;

        alert.setContentText(helpText);
        alert.showAndWait();
    }

    private void updateProbabilityValues() {
        double total = labProbabilitySlider.getValue() + xrayProbabilitySlider.getValue() + treatmentProbabilitySlider.getValue();

        if (total > 1) {
            // Prevent setting the total probability above 1
            double excess = total - 1;

            // Adjust the slider values proportionally
            labProbabilitySlider.setValue(labProbabilitySlider.getValue() - excess / 3);
            xrayProbabilitySlider.setValue(xrayProbabilitySlider.getValue() - excess / 3);
            treatmentProbabilitySlider.setValue(treatmentProbabilitySlider.getValue() - excess / 3);
        }

        // Update labels
        labProbabilityValue.setText("Lab: " + String.format("%.2f", labProbabilitySlider.getValue()));
        xrayProbabilityValue.setText("X-Ray: " + String.format("%.2f", xrayProbabilitySlider.getValue()));
        treatmentProbabilityValue.setText("Treatment: " + String.format("%.2f", treatmentProbabilitySlider.getValue()));
    }

    // Helper method to validate input and enable/disable the Run button
    private void validateInput(Button runButton) {
        boolean isTimeValid = isValidDouble(setTimeField.getText());
        boolean isDelayValid = isValidLong(setDelayField.getText());

        // Enable the "Run" button only if both time and delay fields are valid
        runButton.setDisable(!(isTimeValid && isDelayValid));
    }

    // Helper methods to check if the input is a valid number
    private boolean isValidDouble(String input) {
        try {
            double value = Double.parseDouble(input);
            return value > 0; // Ensure time is greater than 0
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean isValidLong(String input) {
        try {
            long value = Long.parseLong(input);
            return value > 0; // Ensure delay is greater than 0
        } catch (NumberFormatException e) {
            return false;
        }
    }
}

