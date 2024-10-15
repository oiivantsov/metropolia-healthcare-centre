package org.group8.view;

/**
 * The IHealthcenterGUI interface defines the methods required for the graphical user interface
 * of the health center simulation. It provides input methods for the controller and allows
 * interaction with visualizations of the simulation.
 */
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

    void updateProgressBar(double currentTime, double endTime);
}
