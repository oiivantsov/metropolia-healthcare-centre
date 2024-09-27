package org.group8.simulator.framework;

import org.group8.controller.IControllerForP;

public abstract class AbstractHealthCentre extends Thread implements IHealthCentre {

    private double simulationTime = 0;
    private long delay = 0;

    // Object and boolean to control the simulation
    private final Object lock = new Object();
    private boolean pause = false;

    private Clock clock;

    protected EventList eventList;

    protected IControllerForP controller;

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
    public void setDelay(long time) {
        delay = time;
    }

    @Override
    public long getDelay() {
        return delay;
    }

    public void run() {
        init(); // create first event
        while (simulate()) {

            // this is the pause mechanism - monitor, which allows the simulation to be paused
            synchronized (lock) {
                while (pause) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            delay();

            Trace.out(Trace.Level.INFO, "\nPhase A, time: " + currentTime());
            clock.setTime(currentTime());

            Trace.out(Trace.Level.INFO, "\nPhase B:");
            processEventB();

            Trace.out(Trace.Level.INFO, "\nPhase C:");
            tryEventC();

        }

        statistics();

    }

    private void processEventB() {
        while (eventList.getNextTime() == clock.getTime()) {
            processEvent(eventList.remove());
        }
    }

    protected abstract void processEvent(Event e);

    protected abstract void tryEventC();

    private double currentTime() {
        return eventList.getNextTime();
    }

    private boolean simulate() {
        return clock.getTime() < simulationTime;
    }

    protected abstract void init();

    protected abstract void statistics();

    private void delay() {
        Trace.out(Trace.Level.INFO, "Delay: " + delay);
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void pauseThread() {
        // without synchronized block, the pause mechanism would not work, see race condition
        synchronized (lock) {
            pause = true;
        }
    }

    @Override
    public void resumeThread() {
        synchronized (lock) {
            pause = false;
            lock.notify(); // notify the waiting thread that it can continue
        }
    }

    @Override
    public boolean isRunning() {
        return !pause;
    }

}
