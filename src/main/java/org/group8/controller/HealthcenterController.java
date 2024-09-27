package org.group8.controller;

import org.group8.view.HealthcenterGUI;
import org.group8.model.HealthCentre;
public class HealthcenterController {

    private HealthcenterGUI gui;
    private HealthCentre centre = new HealthCentre();

    public HealthcenterController(HealthcenterGUI gui) {
        this.gui = gui;
    }
    public void setTime(int time) {
        centre.setSimulationTime(time);
    }
    public void runCentre() {
        centre.run();
        String centreOutput = centre.getOutputs();
        gui.setResult(centreOutput);
    }
}
