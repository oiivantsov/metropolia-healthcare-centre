package org.group8.simulator.framework;

public interface IHealthCentre {
    // The controller uses this interface

    void setSimulationTime(double time);
    void setDelay(long time);
    long getDelay();
    String getStatistics();
    void pauseThread();
    void resumeThread();
    boolean isRunning();
}
