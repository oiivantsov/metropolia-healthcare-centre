package org.group8.simulator.framework;

import org.group8.distributions.ContinuousGenerator;
import org.group8.distributions.SampleGenerator;

/**
 * The {@code ArrivalProcess} class handles the simulation of arrival events.
 * It uses a {@code SampleGenerator} to determine the time between arrivals and schedules
 * the next event in the {@code EventList}.
 */
public class ArrivalProcess {

    private SampleGenerator generator;
    private EventList eventList;
    private IEventType type;

    /**
     * Constructs an {@code ArrivalProcess} object.
     *
     * @param g     the sample generator that determines arrival times
     * @param list  the event list where events are scheduled
     * @param type  the type of event to schedule
     */
    public ArrivalProcess(SampleGenerator g, EventList list, IEventType type) {
        this.generator = g;
        this.eventList = list;
        this.type = type;
    }

    /**
     * Generates and schedules the next arrival event.
     * The time of the next event is based on the current time and a sampled time.
     */
    public void generateNext() {
        Event e = new Event(type, Clock.getInstance().getTime() + generator.sampleAsDouble());
        eventList.add(e);
    }
}
