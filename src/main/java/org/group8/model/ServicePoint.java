package org.group8.model;

import org.group8.distributions.*;
import org.group8.framework.*;

import java.util.LinkedList;
import java.util.Queue;

public class ServicePoint {

    private final Queue<Patient> queue = new LinkedList<>();
    private final ContinuousGenerator generator;
    private final EventList eventList;
    private final EventType scheduledEventType;

    private boolean busy = false;

    public ServicePoint(ContinuousGenerator g, EventList list, EventType type) {
        this.generator = g;
        this.eventList = list;
        this.scheduledEventType = type;
    }

    public void addToQueue(Patient p) {
        queue.add(p);
    }

    public Patient removeFromQueue() {
        busy = false;
        return queue.poll();
    }

    public void startService() {
        if (queue.isEmpty()) return; // to avoid null-exception when queue is empty
        Trace.out(Trace.Level.INFO, "Starting service for patient " + queue.peek().getId());

        busy = true;
        Patient p = queue.peek();
        double serviceTime = generator.sample();
        eventList.add(new Event(scheduledEventType, Clock.getInstance().getTime() + serviceTime));
    }

    public boolean isBusy() {
        return busy;
    }

    public boolean hasQueue() {
        return !queue.isEmpty();
    }
}
