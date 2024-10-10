package org.group8.simulator.framework;

/**
 * The Clock class represents a simulation clock that keeps track of the current simulation time.
 * It follows the singleton pattern to ensure only one instance of the clock is used throughout the simulation.
 */
public class Clock {

    private double time;
    private static Clock instance;

    /**
     * Private constructor to prevent instantiation from outside the class.
     * Initializes the simulation time to 0.
     */
    private Clock() {
        time = 0;
    }

    /**
     * Returns the singleton instance of the Clock. If no instance exists, it creates one.
     *
     * @return the singleton instance of the Clock
     */
    public static Clock getInstance() {
        if (instance == null) {
            instance = new Clock();
        }
        return instance;
    }

    /**
     * Sets the current simulation time.
     *
     * @param time the current time to set for the simulation
     */
    public void setTime(double time) {
        this.time = time;
    }

    /**
     * Returns the current simulation time.
     *
     * @return the current simulation time
     */
    public double getTime() {
        return time;
    }
}
