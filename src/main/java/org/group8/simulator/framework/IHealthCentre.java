package org.group8.simulator.framework;

public interface IHealthCentre {
    // The controller uses this interface

    void setSimulationTime(double time);
    double getSimulationTime();
    void setDelay(long time);
    long getDelay();
    String getStatistics();
    void pauseThread();
    void resumeThread();
    boolean isRunning();
}
