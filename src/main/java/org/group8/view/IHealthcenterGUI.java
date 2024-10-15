package org.group8.view;

/**
 * The IHealthcenterGUI interface defines the contract for the
 * graphical user interface (GUI) of the Health Center application.
 * It provides methods for the controller to interact with the
 * GUI components, ensuring that the necessary data and actions
 * are exposed for the simulation's operation and visualization.
 *
 * <p>This interface serves as an abstraction layer, allowing
 * different implementations of the GUI while providing a standard
 * set of functionalities required for simulation control and
 * data display.</p>
 */
public interface IHealthcenterGUI {

    // The controller needs inputs, which it forwards to the Healthcenter

    /**
     * Retrieves the current time setting from the GUI.
     *
     * @return the current simulation time as a double.
     */
    double getTime();  // Method to get the current time

    /**
     * Retrieves the current delay setting from the GUI.
     *
     * @return the current simulation delay in milliseconds as a long.
     */
    long getDelay();   // Method to get the delay

    /**
     * Displays the provided statistics in the GUI.
     *
     * @param statistics a String containing the statistics to display.
     */
    void showStatistics(String statistics);

    /**
     * Notifies the GUI that the simulation has ended.
     * This method may be used to update the interface accordingly.
     */
    void endSimulation();

    /**
     * Clears all display components in the GUI.
     * This is typically used to reset the visual state of the application.
     */
    void clearDisplays();

    /**
     * Retrieves the check-in visualization component.
     *
     * @return an IVisualization instance representing the check-in canvas.
     */
    IVisualization getCheckInCanvas();

    /**
     * Retrieves the doctor visualization component.
     *
     * @return an IVisualization instance representing the doctor canvas.
     */
    IVisualization getDoctorCanvas();

    /**
     * Retrieves the lab visualization component.
     *
     * @return an IVisualization instance representing the lab canvas.
     */
    IVisualization getLabCanvas();

    /**
     * Retrieves the X-Ray visualization component.
     *
     * @return an IVisualization instance representing the X-Ray canvas.
     */
    IVisualization getXrayCanvas();

    /**
     * Retrieves the treatment visualization component.
     *
     * @return an IVisualization instance representing the treatment canvas.
     */
    IVisualization getTreatmentCanvas();

    /**
     * Updates the progress bar in the GUI based on the current simulation state.
     *
     * @param currentTime the current time in the simulation.
     * @param endTime the total time for the simulation.
     */
    void updateProgressBar(double currentTime, double endTime);
}
