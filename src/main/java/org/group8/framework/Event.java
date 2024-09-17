package org.group8.framework;

public class Event implements Comparable<Event> {

    private IEventType type;
    private double time;

    public Event(IEventType type, double time) {
        this.type = type;
        this.time = time;
    }

    public void setType(IEventType type) {
        this.type = type;
    }

    public IEventType getType() {
        return this.type;
    }

    public void setTime(double time) {
        this.time = time;
    }

    public double getTime() {
        return this.time;
    }

    @Override
    public int compareTo(Event other) {
        if (this.time < other.time) return -1;
        else if (this.time > other.time) return 1;
        return 0;
    }

}
