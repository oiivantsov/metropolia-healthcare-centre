package org.group8.controller;

import javafx.application.Platform;
import org.group8.simulator.framework.IHealthCentre;
import org.group8.simulator.model.DecisionProbability;
import org.group8.simulator.model.HealthCentre;
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
        centre = new HealthCentre(this); // new thread for each simulation
        centre.setSimulationTime(gui.getTime());
        centre.setDelay(gui.getDelay());
        gui.clearDisplays();
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
        Platform.runLater(new Runnable() {
            public void run() {
                gui.getCheckInCanvas().newPatient();
            }
        });
    }

    @Override
    public void addPatientToDoctorCanvas() {
        Platform.runLater(new Runnable() {
            public void run() {
                gui.getDoctorCanvas().newPatient();
            }
        });
    }

    @Override
    public void addPatientToXRayCanvas() {
        Platform.runLater(new Runnable() {
            public void run() {
                gui.getXrayCanvas().newPatient();
            }
        });
    }

    @Override
    public void addPatientToLabCanvas() {
        Platform.runLater(new Runnable() {
            public void run() {
                gui.getLabCanvas().newPatient();
            }
        });
    }

    @Override
    public void addPatientToTreatmentCanvas() {
        Platform.runLater(new Runnable() {
            public void run() {
                gui.getTreatmentCanvas().newPatient();
            }
        });
    }

    @Override
    public void showStatistics(String statistics) {
        Platform.runLater(new Runnable() {
            public void run() {
                String statistics = centre.getStatistics();
                gui.showStatistics(statistics);
            }
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

}
