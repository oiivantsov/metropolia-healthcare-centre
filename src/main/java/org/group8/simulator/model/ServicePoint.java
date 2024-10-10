package org.group8.simulator.model;

import org.group8.distributions.*;
import org.group8.simulator.framework.Clock;
import org.group8.simulator.framework.Event;
import org.group8.simulator.framework.EventList;
import org.group8.simulator.framework.Trace;

import java.util.LinkedList;
import java.util.Queue;

/**
 * The ServicePoint class represents a point in the healthcare simulation where patients receive services.
 * Each service point has a queue for waiting patients, and it generates events based on service times
 * determined by a given distribution.
 */
public class ServicePoint {

    private final Queue<Patient> queue = new LinkedList<>();
    private final SampleGenerator generator;  // Generates service times
    private final EventList eventList;
    private final EventType scheduledEventType;

    private boolean busy = false;

    /**
     * Constructs a new ServicePoint with a specified service time generator, event list, and event type.
     *
     * @param g    the generator for service times (e.g., negexp, poisson)
     * @param list the event list to which service completion events will be added
     * @param type the type of event scheduled after service completion (e.g., departure)
     */
    public ServicePoint(SampleGenerator g, EventList list, EventType type) {
        this.generator = g;
        this.eventList = list;
        this.scheduledEventType = type;
    }

    /**
     * Adds a patient to the service point queue.
     *
     * @param p the patient to add to the queue
     */
    public void addToQueue(Patient p) {
        queue.add(p);
    }

    /**
     * Removes a patient from the service point queue, marking the service point as no longer busy.
     *
     * @return the patient removed from the queue
     */
    public Patient removeFromQueue() {
        busy = false;
        return queue.poll();
    }

    /**
     * Starts the service for the next patient in the queue. It generates a service completion event
     * based on the service time sampled from the generator.
     */
    public void startService() {
        if (queue.isEmpty()) return; // Avoid null-exception when queue is empty
        Trace.out(Trace.Level.INFO, "Starting service for patient " + queue.peek().getId());

        busy = true;
        Patient p = queue.peek();
        double serviceTime = generator.sampleAsDouble();
        eventList.add(new Event(scheduledEventType, Clock.getInstance().getTime() + serviceTime));
    }

    /**
     * Checks if the service point is currently busy.
     *
     * @return {@code true} if the service point is busy, {@code false} otherwise
     */
    public boolean isBusy() {
        return busy;
    }

    /**
     * Checks if there are patients in the queue.
     *
     * @return {@code true} if the queue is not empty, {@code false} otherwise
     */
    public boolean hasQueue() {
        return !queue.isEmpty();
    }
}
