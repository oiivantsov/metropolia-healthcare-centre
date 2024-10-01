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
    void setProbabilities(double lab, double xray, double treatment);
    void setAverageTimes(double checkIn, double doctor, double xray, double lab, double treatment, double arrival);
    double getProbability(String decisionType);
    double getAverageTime(String event);
}
