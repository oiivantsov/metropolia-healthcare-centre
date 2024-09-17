package org.group8.model;

import org.group8.distributions.Negexp;
import org.group8.framework.*;

import java.util.Random;

public class MyHealthCentre extends HealthCentre {

    private ArrivalProcess checkInProcess;
    private ServicePoint checkIn, doctor, lab, xRay, treatment;
    private Random decisionMaker = new Random();

    public MyHealthCentre() {
        // Check-In process
        checkInProcess = new ArrivalProcess(new Negexp(15), eventList, EventType.ARR_CHECKIN);

        // Define service points
        checkIn = new ServicePoint(new Negexp(3), eventList, EventType.DEP_CHECKIN);
        doctor = new ServicePoint(new Negexp(5), eventList, EventType.DEP_DOCTOR);
        lab = new ServicePoint(new Negexp(10), eventList, EventType.DEP_LAB);
        xRay = new ServicePoint(new Negexp(8), eventList, EventType.DEP_XRAY);
        treatment = new ServicePoint(new Negexp(12), eventList, EventType.DEP_TREATMENT);
    }

    @Override
    protected void init() {
        // Initialize the first event (arrival at Check-In)
        checkInProcess.generateNext();
    }

    @Override
    protected void processEvent(Event e) {
        Patient p;
        switch ((EventType) e.getType()) {
            case ARR_CHECKIN:
                checkIn.addToQueue(new Patient());
                checkInProcess.generateNext();
                break;

            case DEP_CHECKIN:
                p = checkIn.removeFromQueue();
                doctor.addToQueue(p);  // Move to doctor
                break;

            case DEP_DOCTOR:
                p = doctor.removeFromQueue();
                // Decision-making process (random)
                int nextStep = decisionMaker.nextInt(100);
                if (nextStep < 30) {
                    lab.addToQueue(p);  // 30% chance of lab
                } else if (nextStep < 70) {
                    xRay.addToQueue(p);  // 40% chance of x-ray
                } else {
                    treatment.addToQueue(p);  // 30% chance of direct treatment
                }
                break;

            case DEP_LAB:
            case DEP_XRAY:
                p = e.getType() == EventType.DEP_LAB ? lab.removeFromQueue() : xRay.removeFromQueue();
                treatment.addToQueue(p);  // After diagnostics, go to treatment
                break;

            case DEP_TREATMENT:
                p = treatment.removeFromQueue();
                p.setDepartureTime(Clock.getInstance().getTime());
                p.report();
                break;
        }
    }

    @Override
    protected void tryEventC() {
        for (ServicePoint sp : new ServicePoint[]{checkIn, doctor, lab, xRay, treatment}) {
            if (!sp.isBusy() && sp.hasQueue()) {
                sp.startService();
            }
        }
    }

    @Override
    protected void statistics() {
        System.out.println("Simulation ended at time " + Clock.getInstance().getTime());
        System.out.println("Results will be detailed here.");
    }
}
