package org.group8.simulator.framework;

/**
 * The {@code IHealthCentre} interface defines the methods that a health center simulation must implement.
 * It is intended to provide control and management functionality for the simulation, allowing for
 * configuration of simulation parameters and handling of simulation state.
 */
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
