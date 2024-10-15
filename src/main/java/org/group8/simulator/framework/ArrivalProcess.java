package org.group8.simulator.framework;

import org.group8.distributions.ContinuousGenerator;
import org.group8.distributions.SampleGenerator;

/**
 * The ArrivalProcess class models the process of generating arrival events in the simulation.
 * It generates events based on a specified time distribution and adds them to the event list.
 */
public class ArrivalProcess {

    private SampleGenerator generator;
    private EventList eventList;
    private IEventType type;

    /**
     * Constructs a new ArrivalProcess with the specified generator, event list, and event type.
     *
     * @param g     the generator to generate the next event time (e.g., based on a distribution)
     * @param list  the event list where the generated event will be added
     * @param type  the type of event to generate (e.g., arrival at a specific service point)
     */
    public ArrivalProcess(SampleGenerator g, EventList list, IEventType type) {
        this.generator = g;
        this.eventList = list;
        this.type = type;
    }

    /**
     * Generates the next event by sampling from the generator and adding the event
     * to the event list with a time offset.
     */
    public void generateNext() {
        Event e = new Event(type, Clock.getInstance().getTime() + generator.sampleAsDouble());
        eventList.add(e);
    }
}
