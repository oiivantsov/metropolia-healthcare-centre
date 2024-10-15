package org.group8.simulator.framework;

import org.group8.controller.IControllerForP;

/**
 * The {@code AbstractHealthCentre} class is an abstract base class for simulating a healthcare center.
 * It extends {@code Thread} and manages the simulation flow, event processing, and thread control (pause/resume).
 * Subclasses must implement the abstract methods to define event handling and simulation statistics.
 */
public abstract class AbstractHealthCentre extends Thread implements IHealthCentre {

    /** The total simulation time. */
    private double simulationTime = 0;

    /** Delay time between simulation steps (in milliseconds). */
    private long delay = 0;

    // Object and boolean to control the simulation's thread
    /** Lock object to control thread pausing and resuming. */
    private final Object lock = new Object();

    /** Boolean flag to control the pause state of the simulation thread. */
    private boolean pause = false;

    /** The clock object used to track simulation time. */
    private Clock clock;

    /** The list of events to be processed during the simulation. */
    protected EventList eventList;

    /** Controller interface for updating the UI or external components during the simulation. */
    protected IControllerForP controller;

    /**
     * Constructor to initialize the health center simulation.
     *
     * @param controller the controller interface for handling simulation updates
     */
    public AbstractHealthCentre(IControllerForP controller) {
        this.controller = controller;
        clock = Clock.getInstance();
        eventList = new EventList();
    }

    /**
     * Sets the total simulation time.
     *
     * @param time the total simulation time
     */
    @Override
    public void setSimulationTime(double time) {
        simulationTime = time;
    }

    /**
     * Returns the total simulation time.
     *
     * @return the total simulation time
     */
    @Override
    public double getSimulationTime() {
        return simulationTime;
    }

    /**
     * Sets the delay between simulation steps.
     *
     * @param time the delay time in milliseconds
     */
    @Override
    public void setDelay(long time) {
        delay = time;
    }


    /**
     * Returns the delay between simulation steps.
     *
     * @return the delay time in milliseconds
     */
    @Override
    public long getDelay() {
        return delay;
    }

    /**
     * The main simulation loop, which runs the simulation, processes events,
     * handles pauses, and triggers the appropriate phases (A, B, C).
     */
    public void run() {
        init(); // create first event
        while (simulate()) {

            // check if thread is interrupted
            if (Thread.currentThread().isInterrupted()) {
                Trace.out(Trace.Level.INFO, "Simulation interrupted. Stopping...");
                return;
            }

            // this is the pause mechanism - monitor, which allows the simulation to be paused
            synchronized (lock) {
                while (pause) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        Trace.out(Trace.Level.INFO, "Thread interrupted while waiting");
                        return;
                    }
                }
            }

            delay();
            controller.updateProgressBar();

            Trace.out(Trace.Level.INFO, "\nPhase A, time: " + currentTime());
            clock.setTime(currentTime());

            Trace.out(Trace.Level.INFO, "\nPhase B:");
            processEventB();

            Trace.out(Trace.Level.INFO, "\nPhase C:");
            tryEventC();

        }

        controller.updateProgressBar();
        controller.onSimulationEnd();
        statistics();

    }

    /**
     * Processes all events that occur at the current clock time (Phase B).
     */
    private void processEventB() {
        while (eventList.getNextTime() == clock.getTime()) {
            processEvent(eventList.remove());
        }
    }

    /**
     * Abstract method to process an event. Must be implemented by subclasses.
     *
     * @param e the event to process
     */
    protected abstract void processEvent(Event e);

    /**
     * Abstract method for trying to start new events (Phase C). Must be implemented by subclasses.
     */
    protected abstract void tryEventC();

    /**
     * Returns the time of the next event.
     *
     * @return the time of the next event
     */
    private double currentTime() {
        return eventList.getNextTime();
    }

    /**
     * Checks if the simulation should continue based on the current time.
     *
     * @return {@code true} if the current time is less than the simulation time, {@code false} otherwise
     */
    private boolean simulate() {
        return clock.getTime() < simulationTime;
    }

    /**
     * Abstract method to initialize the simulation. Must be implemented by subclasses.
     */
    protected abstract void init();

    /**
     * Abstract method to gather and report simulation statistics. Must be implemented by subclasses.
     */
    protected abstract void statistics();

    /**
     * Delays the simulation by the specified delay time.
     */
    private void delay() {
        Trace.out(Trace.Level.INFO, "Delay: " + delay);
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            Trace.out(Trace.Level.INFO, "Thread interrupted while sleeping");
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Pauses the simulation thread. The thread will wait until resumed.
     */
    @Override
    public void pauseThread() {
        // without synchronized block, the pause mechanism would not work, see race condition
        synchronized (lock) {
            pause = true;
        }
    }

    /**
     * Resumes the simulation thread if it is paused.
     */
    @Override
    public void resumeThread() {
        synchronized (lock) {
            pause = false;
            lock.notify(); // notify the waiting thread that it can continue
        }
    }

    /**
     * Checks if the simulation is currently running (i.e., not paused).
     *
     * @return {@code true} if the simulation is running, {@code false} if it is paused
     */
    @Override
    public boolean isRunning() {
        return !pause;
    }

}
