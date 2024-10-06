package org.group8.view;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.group8.controller.HealthcenterController;
import org.group8.controller.IControllerForV;
import org.group8.simulator.framework.Trace;
import org.group8.simulator.model.AverageTime;
import javafx.stage.Modality;

public class HealthcenterGUI extends Application implements IHealthcenterGUI {

    private static final int CANVAS_WIDTH = 150;
    private static final int CANVAS_HEIGHT = 450;

    private IControllerForV controller;

    // time and delay fields
    private TextField setTimeField;
    private TextField setDelayField;

    // buttons
    private Button runButton;
    private Button stopButton;
    private Button updateTimeButton;
    private Button updateDelayButton;
    private Button speedUpButton;
    private Button speedDownButton;
    private Button statisticsButton;

    // displays for service points
    private Visualization checkInCanvas;
    private Visualization doctorCanvas;
    private Visualization labCanvas;
    private Visualization xrayCanvas;
    private Visualization treatmentCanvas;

    // probability sliders
    private Slider labProbabilitySlider;
    private Slider xrayProbabilitySlider;
    private Slider treatmentProbabilitySlider;

    // Labels to show the probability values
    private Label labProbabilityValue;
    private Label xrayProbabilityValue;
    private Label treatmentProbabilityValue;

    // styles
    private static final String BLACK_THEME_CSS = "/black-theme.css";
    private static final String WHITE_THEME_CSS = "/white-theme.css";
    private boolean isBlackTheme = true;

    @Override
    public void init() {
        Trace.setTraceLevel(Trace.Level.INFO);
        controller = new HealthcenterController(this);
    }

    @Override
    public void start(Stage primaryStage) {
        setupPrimaryStage(primaryStage);
        MenuBar menuBar = setupMenuBar();
        HBox gridAndProbBox = setupControlPanel();
        HBox servicePointsBox = setupServicePoints();
        VBox mainLayout = new VBox(menuBar, gridAndProbBox, servicePointsBox);
        Scene scene = new Scene(mainLayout, 1400, 800);
        applyTheme(scene, isBlackTheme);

        primaryStage.setScene(scene);
        primaryStage.show();
        Platform.runLater(mainLayout::requestFocus);
    }

    private void setupPrimaryStage(Stage primaryStage) {
        primaryStage.setOnCloseRequest(event -> {
            Platform.exit();
            System.exit(0);
        });
        primaryStage.setTitle("Health Center Simulation");
    }

    private MenuBar setupMenuBar() {
        MenuBar menuBar = new MenuBar();
        Menu fileMenu = new Menu("File");
        Menu editMenu = new Menu("Config");
        Menu helpMenu = new Menu("Help");
        Menu resultsMenu = new Menu("Results");

        MenuItem resultsItem = new MenuItem("Results");
        resultsItem.setOnAction(e -> showResultsDialog());
        resultsMenu.getItems().add(resultsItem);

        // "How to use" menu item
        MenuItem helpItem = new MenuItem("How to use");
        helpItem.setOnAction(e -> showHelpDialog());
        helpMenu.getItems().add(helpItem);

        // "Edit Average Times" menu item
        MenuItem editConfigItem = new MenuItem("Edit Average Times");
        editConfigItem.setOnAction(e -> showEditConfigDialog());
        editMenu.getItems().add(editConfigItem);

        // "Edit Probabilities" menu item
        MenuItem editProbabilitiesItem = new MenuItem("Edit Probabilities");
        editProbabilitiesItem.setOnAction(e -> showEditProbabilitiesDialog());
        editMenu.getItems().add(editProbabilitiesItem);

        // "Toggle Theme" menu item
        MenuItem toggleThemeItem = new MenuItem("Black / White Theme");
        toggleThemeItem.setOnAction(e -> {
            isBlackTheme = !isBlackTheme; // Toggle theme flag
            applyTheme(menuBar.getScene(), isBlackTheme); // Apply the new theme to the scene
        });
        fileMenu.getItems().add(toggleThemeItem); // Add the toggle theme item to the file menu

        // "Exit" menu item
        MenuItem exitMenuItem = new MenuItem("Exit");
        exitMenuItem.setOnAction(e -> Platform.exit());
        fileMenu.getItems().add(exitMenuItem);

        // Add menus to menu bar
        menuBar.getMenus().addAll(fileMenu, editMenu, helpMenu, resultsMenu);
        return menuBar;
    }


    private void showEditConfigDialog() {
        Stage dialog = new Stage();
        // Block events to other windows
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Edit Average Time Config");

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.setHgap(10);
        grid.setVgap(10);

        // current average times in db
        double arrivalTime = controller.getAverageTime("arrival");
        double checkInTime = controller.getAverageTime("check-in");
        double doctorTime = controller.getAverageTime("doctor");
        double labTime = controller.getAverageTime("lab");
        double xRayTime = controller.getAverageTime("xray");
        double treatmentTime = controller.getAverageTime("treatment");

        // text fields for average times
        TextField arrivalField = new TextField(String.valueOf(arrivalTime));
        TextField checkInField = new TextField(String.valueOf(checkInTime));
        TextField doctorField = new TextField(String.valueOf(doctorTime));
        TextField labField = new TextField(String.valueOf(labTime));
        TextField xRayField = new TextField(String.valueOf(xRayTime));
        TextField treatmentField = new TextField(String.valueOf(treatmentTime));

        grid.add(new Label("Average Arrival Time:"), 0, 0);
        grid.add(arrivalField, 1, 0);
        grid.add(new Label("Average Check-In Time:"), 0, 1);
        grid.add(checkInField, 1, 1);
        grid.add(new Label("Average Doctor Time:"), 0, 2);
        grid.add(doctorField, 1, 2);
        grid.add(new Label("Average Lab Time:"), 0, 3);
        grid.add(labField, 1, 3);
        grid.add(new Label("Average X-Ray Time:"), 0, 4);
        grid.add(xRayField, 1, 4);
        grid.add(new Label("Average Treatment Time:"), 0, 5);
        grid.add(treatmentField, 1, 5);

        Button saveButton = new Button("Save");
        saveButton.setPadding(new Insets(10, 20, 10, 20));
        saveButton.setDisable(true); // Disable save button initially

        // Add listeners to validate input fields
        arrivalField.textProperty().addListener((observable, oldValue, newValue) -> validateFields(saveButton, checkInField, doctorField, labField, xRayField, treatmentField, arrivalField));
        checkInField.textProperty().addListener((observable, oldValue, newValue) -> validateFields(saveButton, checkInField, doctorField, labField, xRayField, treatmentField, arrivalField));
        doctorField.textProperty().addListener((observable, oldValue, newValue) -> validateFields(saveButton, checkInField, doctorField, labField, xRayField, treatmentField, arrivalField));
        labField.textProperty().addListener((observable, oldValue, newValue) -> validateFields(saveButton, checkInField, doctorField, labField, xRayField, treatmentField, arrivalField));
        xRayField.textProperty().addListener((observable, oldValue, newValue) -> validateFields(saveButton, checkInField, doctorField, labField, xRayField, treatmentField, arrivalField));
        treatmentField.textProperty().addListener((observable, oldValue, newValue) -> validateFields(saveButton, checkInField, doctorField, labField, xRayField, treatmentField, arrivalField));

        saveButton.setOnAction(e -> {
            double avgCheckIn = Double.parseDouble(checkInField.getText());
            double avgDoctor = Double.parseDouble(doctorField.getText());
            double avgLab = Double.parseDouble(labField.getText());
            double avgXRay = Double.parseDouble(xRayField.getText());
            double avgTreatment = Double.parseDouble(treatmentField.getText());
            double avgArrival = Double.parseDouble(arrivalField.getText());

            controller.setAverageTimes(avgCheckIn, avgDoctor, avgLab, avgXRay, avgTreatment, avgArrival);
            dialog.close();
        });

        VBox layout = new VBox(10, grid, saveButton);
        layout.setPadding(new Insets(10));
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout, 480, 400);
        applyTheme(scene, isBlackTheme);
        dialog.setScene(scene);
        dialog.showAndWait();
    }

    private void validateFields(Button saveButton, TextField... fields) {
        boolean allValid = true;
        for (TextField field : fields) {
            if (!isValidDouble(field.getText())) {
                allValid = false;
                break;
            }
        }
        saveButton.setDisable(!allValid);
    }

    private void showEditProbabilitiesDialog() {
        Stage dialog = new Stage();
        // Block events to other windows
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Edit Probabilities");

        labProbabilitySlider = createProbabilitySlider("Lab Probability", "LAB");
        xrayProbabilitySlider = createProbabilitySlider("X-Ray Probability", "XRAY");
        treatmentProbabilitySlider = createProbabilitySlider("Treatment Probability", "TREATMENT");

        labProbabilityValue = new Label("Lab: " + String.format("%.2f", labProbabilitySlider.getValue()));
        xrayProbabilityValue = new Label("X-Ray: " + String.format("%.2f", xrayProbabilitySlider.getValue()));
        treatmentProbabilityValue = new Label("Treatment: " + String.format("%.2f", treatmentProbabilitySlider.getValue()));

        labProbabilitySlider.valueProperty().addListener((observable, oldValue, newValue) -> updateProbabilityValues());
        xrayProbabilitySlider.valueProperty().addListener((observable, oldValue, newValue) -> updateProbabilityValues());
        treatmentProbabilitySlider.valueProperty().addListener((observable, oldValue, newValue) -> updateProbabilityValues());

        Button applyProbabilitiesButton = new Button("Apply Probabilities");

        applyProbabilitiesButton.setOnAction(e -> {
                    double labProbability = labProbabilitySlider.getValue();
                    double xrayProbability = xrayProbabilitySlider.getValue();
                    double treatmentProbability = treatmentProbabilitySlider.getValue();
                    controller.setProbabilities(labProbability, xrayProbability, treatmentProbability);
                    dialog.close();
                }
        );

        VBox layout = new VBox(10);

        HBox labProbabilityBox = new HBox(10, new Label("Lab Probability:"), labProbabilitySlider, labProbabilityValue);
        labProbabilityBox.setAlignment(Pos.CENTER);

        HBox xrayProbabilityBox = new HBox(10, new Label("X-Ray Probability:"), xrayProbabilitySlider, xrayProbabilityValue);
        xrayProbabilityBox.setAlignment(Pos.CENTER);

        HBox treatmentProbabilityBox = new HBox(10, new Label("Treatment Probability:"), treatmentProbabilitySlider, treatmentProbabilityValue);
        treatmentProbabilityBox.setAlignment(Pos.CENTER);

        layout.getChildren().addAll(
                labProbabilityBox,
                xrayProbabilityBox,
                treatmentProbabilityBox,
                applyProbabilitiesButton
        );

        layout.setPadding(new Insets(10));
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout, 700, 300);
        applyTheme(scene, isBlackTheme);
        dialog.setScene(scene);
        dialog.showAndWait();
    }

    private HBox setupControlPanel() {
        setTimeField = new TextField();
        setTimeField.setPromptText("Set Time");
        updateTimeButton = new Button("Update Time");

        setDelayField = new TextField();
        setDelayField.setPromptText("Set Delay");
        updateDelayButton = new Button("Update Delay");

        speedUpButton = new Button("Speed Up");
        speedDownButton = new Button("Speed Down");

        runButton = new Button("Start / Restart");
        runButton.setMinWidth(100);
        runButton.getStyleClass().add("run-button");

        stopButton = new Button("Stop / Resume");
        stopButton.setMinWidth(100);

        statisticsButton = new Button("Statistics");
        statisticsButton.setMinWidth(100);

        disableInitialButtons();
        addInputValidationListeners();

        setupEventHandlers();

        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10));
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.add(setTimeField, 0, 0);
        gridPane.add(updateTimeButton, 1, 0);
        gridPane.add(setDelayField, 0, 1);
        gridPane.add(updateDelayButton, 1, 1);
        gridPane.add(speedUpButton, 2, 1);
        gridPane.add(speedDownButton, 3, 1);
        gridPane.add(runButton, 0, 2);
        gridPane.add(stopButton, 1, 2);
        gridPane.add(statisticsButton, 3, 2);

        HBox gridAndProbBox = new HBox();
        gridAndProbBox.setSpacing(150);
        gridAndProbBox.setPadding(new Insets(40, 10, 30, 10));
        gridAndProbBox.setAlignment(Pos.CENTER);
        gridAndProbBox.getChildren().addAll(gridPane);

        return gridAndProbBox;
    }

    private Slider createProbabilitySlider(String labelText, String decisionType) {
        double probability = controller.getProbability(decisionType);
        Slider slider = new Slider(0, 1, probability);
        slider.setShowTickLabels(true);
        slider.setShowTickMarks(true);
        return slider;
    }

    private HBox setupServicePoints() {
        checkInCanvas = new Visualization(CANVAS_WIDTH, CANVAS_HEIGHT);
        doctorCanvas = new Visualization(CANVAS_WIDTH, CANVAS_HEIGHT);
        labCanvas = new Visualization(CANVAS_WIDTH, CANVAS_HEIGHT);
        xrayCanvas = new Visualization(CANVAS_WIDTH, CANVAS_HEIGHT);
        treatmentCanvas = new Visualization(CANVAS_WIDTH, CANVAS_HEIGHT);

        VBox checkInBox = createServicePointBox("Check-In", checkInCanvas);
        VBox doctorBox = createServicePointBox("Doctor", doctorCanvas);
        VBox labBox = createServicePointBox("Lab", labCanvas);
        VBox xrayBox = createServicePointBox("X-Ray", xrayCanvas);
        VBox treatmentBox = createServicePointBox("Treatment", treatmentCanvas);

        HBox servicePointsBox = new HBox(40);
        servicePointsBox.setPadding(new Insets(10));
        servicePointsBox.setAlignment(Pos.CENTER);

        servicePointsBox.getChildren().addAll(checkInBox, doctorBox, labBox, xrayBox, treatmentBox);

        return servicePointsBox;
    }

    private VBox createServicePointBox(String labelText, Visualization canvas) {
        Label label = new Label(labelText);
        label.getStyleClass().add("service-point-label"); // Add CSS class to label

        VBox vbox = new VBox(10);
        vbox.getChildren().addAll(label, canvas);
        vbox.setAlignment(Pos.CENTER);

        return vbox;
    }


    private void setupEventHandlers() {
        updateTimeButton.setOnAction(e -> updateSimulationTime());
        updateDelayButton.setOnAction(e -> updateSimulationDelay());
        speedUpButton.setOnAction(e -> controller.speedUp());
        speedDownButton.setOnAction(e -> controller.slowDown());
        runButton.setOnAction(e -> startSimulation());
        stopButton.setOnAction(e -> toggleSimulation());
        statisticsButton.setOnAction(e -> controller.showStatistics("Statistics go here"));
    }

    private void updateSimulationTime() {
        controller.setTime((int) getTime());
    }

    private void updateSimulationDelay() {
        controller.setDelay(getDelay());
    }

    private void startSimulation() {
        controller.startSimulation();
        activateButtons();
    }

    private void toggleSimulation() {
        if (controller.isRunning()) {
            controller.stopSimulation();
        } else {
            controller.resumeSimulation();
        }
    }

    private void disableInitialButtons() {
        runButton.setDisable(true);
        stopButton.setDisable(true);
        updateTimeButton.setDisable(true);
        updateDelayButton.setDisable(true);
        speedUpButton.setDisable(true);
        speedDownButton.setDisable(true);
        statisticsButton.setDisable(true);
    }

    private void addInputValidationListeners() {
        setTimeField.textProperty().addListener((observable, oldValue, newValue) -> validateInput());
        setDelayField.textProperty().addListener((observable, oldValue, newValue) -> validateInput());
    }

    private void validateInput() {
        boolean isTimeValid = isValidDouble(setTimeField.getText());
        boolean isDelayValid = isValidLong(setDelayField.getText());
        runButton.setDisable(!(isTimeValid && isDelayValid));
    }

    private boolean isValidDouble(String input) {
        try {
            double value = Double.parseDouble(input);
            return value > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean isValidLong(String input) {
        try {
            long value = Long.parseLong(input);
            return value >= 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private void activateButtons() {
        stopButton.setDisable(false);
        speedUpButton.setDisable(false);
        updateTimeButton.setDisable(false);
        updateDelayButton.setDisable(false);
        speedDownButton.setDisable(false);
        statisticsButton.setDisable(false);
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
    }

    @Override
    public void showStatistics(String statistics) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Statistics");
        alert.setHeaderText("Simulation Statistics");

        String formattedStatistics = """
        %s
        """.formatted(statistics);

        // Set a custom width for the Alert
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setPrefWidth(600); // Set the preferred width for the dialog pane
        applyTheme(alert.getDialogPane().getScene(), isBlackTheme); // Apply theme to the dialog

        alert.setContentText(formattedStatistics);
        alert.showAndWait();
    }


    @Override
    public void clearDisplays() {
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

            Use the following controls to manage the simulation:

            1. **Set Time**: Define the total duration of the simulation using the 'Set Time' field, click 'Update Time' to update it after the simulation has started.
            2. **Set Delay**: Set the delay between actions using the 'Set Delay' field, click 'Update Delay' to update it after the simulation has started.
            3. **Speed Up / Speed Down**: Increase or decrease the simulation speed using the 'Speed Up' and 'Speed Down' buttons.
            4. **Run**: Start or restart the simulation by clicking 'Run' (after setting the time and delay).
            5. **Stop / Resume**: Pause or resume the simulation by clicking 'Stop / Resume'.
            6. **Statistics**: Display the simulation statistics by clicking the 'Statistics' button.

            Configuration Options:
            - **Edit Average Times**: You can adjust the average time for each service point (e.g., Check-In, Doctor, Lab, etc.) by selecting 'Edit Average Times' from the 'Config' menu. Ensure that all values are positive numbers.
            - **Edit Probabilities**: Adjust the probabilities for patient routing after doctor visits (e.g., Lab, X-Ray, Treatment) by selecting 'Edit Probabilities' from the 'Config' menu. Ensure the total of all probabilities does not exceed 1.

            Additional Features:
            - Each probability for Lab, X-Ray, and Treatment can be individually adjusted, and they should sum to 1. If the total exceeds 1, adjustments are required.
            - The visual representation on the screen will show patient progress through the different stages of the health center.

            For more detailed information, consult the documentation or reach out to support.
            """;

        // Set a custom width for the Alert
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setPrefWidth(600);
        applyTheme(alert.getDialogPane().getScene(), isBlackTheme);

        alert.setContentText(helpText);
        alert.showAndWait();
    }

    public void showResultsDialog(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Show Results");
        alert.setHeaderText("Results from previous simulations ");

        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setPrefHeight(400);
        dialogPane.setPrefWidth(600);
        applyTheme(alert.getDialogPane().getScene(), isBlackTheme);

        alert.setContentText("asd");
        alert.showAndWait();
        
    }

    private void updateProbabilityValues() {
        double total = labProbabilitySlider.getValue() + xrayProbabilitySlider.getValue() + treatmentProbabilitySlider.getValue();

        if (total > 1) {
            double excess = total - 1;
            labProbabilitySlider.setValue(labProbabilitySlider.getValue() - excess / 3);
            xrayProbabilitySlider.setValue(xrayProbabilitySlider.getValue() - excess / 3);
            treatmentProbabilitySlider.setValue(treatmentProbabilitySlider.getValue() - excess / 3);
        }

        labProbabilityValue.setText("Lab: " + String.format("%.2f", labProbabilitySlider.getValue()));
        xrayProbabilityValue.setText("X-Ray: " + String.format("%.2f", xrayProbabilitySlider.getValue()));
        treatmentProbabilityValue.setText("Treatment: " + String.format("%.2f", treatmentProbabilitySlider.getValue()));
    }

    private void applyTheme(Scene scene, boolean isBlackTheme) {
        scene.getStylesheets().clear();
        if (isBlackTheme) {
            scene.getStylesheets().add(getClass().getResource(BLACK_THEME_CSS).toExternalForm());
        } else {
            scene.getStylesheets().add(getClass().getResource(WHITE_THEME_CSS).toExternalForm());
        }
    }

    @Override
    public void showSimulationEndAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Simulation Ended");
        alert.setHeaderText(null);
        alert.setContentText("The simulation has ended successfully.");

        // Set a custom width for the Alert (optional)
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setPrefWidth(400);
        applyTheme(dialogPane.getScene(), isBlackTheme);

        alert.showAndWait();
    }
}
