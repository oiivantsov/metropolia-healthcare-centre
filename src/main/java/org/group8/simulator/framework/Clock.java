package org.group8.simulator.framework;

/**
 * The {@code Clock} class is a singleton that manages the simulation time.
 * It provides methods to set and retrieve the current simulation time.
 */
public class Clock {
    private double time;
    private static Clock instance;


    /**
     * Private constructor to prevent external instantiation.
     * Initializes the simulation time to zero.
     */
    private Clock() {
        time = 0;
    }

    /**
     * Returns the singleton instance of the {@code Clock}.
     * If the instance does not exist, it is created.
     *
     * @return the singleton instance of the {@code Clock}
     */
    public static Clock getInstance() {
        if (instance == null) {
            instance = new Clock();
        }
        return instance;
    }

    /**
     * Sets the current time of the simulation.
     *
     * @param time the new simulation time to set
     */
    public void setTime(double time) {
        this.time = time;
    }

    /**
     * Retrieves the current simulation time.
     *
     * @return the current simulation time
     */
    public double getTime() {
        return time;
    }
}
