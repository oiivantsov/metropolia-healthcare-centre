package org.group8.view;

public interface IHealthcenterGUI {

    // The controller needs inputs, which it forwards to the Healthcenter
    double getTime();  // Method to get the current time
    long getDelay();   // Method to get the delay

    void showStatistics(String statistics);
    void endSimulation();
    void clearDisplays();

    IVisualization getCheckInCanvas();
    IVisualization getDoctorCanvas();
    IVisualization getLabCanvas();
    IVisualization getXrayCanvas();
    IVisualization getTreatmentCanvas();

}
