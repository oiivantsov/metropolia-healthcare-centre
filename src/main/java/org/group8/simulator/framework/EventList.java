package org.group8.simulator.framework;

import java.util.PriorityQueue;

/**
 * The EventList class manages a list of simulation events using a priority queue.
 * Events are processed in order based on their scheduled time.
 */
public class EventList {

    private PriorityQueue<Event> list = new PriorityQueue<Event>();

    /**
     * Constructs an empty EventList.
     */
    public EventList() {
    }

    /**
     * Removes and returns the event with the earliest time from the event list.
     *
     * @return the event with the earliest time
     */
    public Event remove() {
        Trace.out(Trace.Level.INFO, "Event " + list.peek().getType() + " is removed from event list: " + list.peek().getTime());
        return list.remove();
    }

    /**
     * Adds a new event to the event list. Events are sorted by time, with the earliest
     * events being processed first.
     *
     * @param e the event to add to the list
     */
    public void add(Event e) {
        Trace.out(Trace.Level.INFO, "Event " + e.getType() + " is added to event list: " + e.getTime());
        list.add(e);
    }

    /**
     * Returns the time of the next event to be processed, or {@code Double.MAX_VALUE} if
     * the list is empty, to avoid null pointer exceptions.
     *
     * @return the time of the next event, or {@code Double.MAX_VALUE} if the list is empty
     */
    public double getNextTime() {
        if (list.isEmpty()) {
            return Double.MAX_VALUE; // Avoid null-exception when queue is empty
        }
        return list.peek().getTime();
    }
}
