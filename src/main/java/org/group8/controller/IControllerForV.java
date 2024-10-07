package org.group8.controller;

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
