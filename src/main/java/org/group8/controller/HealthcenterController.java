package org.group8.controller;

import javafx.application.Platform;
import org.group8.simulator.framework.Clock;
import org.group8.simulator.framework.IHealthCentre;
import org.group8.simulator.model.*;
import org.group8.view.IHealthcenterGUI;

public class HealthcenterController implements IControllerForP, IControllerForV {

    private IHealthcenterGUI gui;
    private IHealthCentre centre;

    public HealthcenterController(IHealthcenterGUI gui) {
        this.gui = gui;
    }

    public void setTime(int time) {
        centre.setSimulationTime(time);
    }

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

    @Override
    public void speedUp() {
        centre.setDelay((long) (centre.getDelay() * 0.9));
    }

    @Override
    public void slowDown() {
        centre.setDelay((long) (centre.getDelay() * 1.1));
    }

    @Override
    public void addPatientToCheckInCanvas() {
        Platform.runLater(() -> gui.getCheckInCanvas().newPatient("sick"));
    }

    @Override
    public void addPatientToDoctorCanvas() {
        Platform.runLater(() -> gui.getDoctorCanvas().newPatient("doctor"));
    }

    @Override
    public void addPatientToXRayCanvas() {
        Platform.runLater(() -> gui.getXrayCanvas().newPatient("xray"));
    }

    @Override
    public void addPatientToLabCanvas() {
        Platform.runLater(() -> gui.getLabCanvas().newPatient("lab"));
    }

    @Override
    public void addPatientToTreatmentCanvas() {
        Platform.runLater(() -> gui.getTreatmentCanvas().newPatient("treatment"));
    }

    @Override
    public void removePatientFromCheckInCanvas() {
        Platform.runLater(() -> gui.getCheckInCanvas().removePatient());
    }

    @Override
    public void removePatientFromDoctorCanvas() {
        Platform.runLater(() -> gui.getDoctorCanvas().removePatient());
    }

    @Override
    public void removePatientFromXRayCanvas() {
        Platform.runLater(() -> gui.getXrayCanvas().removePatient());
    }

    @Override
    public void removePatientFromLabCanvas() {
        Platform.runLater(() -> gui.getLabCanvas().removePatient());
    }

    @Override
    public void removePatientFromTreatmentCanvas() {
        Platform.runLater(() -> gui.getTreatmentCanvas().removePatient());
    }

    @Override
    public void showStatistics(String statistics) {
        Platform.runLater(() -> {
            String stats = centre.getStatistics();
            gui.showStatistics(stats);
        });
    }

    @Override
    public void setDelay(long delay) {
        centre.setDelay(delay);
    }

    @Override
    public void stopSimulation() {
        centre.pauseThread();
    }

    @Override
    public void resumeSimulation() {
        centre.resumeThread();
    }

    @Override
    public boolean isRunning() {
        return centre.isRunning();
    }

    @Override
    public void onSimulationEnd() {
        Platform.runLater(() -> {
            gui.endSimulation();
            gui.showStatistics(centre.getStatistics());

        });
    }

    @Override
    public void updateProgressBar() {
        double currentTime = Clock.getInstance().getTime();
        double totalTime = centre.getSimulationTime();
        Platform.runLater(() -> gui.updateProgressBar(currentTime, totalTime));
    }

}
