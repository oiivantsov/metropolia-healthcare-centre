package org.group8.simulator.framework;

import java.util.PriorityQueue;

/**
 * The {@code EventList} class manages a priority queue of events for the simulation.
 * It allows for the addition and removal of events in a time-ordered manner,
 * ensuring that events are processed in the correct sequence based on their scheduled time.
 */
public class EventList {

    private PriorityQueue<Event> list = new PriorityQueue<Event>();

    /**
     * Constructs an empty {@code EventList}.
     */
    public EventList() {
    }


    /**
     * Removes and returns the next event from the event list.
     * The event removed is the one scheduled to occur the soonest.
     *
     * @return the event that is removed from the event list
     * @throws NullPointerException if the event list is empty
     */
    public Event remove() {
        Trace.out(Trace.Level.INFO, "Event " + list.peek().getType() + " is removed from event list: " + list.peek().getTime());
        return list.remove();
    }

    /**
     * Adds an event to the event list.
     * The event will be inserted into the priority queue, maintaining the order of events.
     *
     * @param e the event to be added to the event list
     */
    public void add(Event e) {
        Trace.out(Trace.Level.INFO, "Event " + e.getType() + " is added to event list: " + e.getTime());
        list.add(e);
    }

    /**
     * Retrieves the time of the next event scheduled to occur.
     *
     * @return the time of the next event, or {@code Double.MAX_VALUE} if the event list is empty
     */
    public double getNextTime() {
        if (list.isEmpty()) {
            return Double.MAX_VALUE; // to avoid null-exception when queue is empty
        }
        return list.peek().getTime();
    }

}
