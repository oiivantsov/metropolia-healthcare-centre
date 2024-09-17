package org.group8.model;

import org.group8.framework.Clock;
import org.group8.framework.Trace;

public class Patient {
    private double arrivalTime;
    private double departureTime;
    private int id;
    private static int counter = 0;
    private static double totalTime = 0;
    private static int completedPatients = 0;

    public Patient() {
        this.id = ++counter;
        this.arrivalTime = Clock.getInstance().getTime();
        Trace.out(Trace.Level.INFO, "New patient #" + this.id + " arrived at: " + this.arrivalTime);
    }

    public double getDepartureTime() {
        return this.departureTime;
    }

    public void setDepartureTime(double departureTime) {
        this.departureTime = departureTime;
    }

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

    public String getId() {
        return String.valueOf(this.id);
    }

    public static double getTotalTime() {
        return totalTime;
    }

    public static int getTotalPatients() {
        return counter;
    }

    public static int getCompletedPatients() {
        return completedPatients;
    }
}
