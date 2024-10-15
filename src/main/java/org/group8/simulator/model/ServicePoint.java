package org.group8.simulator.model;

import org.group8.distributions.*;
import org.group8.simulator.framework.Clock;
import org.group8.simulator.framework.Event;
import org.group8.simulator.framework.EventList;
import org.group8.simulator.framework.Trace;

import java.util.LinkedList;
import java.util.Queue;

/**
 * The {@code ServicePoint} class represents a service point where patients are queued for service.
 * It uses a {@link SampleGenerator} to generate service times and an {@link EventList} to schedule events.
 */
public class ServicePoint {

    /**
     * A queue that holds patients waiting for service.
     */
    private final Queue<Patient> queue = new LinkedList<>();

    /**
     * A generator that provides random service times for the patients.
     */
    private final SampleGenerator generator;  // Use the common interface


    /**
     * The event list where events are scheduled after service is completed.
     */
    private final EventList eventList;

    /**
     * The type of event to schedule after service is completed.
     */
    private final EventType scheduledEventType;

    /**
     * Flag to indicate whether the service point is currently busy.
     */
    private boolean busy = false;

    /**
     * Constructs a new {@code ServicePoint}.
     *
     * @param g     the sample generator used to generate service times
     * @param list  the event list to add events to
     * @param type  the type of event to schedule after a service is completed
     */
    public ServicePoint(SampleGenerator g, EventList list, EventType type) {
        this.generator = g;
        this.eventList = list;
        this.scheduledEventType = type;
    }

    /**
     * Adds a patient to the queue for service.
     *
     * @param p the patient to be added to the queue
     */
    public void addToQueue(Patient p) {
        queue.add(p);
    }

    /**
     * Removes and returns the patient at the front of the queue.
     * Sets the service point to not busy.
     *
     * @return the patient removed from the front of the queue, or {@code null} if the queue is empty
     */
    public Patient removeFromQueue() {
        busy = false;
        return queue.poll();
    }

    /**
     * Starts the service for the patient at the front of the queue.
     * If the queue is empty, no service is started.
     * An event is scheduled for when the service is expected to finish.
     */
    public void startService() {
        if (queue.isEmpty()) return; // to avoid null-exception when queue is empty
        Trace.out(Trace.Level.INFO, "Starting service for patient " + queue.peek().getId());

        busy = true;
        Patient p = queue.peek();
        double serviceTime = generator.sampleAsDouble();
        eventList.add(new Event(scheduledEventType, Clock.getInstance().getTime() + serviceTime));
    }

    /**
     * Returns whether the service point is currently busy.
     *
     * @return {@code true} if the service point is busy, otherwise {@code false}
     */
    public boolean isBusy() {
        return busy;
    }

    /**
     * Checks if there are any patients in the queue.
     *
     * @return {@code true} if the queue is not empty, otherwise {@code false}
     */
    public boolean hasQueue() {
        return !queue.isEmpty();
    }
}
