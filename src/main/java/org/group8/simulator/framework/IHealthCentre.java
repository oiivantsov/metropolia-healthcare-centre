package org.group8.simulator.framework;

/**
 * The IHealthCentre interface defines the methods that must be implemented
 * by any class representing a health center in the simulation.
 * It allows the controller to interact with the simulation by setting parameters,
 * controlling the simulation flow, and retrieving statistics.
 */
public interface IHealthCentre {

    /**
     * Sets the total simulation time for the health center.
     *
     * @param time the total simulation time
     */
    void setSimulationTime(double time);

    /**
     * Gets the total simulation time set for the health center.
     *
     * @return the total simulation time
     */
    double getSimulationTime();

    /**
     * Sets the delay between simulation steps to control the simulation speed.
     *
     * @param time the delay time in milliseconds
     */
    void setDelay(long time);

    /**
     * Gets the delay between simulation steps.
     *
     * @return the delay time in milliseconds
     */
    long getDelay();

    /**
     * Retrieves the current simulation statistics, including patient flow and timing data.
     *
     * @return a string containing the simulation statistics
     */
    String getStatistics();

    /**
     * Pauses the simulation thread.
     */
    void pauseThread();

    /**
     * Resumes the simulation thread if it is paused.
     */
    void resumeThread();

    /**
     * Checks if the simulation is currently running.
     *
     * @return {@code true} if the simulation is running, {@code false} if it is paused
     */
    boolean isRunning();
}
