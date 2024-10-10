package org.group8.simulator.framework;

/**
 * The Event class represents an event in the simulation, characterized by its type and the time it occurs.
 * It implements {@link Comparable} to allow events to be sorted based on their occurrence time.
 */
public class Event implements Comparable<Event> {

    private IEventType type;
    private double time;

    /**
     * Constructs an Event with the specified type and time.
     *
     * @param type the type of the event (e.g., arrival, departure)
     * @param time the time at which the event occurs
     */
    public Event(IEventType type, double time) {
        this.type = type;
        this.time = time;
    }

    /**
     * Sets the type of the event.
     *
     * @param type the type of the event to set
     */
    public void setType(IEventType type) {
        this.type = type;
    }

    /**
     * Returns the type of the event.
     *
     * @return the event type
     */
    public IEventType getType() {
        return this.type;
    }

    /**
     * Sets the time of the event.
     *
     * @param time the time to set for the event
     */
    public void setTime(double time) {
        this.time = time;
    }

    /**
     * Returns the time of the event.
     *
     * @return the event time
     */
    public double getTime() {
        return this.time;
    }

    /**
     * Compares this event to another event based on their time.
     *
     * @param other the other event to compare with
     * @return -1 if this event occurs earlier, 1 if later, and 0 if at the same time
     */
    @Override
    public int compareTo(Event other) {
        if (this.time < other.time) return -1;
        else if (this.time > other.time) return 1;
        return 0;
    }

}
