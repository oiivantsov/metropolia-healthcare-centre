package org.group8.framework;

public abstract class AbstractHealthCentre {

    private double simulationTime = 0;
    private Clock clock;

    protected EventList eventList;

    public AbstractHealthCentre() {
        clock = Clock.getInstance();
        eventList = new EventList();
    }

    public void setSimulationTime(double time) {
        simulationTime = time;
    }

    public void run() {
        init(); // create first event
        while (simulate()) {

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


}
