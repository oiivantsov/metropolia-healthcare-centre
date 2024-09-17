package org.group8.model;

import org.group8.framework.Clock;
import org.group8.framework.Trace;

public class Patient {
    private double arrivalTime;
    private double departureTime;
    private int id;
    private static int counter = 0;
    private static long totalTime = 0;

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
        Trace.out(Trace.Level.INFO, "Patient " + this.id + " completed the visit.");
        totalTime += (this.departureTime - this.arrivalTime);
        double averageTime = (double) totalTime / id;
        System.out.println("Average time spent by patients: " + averageTime);
    }

    public String getId() {
        return String.valueOf(this.id);
    }
}
