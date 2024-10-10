package org.group8.simulator.model;

import jakarta.persistence.*;

/**
 * Represents a distribution entity for an event in the simulation.
 * This class is mapped to the "distribution" table in the database.
 * It contains information about the event, its associated distribution,
 * and the average time for the event.
 */
@Entity
@Table(name = "distribution")
public class Distribution {

    /**
     * The unique event name associated with the distribution.
     * This field is the primary key for the "distribution" table.
     */
    @Id
    @Column(name = "event", nullable = false, unique = true)
    private String event;

    /**
     * The type of distribution (e.g., normal, negexp, uniform) used for the event.
     */
    @Column(name = "distribution", nullable = false)
    private String distribution;

    /**
     * The average time (in seconds or another unit) associated with the event's distribution.
     */
    @Column(name = "average_time", nullable = false)
    private double averageTime;

    // Constructors

    /**
     * Default constructor for the Distribution class.
     * Required by JPA for entity instantiation.
     */
    public Distribution() {
    }

    /**
     * Constructs a new Distribution with the specified event, distribution type,
     * and average time.
     *
     * @param event        the name of the event
     * @param distribution the type of distribution used for the event
     * @param averageTime  the average time associated with the distribution
     */
    public Distribution(String event, String distribution, double averageTime) {
        this.event = event;
        this.distribution = distribution;
        this.averageTime = averageTime;
    }

    // Getters and Setters

    /**
     * Gets the event name associated with this distribution.
     *
     * @return the event name
     */
    public String getEvent() {
        return event;
    }

    /**
     * Sets the event name for this distribution.
     *
     * @param event the event name to set
     */
    public void setEvent(String event) {
        this.event = event;
    }

    /**
     * Gets the average time associated with this distribution.
     *
     * @return the average time
     */
    public double getAverageTime() {
        return averageTime;
    }

    /**
     * Sets the average time for this distribution.
     *
     * @param averageTime the average time to set
     */
    public void setAverageTime(double averageTime) {
        this.averageTime = averageTime;
    }

    /**
     * Gets the type of distribution for this event.
     *
     * @return the distribution type
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
