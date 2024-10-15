package org.group8.simulator.framework;

/**
 * The {@code Event} class represents an event in the simulation, characterized by its type and the time it occurs.
 * Each event is comparable based on its time, allowing for ordered event handling in the simulation process.
 */
public class Event implements Comparable<Event> {

    private IEventType type;
    private double time;

    /**
     * Constructs an {@code Event} with the specified type and time.
     *
     * @param type the type of the event
     * @param time the time at which the event occurs
     */
    public Event(IEventType type, double time) {
        this.type = type;
        this.time = time;
    }

    /**
     * Sets the type of the event.
     *
     * @param type the new type of the event
     */
    public void setType(IEventType type) {
        this.type = type;
    }

    /**
     * Retrieves the type of the event.
     *
     * @return the type of the event
     */
    public IEventType getType() {
        return this.type;
    }

    /**
     * Sets the time of the event.
     *
     * @param time the new time of the event
     */
    public void setTime(double time) {
        this.time = time;
    }

    /**
     * Retrieves the time at which the event occurs.
     *
     * @return the time of the event
     */
    public double getTime() {
        return this.time;
    }

    /**
     * Compares this event with another event for order based on their occurrence time.
     *
     * @param other the event to be compared
     * @return a negative integer, zero, or a positive integer as this event occurs
     *         before, at the same time as, or after the specified event
     */
    @Override
    public int compareTo(Event other) {
        if (this.time < other.time) return -1;
        else if (this.time > other.time) return 1;
        return 0;
    }

}
