package org.group8.simulator.framework;

import org.group8.controller.IControllerForP;

/**
 * Abstract class representing the core logic of a healthcare simulation.
 * This class provides the base structure for handling simulation events,
 * controlling the simulation thread, and managing the simulation clock.
 * It extends {@link Thread} to allow the simulation to run in a separate thread.
 */
public abstract class AbstractHealthCentre extends Thread implements IHealthCentre {

    private double simulationTime = 0;
    private long delay = 0;

    // Object and boolean to control the simulation's thread
    private final Object lock = new Object();
    private boolean pause = false;

    private Clock clock;

    protected EventList eventList;

    protected IControllerForP controller;

    /**
     * Constructs an AbstractHealthCentre with a controller responsible
     * for managing simulation events and the graphical interface.
     *
     * @param controller the controller for managing simulation events and UI
     */
    public AbstractHealthCentre(IControllerForP controller) {
        this.controller = controller;
        clock = Clock.getInstance();
        eventList = new EventList();
    }

    @Override
    public void setSimulationTime(double time) {
        simulationTime = time;
    }

    @Override
    public double getSimulationTime() {
        return simulationTime;
    }

    @Override
    public void setDelay(long time) {
        delay = time;
    }

    @Override
    public long getDelay() {
        return delay;
    }

    /**
     * Main loop for running the simulation. This method initializes the simulation,
     * processes events, and manages pauses and delays.
     */
    public void run() {
        init(); // Initialize and create the first event
        while (simulate()) {

            // Check if thread is interrupted
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
     * Processes events scheduled for the current simulation time.
     */
    private void processEventB() {
        while (eventList.getNextTime() == clock.getTime()) {
            processEvent(eventList.remove());
        }
    }

    /**
     * Abstract method to be implemented by subclasses to define
     * how an event should be processed.
     *
     * @param e the event to process
     */
    protected abstract void processEvent(Event e);

    /**
     * Abstract method to be implemented by subclasses to define
     * the logic for attempting to process events in phase C of the simulation.
     */
    protected abstract void tryEventC();

    /**
     * Retrieves the current time from the event list.
     *
     * @return the current simulation time
     */
    private double currentTime() {
        return eventList.getNextTime();
    }

    /**
     * Determines if the simulation should continue based on the current
     * simulation time and the total simulation duration.
     *
     * @return {@code true} if the simulation should continue, {@code false} otherwise
     */
    private boolean simulate() {
        return clock.getTime() < simulationTime;
    }

    /**
     * Abstract method to be implemented by subclasses to initialize the simulation.
     */
    protected abstract void init();

    /**
     * Abstract method to be implemented by subclasses to gather and print
     * simulation statistics at the end of the simulation.
     */
    protected abstract void statistics();

    /**
     * Introduces a delay during the simulation to simulate real-time progress.
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
     * Pauses the simulation thread.
     * <p>
     * Sets the {@code pause} flag to {@code true}, halting the simulation.
     * Synchronization ensures thread safety.
     */
    @Override
    public void pauseThread() {
        // without synchronized block, the pause mechanism would not work, see race condition
        synchronized (lock) {
            pause = true;
        }
    }

    /**
     * Resumes the simulation thread.
     * <p>
     * Sets the {@code pause} flag to {@code false} and notifies the waiting thread to continue.
     */
    @Override
    public void resumeThread() {
        // Resumes the paused thread
        synchronized (lock) {
            pause = false;
            lock.notify(); // Notify the waiting thread to continue
        }
    }

    /**
     * Checks if the simulation is running.
     *
     * @return {@code true} if the simulation is active, {@code false} if paused.
     */
    @Override
    public boolean isRunning() {
        return !pause;
    }

}
