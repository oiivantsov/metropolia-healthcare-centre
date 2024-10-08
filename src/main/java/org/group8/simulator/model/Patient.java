package org.group8.simulator.model;

import org.group8.simulator.framework.Clock;
import org.group8.simulator.framework.Trace;

/**
 * The Patient class represents a patient in the healthcare simulation.
 * It tracks the patient's arrival and departure times, as well as
 * system-wide statistics such as the total time patients have spent in the system
 * and the number of patients completed.
 */
public class Patient {
    private double arrivalTime;
    private double departureTime;
    private int id;

    /** Counter to track the total number of patients created */
    private static int counter = 0;

    /** Total time spent by all patients in the system */
    private static double totalTime = 0;

    /** Total number of patients that have completed their visit */
    private static int completedPatients = 0;

    /**
     * Constructor for creating a new Patient object.
     * Automatically assigns a unique ID and logs the patient's arrival time.
     */
    public Patient() {
        this.id = ++counter;
        this.arrivalTime = Clock.getInstance().getTime();
        Trace.out(Trace.Level.INFO, "New patient #" + this.id + " arrived at: " + this.arrivalTime);
    }

    /**
     * Gets the departure time of the patient.
     *
     * @return the departure time of the patient.
     */
    public double getDepartureTime() {
        return this.departureTime;
    }

    /**
     * Sets the departure time for the patient.
     *
     * @param departureTime The time the patient departs.
     */
    public void setDepartureTime(double departureTime) {
        this.departureTime = departureTime;
    }

    /**
     * Generates a report for the patient, logging their visit details.
     * Updates system-wide statistics such as total time spent in the system
     * and the number of completed patients.
     */
    public void report() {
        totalTime += this.departureTime - this.arrivalTime;
        completedPatients++;

        System.out.println();
        System.out.println("--- Patient Report ---");
        Trace.out(Trace.Level.INFO, "Patient " + this.id + " completed the visit.");
        Trace.out(Trace.Level.INFO, "Patient " + this.id + " arrived at " + this.arrivalTime + " and departed at " + this.departureTime);
        Trace.out(Trace.Level.INFO, "Patient " + this.id + " spent " + (this.departureTime - this.arrivalTime) + " time units in the system.");

        double averageTime = totalTime / completedPatients;
        System.out.println("For now, average time spent by all patients: " + averageTime);
    }

    /**
     * Returns the ID of the patient as a string.
     *
     * @return the ID of the patient.
     */
    public String getId() {
        return String.valueOf(this.id);
    }

    /**
     * Returns the total time spent by all patients in the system.
     *
     * @return total time spent by all patients.
     */
    public static double getTotalTime() {
        return totalTime;
    }

    /**
     * Returns the total number of patients created.
     *
     * @return the total number of patients.
     */
    public static int getTotalPatients() {
        return counter;
    }

    /**
     * Returns the number of patients that have completed their visit.
     *
     * @return the number of completed patients.
     */
    public static int getCompletedPatients() {
        return completedPatients;
    }

    /**
     * Resets the static fields that track the total number of patients,
     * total time spent in the system, and the number of completed patients.
     * This is useful for resetting the system state between simulations.
     */
    public static void reset() {
        counter = 0;
        totalTime = 0;
        completedPatients = 0;
    }

    /**
     * Gets the arrival time of the patient.
     *
     * @return the arrival time of the patient.
     */
    public double getArrivalTime() {
        return this.arrivalTime;
    }
}
