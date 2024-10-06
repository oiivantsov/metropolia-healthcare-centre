package org.group8.simulator.framework;

import org.group8.distributions.ContinuousGenerator;
import org.group8.distributions.SampleGenerator;

public class ArrivalProcess {

    private SampleGenerator generator;
    private EventList eventList;
    private IEventType type;

    public ArrivalProcess(SampleGenerator g, EventList list, IEventType type) {
        this.generator = g;
        this.eventList = list;
        this.type = type;
    }

    public void generateNext() {
        Event e = new Event(type, Clock.getInstance().getTime() + generator.sampleAsDouble());
        eventList.add(e);
    }
}
