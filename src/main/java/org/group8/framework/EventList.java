package org.group8.framework;

import java.util.PriorityQueue;

public class EventList {

    private PriorityQueue<Event> list = new PriorityQueue<Event>();

    public EventList() {

    }

    public Event remove() {
        Trace.out(Trace.Level.INFO, "Event " + list.peek().getType() + " is removed from event list: " + list.peek().getTime());
        return list.remove();
    }

    public void add(Event e) {
        Trace.out(Trace.Level.INFO, "Event " + e.getType() + " is added to event list: " + e.getTime());
        list.add(e);
    }

    public double getNextTime() {
        return list.peek().getTime();
    }
}
