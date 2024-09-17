package org.group8.framework;

import org.group8.distributions.ContinuousGenerator;

public class ArrivalProcess {

    private ContinuousGenerator generator;
    private EventList eventList;
    private IEventType type;

    public ArrivalProcess(ContinuousGenerator g, EventList list, IEventType type) {
        this.generator = g;
        this.eventList = list;
        this.type = type;
    }

    public void generateNext() {
        Event e = new Event(type, Clock.getInstance().getTime() + generator.sample());
        eventList.add(e);
    }
}
