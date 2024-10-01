package org.group8.simulator.model;

import jakarta.persistence.*;

@Entity
@Table(name = "average_time")
public class AverageTime {

    @Id
    @Column(name = "event", nullable = false, unique = true)
    private String event;

    @Column(name = "average_time", nullable = false)
    private double averageTime;

    // Constructors
    public AverageTime() {
    }

    public AverageTime(String event, double averageTime) {
        this.event = event;
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
}
