package org.group8.view;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.stage.Modality;
import org.group8.controller.DataController;
import org.group8.controller.HealthcenterController;
import org.group8.controller.IControllerForV;
import org.group8.controller.IDataControlller;
import org.group8.simulator.framework.Trace;
import org.group8.simulator.model.SimulationResults;

import java.util.List;

public class HealthcenterGUI extends Application implements IHealthcenterGUI {

    private static final int CANVAS_WIDTH = 180;
    private static final int CANVAS_HEIGHT = 450;

    private IControllerForV controller;
    private IDataControlller dataController;

    // time and delay fields
    private TextField setTimeField;
    private TextField setDelayField;

    // status label
    private Label statusLabel;

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
        dataController = new DataController();
    }

    @Override
    public void start(Stage primaryStage) {
        setupPrimaryStage(primaryStage);
        MenuBar menuBar = setupMenuBar();
        VBox controlPanel = setupControlPanel();
        HBox servicePointsBox = setupServicePoints();
        VBox mainLayout = new VBox(menuBar, controlPanel, servicePointsBox);
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
        MenuItem editConfigItem = new MenuItem("Edit Distributions");
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
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Edit Distributions");

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);

        // Current average times and distributions in DB
        double arrivalTime = dataController.getAverageTime("arrival");
        double checkInTime = dataController.getAverageTime("check-in");
        double doctorTime = dataController.getAverageTime("doctor");
        double labTime = dataController.getAverageTime("lab");
        double xRayTime = dataController.getAverageTime("xray");
        double treatmentTime = dataController.getAverageTime("treatment");

        String arrivalDistribution = dataController.getDistribution("arrival");
        String checkInDistribution = dataController.getDistribution("check-in");
        String doctorDistribution = dataController.getDistribution("doctor");
        String labDistribution = dataController.getDistribution("lab");
        String xRayDistribution = dataController.getDistribution("xray");
        String treatmentDistribution = dataController.getDistribution("treatment");

        // Create an observable list of distribution options
        ObservableList<String> distributionOptions = FXCollections.observableArrayList("negexp", "poisson");

        // Create labels for service points
        Label arrivalLabel = new Label("Arrival");
        Label checkInLabel = new Label("Check-In");
        Label doctorLabel = new Label("Doctor");
        Label labLabel = new Label("Lab");
        Label xRayLabel = new Label("X-Ray");
        Label treatmentLabel = new Label("Treatment");

        // Create text fields for average times
        TextField arrivalField = new TextField(String.valueOf(arrivalTime));
        TextField checkInField = new TextField(String.valueOf(checkInTime));
        TextField doctorField = new TextField(String.valueOf(doctorTime));
        TextField labField = new TextField(String.valueOf(labTime));
        TextField xRayField = new TextField(String.valueOf(xRayTime));
        TextField treatmentField = new TextField(String.valueOf(treatmentTime));

        // Create combo boxes for distributions
        ComboBox<String> arrivalComboBox = new ComboBox<>(distributionOptions);
        arrivalComboBox.setValue(arrivalDistribution);
        ComboBox<String> checkInComboBox = new ComboBox<>(distributionOptions);
        checkInComboBox.setValue(checkInDistribution);
        ComboBox<String> doctorComboBox = new ComboBox<>(distributionOptions);
        doctorComboBox.setValue(doctorDistribution);
        ComboBox<String> labComboBox = new ComboBox<>(distributionOptions);
        labComboBox.setValue(labDistribution);
        ComboBox<String> xRayComboBox = new ComboBox<>(distributionOptions);
        xRayComboBox.setValue(xRayDistribution);
        ComboBox<String> treatmentComboBox = new ComboBox<>(distributionOptions);
        treatmentComboBox.setValue(treatmentDistribution);

        // Add labels and fields to grid
        grid.add(new Label("Distribution"), 1, 0);
        grid.add(new Label("Average Time"), 2, 0);

        // Add combo boxes and text fields
        grid.add(arrivalLabel, 0, 1);
        grid.add(arrivalComboBox, 1, 1);
        grid.add(arrivalField, 2, 1);

        grid.add(checkInLabel, 0, 2);
        grid.add(checkInComboBox, 1, 2);
        grid.add(checkInField, 2, 2);

        grid.add(doctorLabel, 0, 3);
        grid.add(doctorComboBox, 1, 3);
        grid.add(doctorField, 2, 3);

        grid.add(labLabel, 0, 4);
        grid.add(labComboBox, 1, 4);
        grid.add(labField, 2, 4);

        grid.add(xRayLabel, 0, 5);
        grid.add(xRayComboBox, 1, 5);
        grid.add(xRayField, 2, 5);

        grid.add(treatmentLabel, 0, 6);
        grid.add(treatmentComboBox, 1, 6);
        grid.add(treatmentField, 2, 6);

        // Create save button
        Button saveButton = new Button("Save");
        saveButton.setPadding(new Insets(10, 20, 10, 20));
        saveButton.setDisable(true); // Disable save button initially

        // Create Set Default button
        Button setDefaultButton = new Button("Set Default");
        setDefaultButton.setPadding(new Insets(10, 20, 10, 20));

        // Add listeners to validate input fields and enable save button when any field changes
        arrivalField.textProperty().addListener((observable, oldValue, newValue) -> validateAndEnableSaveButton(saveButton, arrivalField, checkInField, doctorField, labField, xRayField, treatmentField));
        checkInField.textProperty().addListener((observable, oldValue, newValue) -> validateAndEnableSaveButton(saveButton, arrivalField, checkInField, doctorField, labField, xRayField, treatmentField));
        doctorField.textProperty().addListener((observable, oldValue, newValue) -> validateAndEnableSaveButton(saveButton, arrivalField, checkInField, doctorField, labField, xRayField, treatmentField));
        labField.textProperty().addListener((observable, oldValue, newValue) -> validateAndEnableSaveButton(saveButton, arrivalField, checkInField, doctorField, labField, xRayField, treatmentField));
        xRayField.textProperty().addListener((observable, oldValue, newValue) -> validateAndEnableSaveButton(saveButton, arrivalField, checkInField, doctorField, labField, xRayField, treatmentField));
        treatmentField.textProperty().addListener((observable, oldValue, newValue) -> validateAndEnableSaveButton(saveButton, arrivalField, checkInField, doctorField, labField, xRayField, treatmentField));

        arrivalComboBox.valueProperty().addListener((observable, oldValue, newValue) -> saveButton.setDisable(false));
        checkInComboBox.valueProperty().addListener((observable, oldValue, newValue) -> saveButton.setDisable(false));
        doctorComboBox.valueProperty().addListener((observable, oldValue, newValue) -> saveButton.setDisable(false));
        labComboBox.valueProperty().addListener((observable, oldValue, newValue) -> saveButton.setDisable(false));
        xRayComboBox.valueProperty().addListener((observable, oldValue, newValue) -> saveButton.setDisable(false));
        treatmentComboBox.valueProperty().addListener((observable, oldValue, newValue) -> saveButton.setDisable(false));

        saveButton.setOnAction(e -> {
            double avgCheckIn = Double.parseDouble(checkInField.getText());
            double avgDoctor = Double.parseDouble(doctorField.getText());
            double avgLab = Double.parseDouble(labField.getText());
            double avgXRay = Double.parseDouble(xRayField.getText());
            double avgTreatment = Double.parseDouble(treatmentField.getText());
            double avgArrival = Double.parseDouble(arrivalField.getText());

            String distCheckIn = checkInComboBox.getValue();
            String distDoctor = doctorComboBox.getValue();
            String distLab = labComboBox.getValue();
            String distXRay = xRayComboBox.getValue();
            String distTreatment = treatmentComboBox.getValue();
            String distArrival = arrivalComboBox.getValue();

            dataController.updateDistribution("check-in", distCheckIn, avgCheckIn);
            dataController.updateDistribution("doctor", distDoctor, avgDoctor);
            dataController.updateDistribution("lab", distLab, avgLab);
            dataController.updateDistribution("xray", distXRay, avgXRay);
            dataController.updateDistribution("treatment", distTreatment, avgTreatment);
            dataController.updateDistribution("arrival", distArrival, avgArrival);

            dialog.close();
        });

        setDefaultButton.setOnAction(e -> {
            dataController.setDefaultDistributions();
            refreshDistributionDialogFields(arrivalField, checkInField, doctorField, labField, xRayField, treatmentField,
                    arrivalComboBox, checkInComboBox, doctorComboBox, labComboBox, xRayComboBox, treatmentComboBox);
        });

        HBox buttonsBox = new HBox(10, saveButton, setDefaultButton);
        buttonsBox.setAlignment(Pos.CENTER);

        VBox layout = new VBox(10, grid, buttonsBox);
        layout.setPadding(new Insets(10));
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout, 600, 400);
        applyTheme(scene, isBlackTheme);
        dialog.setScene(scene);
        dialog.showAndWait();
    }

    private void refreshDistributionDialogFields(TextField arrivalField, TextField checkInField, TextField doctorField, TextField labField, TextField xRayField, TextField treatmentField,
                                                 ComboBox<String> arrivalComboBox, ComboBox<String> checkInComboBox, ComboBox<String> doctorComboBox, ComboBox<String> labComboBox, ComboBox<String> xRayComboBox, ComboBox<String> treatmentComboBox) {
        arrivalField.setText(String.valueOf(dataController.getAverageTime("arrival")));
        checkInField.setText(String.valueOf(dataController.getAverageTime("check-in")));
        doctorField.setText(String.valueOf(dataController.getAverageTime("doctor")));
        labField.setText(String.valueOf(dataController.getAverageTime("lab")));
        xRayField.setText(String.valueOf(dataController.getAverageTime("xray")));
        treatmentField.setText(String.valueOf(dataController.getAverageTime("treatment")));

        arrivalComboBox.setValue(dataController.getDistribution("arrival"));
        checkInComboBox.setValue(dataController.getDistribution("check-in"));
        doctorComboBox.setValue(dataController.getDistribution("doctor"));
        labComboBox.setValue(dataController.getDistribution("lab"));
        xRayComboBox.setValue(dataController.getDistribution("xray"));
        treatmentComboBox.setValue(dataController.getDistribution("treatment"));
    }

    private void validateAndEnableSaveButton(Button saveButton, TextField... fields) {
        boolean allFieldsValid = true;
        for (TextField field : fields) {
            try {
                Double.parseDouble(field.getText());
            } catch (NumberFormatException e) {
                allFieldsValid = false;
                break;
            }
        }
        saveButton.setDisable(!allFieldsValid);
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
                    dataController.setProbabilities(labProbability, xrayProbability, treatmentProbability);
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

        Scene scene = new Scene(layout, 700, 250);
        applyTheme(scene, isBlackTheme);
        dialog.setScene(scene);
        dialog.showAndWait();
    }

    private VBox setupControlPanel() {
        setTimeField = new TextField();
        setTimeField.setPromptText("Set Time, e.g., 1000");
        updateTimeButton = new Button("Update Time");

        setDelayField = new TextField();
        setDelayField.setPromptText("Set Delay in ms");
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

        // Status label
        statusLabel = new Label("Simulation Status: Not Running");
        statusLabel.setMaxWidth(Double.MAX_VALUE); // Allow label to expand to full width
        statusLabel.setAlignment(Pos.CENTER_LEFT); // Align text inside label to the left
        statusLabel.setTextAlignment(TextAlignment.LEFT); // Align text within the label

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

        HBox gridBox = new HBox();
        gridBox.setSpacing(150);
        gridBox.setPadding(new Insets(20, 10, 30, 10));
        gridBox.setAlignment(Pos.CENTER);
        gridBox.getChildren().addAll(gridPane);

        // Main container
        VBox controlPanel = new VBox();
        controlPanel.setPadding(new Insets(10));
        controlPanel.getChildren().addAll(statusLabel, gridBox);
        controlPanel.setAlignment(Pos.CENTER); // Align the entire VBox to the top-right

        return controlPanel;
    }

    private Slider createProbabilitySlider(String labelText, String decisionType) {
        double probability = dataController.getProbability(decisionType);
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
        statusLabel.setText("Simulation Status: Running");
        activateButtons();
    }

    private void toggleSimulation() {
        if (controller.isRunning()) {
            controller.stopSimulation();
            statusLabel.setText("Simulation Status: Paused");
        } else {
            controller.resumeSimulation();
            statusLabel.setText("Simulation Status: Running");
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
        Stage helpStage = new Stage();
        helpStage.setTitle("Help - Health Center Simulation Guide");

        // Help Text
        String helpText = """
        Welcome to the Health Center Simulation!

        This simulation models the workflow of a health center with five service points:
        - **Check-In**: Patients check in before proceeding to see the doctor.
        - **Doctor**: Patients consult with a doctor to determine the next steps.
        - **Lab**: Patients may be sent to the lab for additional tests.
        - **X-Ray**: Patients may require an X-ray examination.
        - **Treatment**: Patients receive any necessary treatment.

        ### Controls Overview:

        1. **Set Time**: 
           - Enter the total simulation time in the 'Set Time' field.
           - Click 'Update Time' to apply changes, even if the simulation is already running.

        2. **Set Delay**:
           - Enter the delay between patient actions in the 'Set Delay' field.
           - Click 'Update Delay' to adjust the delay while the simulation is in progress.

        3. **Speed Up / Speed Down**:
           - Use these buttons to increase or decrease the speed of the simulation in real-time.

        4. **Run**:
           - Start or restart the simulation by clicking 'Run' after setting the time and delay.

        5. **Stop / Resume**:
           - Pause the simulation by clicking 'Stop'.
           - Resume the simulation by clicking 'Resume'.

        6. **Statistics**:
           - View current simulation statistics by clicking the 'Statistics' button.

        ### Configuration Options:

        - **Edit Distributions**: 
          - Adjust the distribution and corresponding processing time for each service point (e.g., Check-In, Doctor, Lab, etc.) via the 'Edit Distributions' option in the 'Config' menu. All values must be positive.

        - **Edit Probabilities**: 
          - Modify the probabilities for routing patients after visiting the doctor (e.g., to Lab, X-Ray, or Treatment) through the 'Edit Probabilities' option in the 'Config' menu. Ensure the total sum of all probabilities does not exceed 1.

        ### Additional Features:

        - **Patient Flow Visualization**:
          - The on-screen visualization shows patient progress as they move through different service points in the health center. This provides a real-time visual representation of the health center's workflow.

        For more detailed information or support, please refer to the user documentation or contact support.
        """;

        // TextArea to display the help text
        TextArea helpTextArea = new TextArea(helpText);
        helpTextArea.setWrapText(true);
        helpTextArea.setEditable(false);
        helpTextArea.setPrefWidth(600);
        helpTextArea.setPrefHeight(400);

        // Close button
        Button closeButton = new Button("Close");
        closeButton.setOnAction(e -> helpStage.close());

        // Layout
        VBox layout = new VBox(10, helpTextArea, closeButton);
        layout.setPadding(new Insets(10));
        layout.setAlignment(Pos.CENTER);

        Scene helpScene = new Scene(layout, 650, 500);
        applyTheme(helpScene, isBlackTheme); // Apply the theme

        helpStage.setScene(helpScene);
        helpStage.initModality(Modality.APPLICATION_MODAL); // Block other windows until help dialog is closed
        helpStage.showAndWait();
    }

    public void showResultsDialog() {
        // Fetch all simulation results
        List<SimulationResults> results = dataController.getSimulationResults();

        // Format the results into a readable format
        StringBuilder statistics = new StringBuilder();
        for (SimulationResults result : results) {
            statistics.append("Simulation ID: ").append(result.getSimulationId()).append("\n");
            statistics.append("Average Time: ").append(result.getAverageTime()).append("\n");
            statistics.append("Total Patients: ").append(result.getTotalPatients()).append("\n");
            statistics.append("Completed Visits: ").append(result.getCompletedVisits()).append("\n");
            statistics.append("Lab Probability: ").append(result.getLabProbability()).append("\n");
            statistics.append("X-ray Probability: ").append(result.getXrayProbability()).append("\n");
            statistics.append("Treatment Probability: ").append(result.getTreatmentProbability()).append("\n");
            statistics.append("Arrival Time: ").append(result.getArrivalTime()).append("\n");
            statistics.append("Check-in Time: ").append(result.getCheckInTime()).append("\n");
            statistics.append("Doctor Time: ").append(result.getDoctorTime()).append("\n");
            statistics.append("Lab Time: ").append(result.getLabTime()).append("\n");
            statistics.append("X-ray Time: ").append(result.getXrayTime()).append("\n");
            statistics.append("Treatment Time: ").append(result.getTreatmentTime()).append("\n");
            statistics.append("End Time: ").append(result.getEndTime()).append("\n");
            statistics.append("------------------------------\n");
        }

        // Create a TextArea to display the results
        TextArea textArea = new TextArea(statistics.toString());
        textArea.setWrapText(true);  // Enable text wrapping
        textArea.setEditable(false);   // Make it read-only

        // Wrap the TextArea in a ScrollPane
        ScrollPane scrollPane = new ScrollPane(textArea);
        scrollPane.setFitToWidth(true); // Allow the ScrollPane to fit to width
        scrollPane.setFitToHeight(true); // Allow the ScrollPane to fit to height

        // Create and configure the dialog
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Simulation Results");
        dialog.getDialogPane().setContent(scrollPane);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK); // Add an OK button
        dialog.setResizable(true); // Make the dialog resizable

        applyTheme(dialog.getDialogPane().getScene(), isBlackTheme);
        // Show the dialog
        dialog.showAndWait();
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
    public void endSimulation() {
        statusLabel.setText("Simulation Status: Ended");

        // deactivating buttons
        stopButton.setDisable(true);
        speedUpButton.setDisable(true);
        speedDownButton.setDisable(true);
        updateTimeButton.setDisable(true);
        updateDelayButton.setDisable(true);


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
