package org.group8.controller;

import javafx.application.Platform;
import org.group8.simulator.framework.Clock;
import org.group8.simulator.framework.IHealthCentre;
import org.group8.simulator.model.AverageTimeConfig;
import org.group8.simulator.model.DecisionProbability;
import org.group8.simulator.model.HealthCentre;
import org.group8.simulator.model.Patient;
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
        Platform.runLater(() -> gui.getCheckInCanvas().newPatient());
    }

    @Override
    public void addPatientToDoctorCanvas() {
        Platform.runLater(() -> gui.getDoctorCanvas().newPatient());
    }

    @Override
    public void addPatientToXRayCanvas() {
        Platform.runLater(() -> gui.getXrayCanvas().newPatient());
    }

    @Override
    public void addPatientToLabCanvas() {
        Platform.runLater(() -> gui.getLabCanvas().newPatient());
    }

    @Override
    public void addPatientToTreatmentCanvas() {
        Platform.runLater(() -> gui.getTreatmentCanvas().newPatient());
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
    public void setProbabilities(double lab, double xray, double treatment) {
        DecisionProbability.setProbabilities(lab, xray, treatment);
    }

    @Override
    public void setAverageTimes(double checkIn, double doctor, double lab, double xray, double treatment, double arrival) {
        AverageTimeConfig.setAllParameters(checkIn, doctor, lab, xray, treatment, arrival);
    }

    @Override
    public void onSimulationEnd() {
        Platform.runLater(() -> {
            gui.showSimulationEndAlert();
            gui.showStatistics(centre.getStatistics());
        });
    }
}
