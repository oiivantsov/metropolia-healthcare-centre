package org.group8.simulator.model;

import jakarta.persistence.*;

/**
 * The {@code Distribution} class represents a distribution entity that is used to define
 * the statistical distribution for different simulation events, along with their average time.
 * This entity is persisted in the database.
 */
@Entity
@Table(name = "distribution")
public class Distribution {

    /** The event associated with this distribution, serving as the unique identifier. */
    @Id
    @Column(name = "event", nullable = false, unique = true)
    private String event;

    /** The type of statistical distribution (e.g., negexp, poisson) used for this event. */
    @Column(name = "distribution", nullable = false)
    private String distribution;

    /** The average time associated with this event's distribution. */
    @Column(name = "average_time", nullable = false)
    private double averageTime;

    // Constructors
    /**
     * Default constructor for JPA.
     */
    public Distribution() {
    }

    /**
     * Constructs a new {@code Distribution} with the specified event, distribution type, and average time.
     *
     * @param event the event associated with this distribution
     * @param distribution the type of statistical distribution
     * @param averageTime the average time for the event
     */
    public Distribution(String event, String distribution, double averageTime) {
        this.event = event;
        this.distribution = distribution;
        this.averageTime = averageTime;
    }

    // Getters and Setters
    /**
     * Returns the event associated with this distribution.
     *
     * @return the event as a {@code String}
     */
    public String getEvent() {
        return event;
    }

    /**
     * Sets the event for this distribution.
     *
     * @param event the event to set
     */
    public void setEvent(String event) {
        this.event = event;
    }

    /**
     * Returns the average time for this event.
     *
     * @return the average time as a {@code double}
     */
    public double getAverageTime() {
        return averageTime;
    }

    /**
     * Sets the average time for this event.
     *
     * @param averageTime the average time to set
     */
    public void setAverageTime(double averageTime) {
        this.averageTime = averageTime;
    }

    /**
     * Returns the type of distribution used for this event.
     *
     * @return the distribution type as a {@code String}
     */
    public String getDistribution() {
        return distribution;
    }

    /**
     * Sets the type of distribution for this event.
     *
     * @param distribution the distribution type to set
     */
    public void setDistribution(String distribution) {
        this.distribution = distribution;
    }
}
