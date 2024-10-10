package org.group8.controller;

/**
 * Interface for controlling the health center simulation from the user interface.
 */
public interface IControllerForV {

    // Interface provided to the user interface:

    void startSimulation();
    void speedUp();
    void slowDown();
    void setTime(int time);
    void showStatistics(String statistics);
    void setDelay(long delay);
    void stopSimulation();
    void resumeSimulation();
    boolean isRunning();
}
