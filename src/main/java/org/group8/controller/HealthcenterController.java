package org.group8.controller;

import javafx.application.Platform;
import org.group8.simulator.framework.Clock;
import org.group8.simulator.framework.IHealthCentre;
import org.group8.simulator.model.*;
import org.group8.view.IHealthcenterGUI;

/**
 * The HealthcenterController class is for managing the health center simulation.
 */
public class HealthcenterController implements IControllerForP, IControllerForV {

    private IHealthcenterGUI gui;
    private IHealthCentre centre;

    /**
     * Constructs a HealthcenterController with the specified gui.
     * @param gui The health center GUI
     */
    public HealthcenterController(IHealthcenterGUI gui) {
        this.gui = gui;
    }

    /**
     * Sets the simulation time.
     * @param time The simulation time
     */
    public void setTime(int time) {
        centre.setSimulationTime(time);
    }

    /**
     * Starts the simulation
     */
    @Override
    public void startSimulation() {
        // Delete the old thread
        if (centre != null && ((Thread) centre).isAlive()) {
            ((Thread) centre).interrupt();
        }

        // Set up new simulation with the current configuration
        centre = new HealthCentre(this);
        centre.setSimulationTime(gui.getTime());
        centre.setDelay(gui.getDelay());

        // Adjust common parameters
        Clock.getInstance().setTime(0);
        Patient.reset();
        gui.clearDisplays();

        // Start the simulation
        ((Thread) centre).start();
    }

    /**
     * Speeds up the simulation.
     */
    @Override
    public void speedUp() {
        centre.setDelay((long) (centre.getDelay() * 0.9));
    }

    /**
     * Slows down the simulation.
     */
    @Override
    public void slowDown() {
        centre.setDelay((long) (centre.getDelay() * 1.1));
    }

    /**
     * Adds a patient to the check-in canvas.
     */
    @Override
    public void addPatientToCheckInCanvas() {
        Platform.runLater(() -> gui.getCheckInCanvas().newPatient("sick"));
    }

    /**
     * Adds a patient to the doctor canvas.
     */
    @Override
    public void addPatientToDoctorCanvas() {
        Platform.runLater(() -> gui.getDoctorCanvas().newPatient("doctor"));
    }

    /**
     * Adds a patient to the x-ray canvas.
     */
    @Override
    public void addPatientToXRayCanvas() {
        Platform.runLater(() -> gui.getXrayCanvas().newPatient("xray"));
    }

    /**
     * Adds a patient to the lab canvas.
     */
    @Override
    public void addPatientToLabCanvas() {
        Platform.runLater(() -> gui.getLabCanvas().newPatient("lab"));
    }

    /**
     * Adds a patient to the treatment canvas.
     */
    @Override
    public void addPatientToTreatmentCanvas() {
        Platform.runLater(() -> gui.getTreatmentCanvas().newPatient("treatment"));
    }

    /**
     * Removes a patient from the check-in canvas.
     */
    @Override
    public void removePatientFromCheckInCanvas() {
        Platform.runLater(() -> gui.getCheckInCanvas().removePatient());
    }

    /**
     * Removes a patient from the doctor canvas.
     */
    @Override
    public void removePatientFromDoctorCanvas() {
        Platform.runLater(() -> gui.getDoctorCanvas().removePatient());
    }

    /**
     * Removes a patient from the x-ray canvas.
     */
    @Override
    public void removePatientFromXRayCanvas() {
        Platform.runLater(() -> gui.getXrayCanvas().removePatient());
    }

    /**
     * Removes a patient from the lab canvas.
     */
    @Override
    public void removePatientFromLabCanvas() {
        Platform.runLater(() -> gui.getLabCanvas().removePatient());
    }

    /**
     * Removes a patient from the treatment canvas.
     */
    @Override
    public void removePatientFromTreatmentCanvas() {
        Platform.runLater(() -> gui.getTreatmentCanvas().removePatient());
    }

    /**
     * Shows the statistics.
     * @param statistics The statistics to be shown
     */
    @Override
    public void showStatistics(String statistics) {
        Platform.runLater(() -> {
            String stats = centre.getStatistics();
            gui.showStatistics(stats);
        });
    }

    /**
     * Sets the delay for the simulation.
     * @param delay The delay in milliseconds.
     */
    @Override
    public void setDelay(long delay) {
        centre.setDelay(delay);
    }

    /**
     * Stops the simulation.
     */
    @Override
    public void stopSimulation() {
        centre.pauseThread();
    }

    /**
     * Resumes the simulation.
     */
    @Override
    public void resumeSimulation() {
        centre.resumeThread();
    }

    /**
     * Checks if the simulation is running.
     * @return true if the simulation is running, false if the simulation is not running
     */
    @Override
    public boolean isRunning() {
        return centre.isRunning();
    }

    /**
     * Handles the actions to be performed when the simulation ends.
     */
    @Override
    public void onSimulationEnd() {
        Platform.runLater(() -> {
            gui.endSimulation();
            gui.showStatistics(centre.getStatistics());

        });
    }

    /**
     * Updates the progress bar based on the current simulation time.
     */
    @Override
    public void updateProgressBar() {
        double currentTime = Clock.getInstance().getTime();
        double totalTime = centre.getSimulationTime();
        Platform.runLater(() -> gui.updateProgressBar(currentTime, totalTime));
    }

}
