package org.group8.simulator.model;

import jakarta.persistence.*;

@Entity
@Table(name = "distribution")
public class Distribution {

    @Id
    @Column(name = "event", nullable = false, unique = true)
    private String event;

    @Column(name = "distribution", nullable = false)
    private String distribution;

    @Column(name = "average_time", nullable = false)
    private double averageTime;

    // Constructors
    public Distribution() {
    }

    public Distribution(String event, String distribution, double averageTime) {
        this.event = event;
        this.distribution = distribution;
        this.averageTime = averageTime;
    }

    // Getters and Setters
    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public double getAverageTime() {
        return averageTime;
    }

    public void setAverageTime(double averageTime) {
        this.averageTime = averageTime;
    }

    public String getDistribution() {
        return distribution;
    }

    public void setDistribution(String distribution) {
        this.distribution = distribution;
    }
}
