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
import java.util.prefs.Preferences;

/**
 * The main graphical user interface (GUI) class for the Health Center simulation application.
 *
 * <p>This class extends the JavaFX Application class and implements the IHealthcenterGUI interface,
 * providing the primary functionality for user interaction and visualization of the health center
 * operations. It manages the layout and behavior of various UI components including buttons,
 * sliders, labels, and canvases for service point visualizations.</p>
 *
 * <p>Key features of this class include:</p>
 * <ul>
 *   <li>Controller instances for handling application logic and data management.</li>
 *   <li>UI components for setting time and delay, as well as displaying status updates.</li>
 *   <li>Buttons for starting, stopping, and adjusting the simulation.</li>
 *   <li>Sliders for configuring probabilities related to service points.</li>
 *   <li>Progress bar to indicate the current progress of the simulation.</li>
 *   <li>Support for dark and light themes through external CSS styles.</li>
 *   <li>Functionality to track whether the help dialog has been displayed to the user.</li>
 * </ul>
 */
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

    // Progress bar
    private ProgressBar progressBar;
    private Label progressLabel;

    // styles
    private static final String BLACK_THEME_CSS = "/black-theme.css";
    private static final String WHITE_THEME_CSS = "/white-theme.css";
    private boolean isBlackTheme = true;

    // Preferences key to store whether the help dialog has been shown
    private static final String HELP_SHOWN_KEY = "helpShown";

    /**
     * Initializes the application before the primary stage is created.
     *
     * <p>This method is called by the JavaFX framework to perform any setup tasks required
     * before the application starts. It sets the trace level for logging and initializes
     * the controllers needed for the application.</p>
     *
     * <p>The following actions are performed:</p>
     * <ul>
     *   <li>Sets the trace level to INFO using the Trace utility for logging purposes.</li>
     *   <li>Creates an instance of the HealthcenterController, passing the current instance of the application.</li>
     *   <li>Initializes the DataController to manage data-related operations.</li>
     * </ul>
     */
    @Override
    public void init() {
        Trace.setTraceLevel(Trace.Level.INFO);
        controller = new HealthcenterController(this);
        dataController = new DataController();
    }

    /**
     * The main entry point of the JavaFX application.
     *
     * <p>This method is called when the application is launched. It sets up the primary stage
     * and initializes the main user interface components, including the menu bar, control panel,
     * service points, and progress bar. The layout is organized in a VBox with specified dimensions.</p>
     *
     * <p>The following actions are performed:</p>
     * <ul>
     *   <li>Configures the primary stage title and close request behavior.</li>
     *   <li>Initializes the menu bar and various UI components.</li>
     *   <li>Creates and sets the scene for the primary stage.</li>
     *   <li>Applies the selected theme to the scene.</li>
     *   <li>Displays the primary stage and requests focus for the main layout.</li>
     *   <li>Checks if it is the user's first time opening the application and shows the help dialog if so.</li>
     * </ul>
     *
     * @param primaryStage The primary stage for this application, onto which the application scene can be set.
     */
    @Override
    public void start(Stage primaryStage) {
        setupPrimaryStage(primaryStage);
        MenuBar menuBar = setupMenuBar();
        VBox controlPanel = setupControlPanel();
        HBox servicePointsBox = setupServicePoints();
        HBox progressBarBox = setupProgressBarBox();
        VBox mainLayout = new VBox(menuBar, controlPanel, servicePointsBox, progressBarBox);
        Scene scene = new Scene(mainLayout, 1400, 850);
        applyTheme(scene, isBlackTheme);

        primaryStage.setScene(scene);
        primaryStage.show();
        Platform.runLater(mainLayout::requestFocus);

        // Check if it's the first time the user is opening the application
        showHelpDialogIfFirstTime();
    }

    /**
     * Sets up the primary stage for the application.
     *
     * <p>This method configures the primary stage by setting the title and defining the behavior
     * when the stage is requested to close. Specifically, it ensures that the application exits
     * cleanly when the user attempts to close the window.</p>
     *
     * @param primaryStage The primary stage of the application to be configured.
     */
    private void setupPrimaryStage(Stage primaryStage) {
        primaryStage.setOnCloseRequest(event -> {
            Platform.exit();
            System.exit(0);
        });
        primaryStage.setTitle("Health Center Simulation");
    }

    /**
     * Sets up and returns a MenuBar with various menu items for the application.
     *
     * <p>This method creates a menu bar containing menus for File, Config, Help, and Results.
     * Each menu contains relevant items that allow the user to perform actions within the application.</p>
     *
     * <p>The following menus and their items are created:</p>
     * <ul>
     *   <li><strong>File Menu:</strong>
     *       <ul>
     *           <li><em>Toggle Theme:</em> Toggles between black and white themes.</li>
     *           <li><em>Exit:</em> Exits the application.</li>
     *       </ul>
     *   </li>
     *   <li><strong>Config Menu:</strong>
     *       <ul>
     *           <li><em>Edit Distributions:</em> Opens a dialog to edit average times and distributions.</li>
     *           <li><em>Edit Probabilities:</em> Opens a dialog to edit probabilities.</li>
     *       </ul>
     *   </li>
     *   <li><strong>Help Menu:</strong>
     *       <ul>
     *           <li><em>How to use:</em> Opens a help dialog.</li>
     *       </ul>
     *   </li>
     *   <li><strong>Results Menu:</strong>
     *       <ul>
     *           <li><em>Results:</em> Opens a dialog to display results.</li>
     *       </ul>
     *   </li>
     * </ul>
     *
     * @return A configured MenuBar with all necessary menus and items.
     */
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

    /**
     * Displays a modal dialog that allows the user to edit the distributions and average times
     * for various service points including Arrival, Check-In, Doctor, Lab, X-Ray, and Treatment.
     *
     * <p>This method creates a dialog containing a grid layout where each service point is represented
     * with a label, a text field for entering the average time, and a combo box for selecting the distribution.</p>
     *
     * <p>The dialog provides the following features:</p>
     * <ul>
     *   <li>Text fields populated with the current average times retrieved from the data controller.</li>
     *   <li>Combo boxes that allow selection of distribution types for each service point.</li>
     *   <li>Buttons for saving the changes and for resetting the distributions to default values.</li>
     *   <li>Dynamic validation that enables the save button only when valid numeric values are entered.</li>
     * </ul>
     *
     * <p>When the user clicks the "Save" button, the updated average times and distributions are
     * sent to the data controller for storage. The "Set Default" button resets the fields to their
     * default values.</p>
     */

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

    /**
     * Refreshes the fields of the distribution dialog with the current average times and distributions
     * retrieved from the data controller.
     *
     * <p>This method updates the text fields with average times for various stages (arrival, check-in,
     * doctor, lab, x-ray, treatment) and sets the selected values of the corresponding combo boxes
     * based on the current distribution from the data controller.</p>
     *
     * @param arrivalField      The text field for arrival time.
     * @param checkInField     The text field for check-in time.
     * @param doctorField      The text field for doctor time.
     * @param labField         The text field for lab time.
     * @param xRayField        The text field for x-ray time.
     * @param treatmentField    The text field for treatment time.
     * @param arrivalComboBox   The combo box for arrival distribution.
     * @param checkInComboBox  The combo box for check-in distribution.
     * @param doctorComboBox    The combo box for doctor distribution.
     * @param labComboBox       The combo box for lab distribution.
     * @param xRayComboBox      The combo box for x-ray distribution.
     * @param treatmentComboBox  The combo box for treatment distribution.
     */
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

    /**
     * Validates the input fields and enables or disables the save button based on the validity of the fields.
     *
     * <p>This method checks each provided {@link TextField} for valid numeric input. If all fields contain
     * valid numeric values, the save button is enabled; otherwise, it is disabled.</p>
     *
     * @param saveButton the button to enable or disable based on field validity
     * @param fields     a variable number of {@link TextField} objects to validate
     */

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

    /**
     * Displays a modal dialog that allows the user to edit the probabilities for Lab, X-Ray, and Treatment.
     * The dialog includes sliders to adjust each probability and applies the changes upon confirmation.
     *
     * <p>This method creates a modal dialog window that blocks interaction with other windows until the user
     * interacts with it. The user can adjust the probabilities using sliders, which will update corresponding
     * labels to display the current values. When the "Apply Probabilities" button is clicked, the selected
     * probabilities are passed to the data controller, and the dialog is closed.</p>
     *
     * <p>The sliders control the following probabilities:</p>
     * <ul>
     *   <li>Lab Probability</li>
     *   <li>X-Ray Probability</li>
     *   <li>Treatment Probability</li>
     * </ul>
     *
     * <p>Components within the dialog:</p>
     * <ul>
     *   <li>Labels showing the current values of the sliders</li>
     *   <li>Three sliders to adjust Lab, X-Ray, and Treatment probabilities</li>
     *   <li>An "Apply Probabilities" button that saves the user's input and closes the dialog</li>
     * </ul>
     *
     * <p>The dialog is styled and positioned in the center of the scene with a VBox layout.
     * It ensures the user cannot interact with other windows while this dialog is open.</p>
     */
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

    /**
     * Sets up the control panel for the health center simulation interface.
     *
     * <p>This method creates and configures various UI components, including text fields for setting
     * time and delay, buttons for starting, stopping, and adjusting the simulation speed, and a status
     * label to display the current simulation state. It also organizes these components in a layout.</p>
     *
     * @return a {@link VBox} containing the configured control panel components.
     */

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
        gridBox.setPadding(new Insets(20, 10, 20, 10));
        gridBox.setAlignment(Pos.CENTER);
        gridBox.getChildren().addAll(gridPane);

        // Main container
        VBox controlPanel = new VBox();
        controlPanel.setPadding(new Insets(10));
        controlPanel.getChildren().addAll(statusLabel, gridBox);
        controlPanel.setAlignment(Pos.CENTER); // Align the entire VBox to the top-right

        return controlPanel;
    }

    /**
     * Creates a probability slider for the specified decision type.
     *
     * <p>This method initializes a {@link Slider} component with a range from 0 to 1,
     * and sets its initial value based on the probability associated with the given decision type.</p>
     *
     * @param labelText the text to be displayed next to the slider, indicating its purpose.
     * @param decisionType the type of decision for which the probability is being set.
     * @return a {@link Slider} configured for adjusting probabilities.
     */
    private Slider createProbabilitySlider(String labelText, String decisionType) {
        double probability = dataController.getProbability(decisionType);
        Slider slider = new Slider(0, 1, probability);
        slider.setShowTickLabels(true);
        slider.setShowTickMarks(true);
        return slider;
    }

    /**
     * Sets up the service points visualizations for the health center simulation.
     *
     * <p>This method creates visual components for each service point (Check-In, Doctor, Lab,
     * X-Ray, and Treatment) and organizes them into a horizontal layout (HBox). Each service
     * point is represented by a VBox containing a label and its corresponding visualization
     * canvas.</p>
     *
     * @return an {@link HBox} containing all the service points' visualizations,
     *         properly aligned and padded.
     */
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

    /**
     * Creates a VBox containing a label and a visualization canvas for a service point.
     *
     * <p>This method constructs a vertical box layout that consists of a label displaying the
     * name of the service point and its corresponding visualization canvas. The label is styled
     * with a specific CSS class for consistent appearance.</p>
     *
     * @param labelText the text to display on the label for the service point.
     * @param canvas the {@link Visualization} canvas associated with the service point.
     * @return a {@link VBox} containing the label and canvas, aligned to the center.
     */
    private VBox createServicePointBox(String labelText, Visualization canvas) {
        Label label = new Label(labelText);
        label.getStyleClass().add("service-point-label"); // Add CSS class to label

        VBox vbox = new VBox(10);
        vbox.getChildren().addAll(label, canvas);
        vbox.setAlignment(Pos.CENTER);

        return vbox;
    }

    /**
     * Sets up event handlers for the control buttons in the user interface.
     *
     * <p>This method configures the actions that will be performed when the various
     * buttons in the control panel are clicked. It associates each button with its
     * respective action, enabling the user to control the simulation.</p>
     *
     * <ul>
     *   <li><strong>Update Time Button:</strong> Calls the {@link #updateSimulationTime()}
     *   method when clicked.</li>
     *   <li><strong>Update Delay Button:</strong> Calls the {@link #updateSimulationDelay()}
     *   method when clicked.</li>
     *   <li><strong>Speed Up Button:</strong> Calls the {@link IControllerForV#speedUp()}
     *   method to increase the simulation speed.</li>
     *   <li><strong>Speed Down Button:</strong> Calls the {@link IControllerForV#slowDown()}
     *   method to decrease the simulation speed.</li>
     *   <li><strong>Run Button:</strong> Calls the {@link #startSimulation()} method
     *   to start or restart the simulation.</li>
     *   <li><strong>Stop Button:</strong> Calls the {@link #toggleSimulation()} method
     *   to pause or resume the simulation.</li>
     *   <li><strong>Statistics Button:</strong> Calls the {@link IControllerForV#showStatistics(String)}
     *   method with a predefined message to display the statistics.</li>
     * </ul>
     */
    private void setupEventHandlers() {
        updateTimeButton.setOnAction(e -> updateSimulationTime());
        updateDelayButton.setOnAction(e -> updateSimulationDelay());
        speedUpButton.setOnAction(e -> controller.speedUp());
        speedDownButton.setOnAction(e -> controller.slowDown());
        runButton.setOnAction(e -> startSimulation());
        stopButton.setOnAction(e -> toggleSimulation());
        statisticsButton.setOnAction(e -> controller.showStatistics("Statistics go here"));
    }

    /**
     * Updates the simulation time based on user input.
     *
     * This method retrieves the time value from the user input field
     * using the {@link #getTime()} method and sets the new time
     * for the simulation by calling the {@link IControllerForV#setTime(int)}
     * method on the controller. The time value is cast to an integer
     * before updating, ensuring that the simulation runs with the
     * appropriate time parameter specified by the user.
     */
    private void updateSimulationTime() {
        controller.setTime((int) getTime());
    }

    /**
     * Updates the simulation delay based on user input.
     *
     * This method retrieves the delay value from the user input field
     * using the {@link #getDelay()} method and sets the new delay
     * for the simulation by calling the {@link IControllerForV#setDelay(long)}
     * method on the controller. This allows the simulation to adjust its
     * timing based on user-defined parameters.
     */
    private void updateSimulationDelay() {
        controller.setDelay(getDelay());
    }

    /**
     * Initiates the simulation and updates the status label.
     *
     * This method starts the simulation by invoking the
     * {@link IControllerForV#startSimulation()} method on the
     * controller. After starting the simulation, it updates
     * the status label to indicate that the simulation is
     * currently running. It also activates the relevant
     * buttons for user interaction during the simulation.
     */
    private void startSimulation() {
        controller.startSimulation();
        statusLabel.setText("Simulation Status: Running");
        activateButtons();
    }

    /**
     * Toggles the state of the simulation between running and paused.
     *
     * This method checks if the simulation is currently running using
     * the controller. If the simulation is running, it stops the
     * simulation and updates the status label to indicate that the
     * simulation is paused. If the simulation is paused, it resumes
     * the simulation and updates the status label to indicate that
     * the simulation is running.
     */
    private void toggleSimulation() {
        if (controller.isRunning()) {
            controller.stopSimulation();
            statusLabel.setText("Simulation Status: Paused");
        } else {
            controller.resumeSimulation();
            statusLabel.setText("Simulation Status: Running");
        }
    }

    /**
     * Disables all control buttons at the start of the application.
     *
     * This method is called to ensure that the user cannot start or
     * manipulate the simulation until appropriate input has been
     * provided. The following buttons are disabled:
     *
     * - Run
     * - Stop
     * - Update Time
     * - Update Delay
     * - Speed Up
     * - Speed Down
     * - Statistics
     */
    private void disableInitialButtons() {
        runButton.setDisable(true);
        stopButton.setDisable(true);
        updateTimeButton.setDisable(true);
        updateDelayButton.setDisable(true);
        speedUpButton.setDisable(true);
        speedDownButton.setDisable(true);
        statisticsButton.setDisable(true);
    }

    /**
     * Adds input validation listeners to the time and delay text fields.
     *
     * Whenever the text in either the time or delay field is changed, the
     * validateInput method is called to check the validity of the input.
     * This ensures that the run button is enabled or disabled based on
     * the current input values.
     */
    private void addInputValidationListeners() {
        setTimeField.textProperty().addListener((observable, oldValue, newValue) -> validateInput());
        setDelayField.textProperty().addListener((observable, oldValue, newValue) -> validateInput());
    }

    /**
     * Validates the input values for time and delay fields.
     *
     * Disables the run button if either the time or delay input is invalid.
     * The time input must be a positive double, and the delay input must be a
     * non-negative long.
     */
    private void validateInput() {
        boolean isTimeValid = isValidDouble(setTimeField.getText());
        boolean isDelayValid = isValidLong(setDelayField.getText());
        runButton.setDisable(!(isTimeValid && isDelayValid));
    }

    /**
     * Validates whether the given input string can be parsed as a positive double value.
     *
     * @param input the string to validate
     * @return {@code true} if the input can be parsed as a positive double;
     *         {@code false} otherwise
     */
    private boolean isValidDouble(String input) {
        try {
            double value = Double.parseDouble(input);
            return value > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Validates whether the given input string can be parsed as a non-negative long value.
     *
     * @param input the string to validate
     * @return {@code true} if the input can be parsed as a non-negative long;
     *         {@code false} otherwise
     */
    private boolean isValidLong(String input) {
        try {
            long value = Long.parseLong(input);
            return value >= 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Activates the control buttons for the simulation.
     *
     * <p>This method enables the buttons that allow the user to interact
     * with the simulation, including stopping, speeding up, and updating
     * simulation parameters. It should be called when the simulation is
     * started or resumed.</p>
     */
    private void activateButtons() {
        stopButton.setDisable(false);
        speedUpButton.setDisable(false);
        updateTimeButton.setDisable(false);
        updateDelayButton.setDisable(false);
        speedDownButton.setDisable(false);
        statisticsButton.setDisable(false);
    }

    /**
     * Retrieves the Check-In visualization canvas.
     *
     * <p>This method provides access to the visualization component
     * that displays information related to the Check-In process
     * in the health center simulation.</p>
     *
     * @return the Check-In visualization canvas as an instance of {@link IVisualization}.
     */
    @Override
    public IVisualization getCheckInCanvas() {
        return checkInCanvas;
    }

    /**
     * Retrieves the Doctor visualization canvas.
     *
     * <p>This method provides access to the visualization component
     * responsible for displaying information related to Doctor services in the simulation.</p>
     *
     * @return the Doctor visualization canvas as an instance of {@link IVisualization}.
     */
    @Override
    public IVisualization getDoctorCanvas() {
        return doctorCanvas;
    }

    /**
     * Retrieves the Lab visualization canvas.
     *
     * <p>This method provides access to the visualization component
     * responsible for displaying information related to Lab services in the simulation.</p>
     *
     * @return the Lab visualization canvas as an instance of {@link IVisualization}.
     */
    @Override
    public IVisualization getLabCanvas() {
        return labCanvas;
    }

    /**
     * Retrieves the X-Ray visualization canvas.
     *
     * <p>This method provides access to the visualization component
     * responsible for displaying information related to X-Ray services in the simulation.</p>
     *
     * @return the X-Ray visualization canvas as an instance of {@link IVisualization}.
     */
    @Override
    public IVisualization getXrayCanvas() {
        return xrayCanvas;
    }

    /**
     * Retrieves the treatment visualization canvas.
     *
     * <p>This method provides access to the visualization component
     * responsible for displaying information related to treatment in the simulation.</p>
     *
     * @return the treatment visualization canvas as an instance of {@link IVisualization}.
     */
    @Override
    public IVisualization getTreatmentCanvas() {
        return treatmentCanvas;
    }

    /**
     * Retrieves the current time value from the input field.
     *
     * <p>This method parses the text from the set time input field
     * and converts it to a double value representing the time.</p>
     *
     * @return the current time as a double value.
     * @throws NumberFormatException if the input text cannot be parsed as a double.
     */
    @Override
    public double getTime() {
        return Double.parseDouble(setTimeField.getText());
    }

    /**
     * Retrieves the current delay value from the input field.
     *
     * <p>This method parses the text from the set delay input field
     * and converts it to a long value representing the delay in milliseconds.</p>
     *
     * @return the current delay in milliseconds as a long value.
     * @throws NumberFormatException if the input text cannot be parsed as a long.
     */
    @Override
    public long getDelay() {
        return Long.parseLong(setDelayField.getText());
    }

    /**
     * Displays a dialog showing the statistics of the simulation.
     *
     * <p>This method creates an informational alert dialog that presents
     * the provided simulation statistics to the user. The dialog has a
     * custom width to ensure the content is displayed clearly. The current
     * theme of the application is applied to the dialog to maintain visual
     * consistency.</p>
     *
     * @param statistics A string containing the statistics to be displayed.
     */
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

    /**
     * Clears all visualization displays for the various service points in the GUI.
     *
     * <p>This method iterates through the visualization components (check-in, doctor,
     * lab, X-ray, and treatment) and calls the {@code clearDisplay} method on each
     * visualization. This is typically used to reset the state of the displays before
     * starting a new simulation or when updating the displays.</p>
     */
    @Override
    public void clearDisplays() {
        for (IVisualization v : new Visualization[]{checkInCanvas, doctorCanvas, labCanvas, xrayCanvas, treatmentCanvas}) {
            v.clearDisplay();
        }
    }

    /**
     * Displays a help dialog for the Health Center Simulation application.
     * <p>
     * This method creates a modal dialog window that provides a detailed guide to
     * using the simulation. It explains the purpose and functions of the simulation,
     * including the workflow of the five service points in a health center (Check-In,
     * Doctor, Lab, X-ray, and Treatment). The help text also includes instructions
     * on how to control the simulation, including setting time, delay, adjusting speed,
     * viewing statistics, and editing configuration options.
     * </p>
     *
     * <h3>Key Sections:</h3>
     * <ul>
     *     <li>Simulation Overview: Explains each service point's role in the simulation.</li>
     *     <li>Controls Overview: Describes how to set simulation time, delay, speed, and view statistics.</li>
     *     <li>Menu Options: Explains file, config, help, and results menu options.</li>
     *     <li>Additional Features: Provides details about patient flow visualization, status, and default settings.</li>
     * </ul>
     *
     * The help content is displayed in a non-editable {@code TextArea}, and users can close
     * the dialog with a "Close" button. The dialog is modal, blocking interaction with
     * other windows until it is closed.
     *
     * @see Stage
     * @see TextArea
     * @see Button
     * @see Modality
     */
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
                   - Enter the delay between patient actions in milliseconds in the 'Set Delay' field.
                   - Click 'Update Delay' to adjust the delay while the simulation is in progress.

                3. **Speed Up / Speed Down**:
                   - Use these buttons to increase or decrease the speed of the simulation in real-time.

                4. **Run**:
                   - Start or restart the simulation by clicking 'Start / Restart' after setting the time and delay.

                5. **Stop / Resume**:
                   - Pause the simulation by clicking 'Stop / Resume'.
                   - Resume the simulation by clicking the same button.

                6. **Statistics**:
                   - View the current simulation statistics by clicking the 'Statistics' button.

                ### Menu Options:

                - **File Menu**:
                  - **Black / White Theme**: Switch between the dark and light themes.
                  - **Exit**: Close the application.

                - **Config Menu**:
                  - **Edit Distributions**: Adjust the distribution type and average processing times for each service point (e.g., Check-In, Doctor, Lab, etc.). You can set the distribution to either "negexp" or "poisson", and specify the average time.
                  - **Edit Probabilities**: Modify the probabilities for routing patients to Lab, X-Ray, or Treatment after a doctor's consultation. Make sure the total sum of probabilities does not exceed 1.

                - **Help Menu**:
                  - **How to Use**: View this help dialog.

                - **Results Menu**:
                  - **Results**: View previous simulation results, including average times, total patients, and probabilities used.

                ### Additional Features:

                - **Patient Flow Visualization**:
                  - The visualization panel displays patient movement through each of the five service points in the health center. Each service point is represented by a box that visually shows the number of patients present.
                    
                - **Set Default**:
                  - When editing distributions, use the 'Set Default' button to reset all distributions and average times to their default values.

                - **Progress and Status**:
                  - A status label at the top displays whether the simulation is running, paused, or ended.
                  - The progress bar fills as the simulation progresses, providing a visual cue of how much time remains.

                For more detailed information or support, please refer to the user documentation or contact support.
                """;

        // TextArea to display the help text
        TextArea helpTextArea = new TextArea(helpText);
        helpTextArea.setWrapText(true);
        helpTextArea.setEditable(false);
        helpTextArea.setPrefWidth(600);
        helpTextArea.setPrefHeight(500);

        // Close button
        Button closeButton = new Button("Close");
        closeButton.setOnAction(e -> helpStage.close());

        // Layout
        VBox layout = new VBox(10, helpTextArea, closeButton);
        layout.setPadding(new Insets(10));
        layout.setAlignment(Pos.CENTER);

        Scene helpScene = new Scene(layout, 650, 600);
        applyTheme(helpScene, isBlackTheme); // Apply the theme

        helpStage.setScene(helpScene);
        helpStage.initModality(Modality.APPLICATION_MODAL); // Block other windows until help dialog is closed
        helpStage.showAndWait();
    }

    /**
     * Displays a dialog with formatted simulation results.
     * <p>
     * This method fetches all the simulation results from the {@code dataController},
     * and formats them into a human-readable form using a {@code StringBuilder}. The results
     * include various statistics for each simulation such as simulation ID, average time,
     * total patients, and probabilities for different events (lab, x-ray, treatment, etc.).
     * The results are then displayed in a dialog (implementation of the actual dialog is omitted in this method).
     * </p>
     *
     * <ul>
     *     <li>Simulation ID</li>
     *     <li>Average Time</li>
     *     <li>Total Patients</li>
     *     <li>Completed Visits</li>
     *     <li>Lab, X-ray, and Treatment Probabilities</li>
     *     <li>Arrival Time, Check-in Time, Doctor Time, Lab Time, X-ray Time, Treatment Time, End Time</li>
     * </ul>
     *
     * The data is formatted with two decimal places for numerical values where applicable.
     *
     * @see SimulationResults
     * @see DataController#getSimulationResults()
     */

    public void showResultsDialog() {
        // Fetch all simulation results
        List<SimulationResults> results = dataController.getSimulationResults();

        // Format the results into a readable format
        StringBuilder statistics = new StringBuilder();
        for (SimulationResults result : results) {
            statistics.append("Simulation ID: ").append(result.getSimulationId()).append("\n");
            statistics.append("Average Time: ").append(String.format("%.2f", result.getAverageTime())).append("\n");
            statistics.append("Total Patients: ").append(result.getTotalPatients()).append("\n");
            statistics.append("Completed Visits: ").append(result.getCompletedVisits()).append("\n");
            statistics.append("End Time: ").append(String.format("%.2f", result.getEndTime())).append("\n\n");
            statistics.append("--Distiributions\n\n");
            statistics.append("Arrival Time: ").append(result.getArrivalTime()).append("\n");
            statistics.append("Check-in Time: ").append(result.getCheckInTime()).append("\n");
            statistics.append("Doctor Time: ").append(result.getDoctorTime()).append("\n");
            statistics.append("Lab Time: ").append(result.getLabTime()).append("\n");
            statistics.append("X-ray Time: ").append(result.getXrayTime()).append("\n");
            statistics.append("Treatment Time: ").append(result.getTreatmentTime()).append("\n\n");
            statistics.append("--Probabilitys\n\n");
            statistics.append("Lab Probability: ").append(String.format("%.2f", result.getLabProbability())).append("\n");
            statistics.append("X-ray Probability: ").append(String.format("%.2f", result.getXrayProbability())).append("\n");
            statistics.append("Treatment Probability: ").append(String.format("%.2f", result.getTreatmentProbability())).append("\n");
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

    /**
     * Updates the probability values displayed in the GUI based on the current
     * values of the probability sliders for lab, X-ray, and treatment.
     *
     * <p>This method first calculates the total of the probabilities from the
     * respective sliders. If the total exceeds 1, it reduces each slider's value
     * equally to ensure the total probability remains at 1. It then updates the
     * corresponding labels to reflect the current probability values formatted
     * to two decimal places.</p>
     */
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


    /**
     * Applies the specified theme to the given scene.
     *
     * <p>This method clears any existing stylesheets from the scene and adds either
     * the black or white theme stylesheet based on the specified boolean flag.</p>
     *
     * @param scene        The JavaFX Scene to which the theme should be applied.
     * @param isBlackTheme A boolean flag indicating whether to apply the black theme
     *                     (true) or the white theme (false).
     */
    private void applyTheme(Scene scene, boolean isBlackTheme) {
        scene.getStylesheets().clear();
        if (isBlackTheme) {
            scene.getStylesheets().add(getClass().getResource(BLACK_THEME_CSS).toExternalForm());
        } else {
            scene.getStylesheets().add(getClass().getResource(WHITE_THEME_CSS).toExternalForm());
        }
    }

    /**
     * Ends the simulation and updates the UI to reflect its status.
     *
     * <p>This method updates the status label to indicate that the simulation has ended
     * and disables various control buttons to prevent further actions. It also displays
     * an informational alert to inform the user that the simulation has concluded.</p>
     */
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

    /**
     * Sets up the progress bar and its accompanying label in a horizontal box layout.
     *
     * <p>This method creates an HBox containing a progress bar and a label to display
     * the current progress percentage. The progress bar is initialized to 0, and the
     * label displays "0%" to indicate the starting state.</p>
     *
     * @return An HBox containing the progress bar and progress label.
     */

    public HBox setupProgressBarBox() {
        HBox progressBarBox = new HBox();
        progressBarBox.setPadding(new Insets(40));
        progressBarBox.setSpacing(10);
        progressBarBox.setAlignment(Pos.CENTER);

        // ProgressBar setup
        progressBar = new ProgressBar(0);
        progressBar.setPrefWidth(500);

        // Progress label setup
        progressLabel = new Label("0%");

        // Add the progress bar and label to the HBox
        progressBarBox.getChildren().addAll(progressBar, progressLabel);

        return progressBarBox;
    }
    /**
     * Updates the progress bar and its corresponding label based on the current simulation time.
     *
     * <p>This method calculates the progress of the simulation as a fraction of the
     * current time to the end time, and updates the progress bar accordingly.
     * It also updates a label to display the progress as a percentage.</p>
     *
     * @param currentTime The current time in the simulation.
     * @param endTime The total duration of the simulation.
     */
    @Override
    public void updateProgressBar(double currentTime, double endTime) {
        double progress = currentTime / endTime;
        progressBar.setProgress(progress);

        // Calculate percentage and update the label
        int percentage = (int) (progress * 100);
        Platform.runLater(() -> progressLabel.setText(percentage + "%"));
    }


    /**
     * Displays the help dialog if the user is opening the application for the first time.
     *
     * <p>This method checks the user's preferences to determine if the help dialog
     * has been shown previously. If it has not been shown, the help dialog is displayed
     * to assist the user in understanding the application features.</p>
     *
     * <p>After displaying the help dialog, the method updates the user's preferences
     * to indicate that the help has been shown, preventing it from appearing on subsequent
     * launches of the application.</p>
     */
    private void showHelpDialogIfFirstTime() {
        // Access the user's preferences for this application
        Preferences prefs = Preferences.userNodeForPackage(HealthcenterGUI.class);

        // Check if the help has been shown before
        boolean helpShown = prefs.getBoolean(HELP_SHOWN_KEY, false);

        if (!helpShown) {
            // Show the help dialog
            showHelpDialog();

            // Set the preference to indicate that the help dialog has been shown
            prefs.putBoolean(HELP_SHOWN_KEY, true);
        }
    }

}
