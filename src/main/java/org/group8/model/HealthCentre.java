package org.group8.model;

import org.group8.distributions.Negexp;
import org.group8.framework.*;

import java.util.Random;

public class HealthCentre extends AbstractHealthCentre {

    private ArrivalProcess checkInProcess;
    private ServicePoint checkIn, doctor, lab, xRay, treatment;
    private Random decisionMaker = new Random();

    public HealthCentre() {
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
        double nextStep;
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
                // decision-making process (random based on enum probabilities)
                nextStep = decisionMaker.nextDouble();
                if (nextStep < DecisionProbability.LAB.getProbability()) {
                    lab.addToQueue(p);  // Lab
                } else if (nextStep < DecisionProbability.LAB.getProbability() + DecisionProbability.XRAY.getProbability()) {
                    xRay.addToQueue(p);  // X-ray
                } else {
                    treatment.addToQueue(p);  // Treatment
                }
                break;

            case DEP_LAB:
                // here we can add some logic to decide where to go next
                p = lab.removeFromQueue();
                treatment.addToQueue(p);  // After lab, go to treatment
                break;

            case DEP_XRAY:
                // here we can add some logic to decide where to go next
                p = xRay.removeFromQueue();
                treatment.addToQueue(p);  // After x-ray, go to treatment
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
        System.out.println();
        System.out.println("--- Simulation statistics ---");
        System.out.println("Simulation ended at time " + Clock.getInstance().getTime());
        System.out.println("Total patients arrived at healthcare centre: " + Patient.getTotalPatients());
        System.out.println("Total patients completed the visit: " + Patient.getCompletedPatients());
        System.out.println("Average time spent by all patients completed the visit: " + Patient.getTotalTime() / Patient.getCompletedPatients());
        // Add more detailed statistics ...
    }

    public String getOutputs() {
        return ("--- Simulation statistics ---" + "\r\n" + ("Simulation ended at time " + Clock.getInstance().getTime()) + "\r\n" + ("Total patients arrived at healthcare centre: " + Patient.getTotalPatients()) + "\r\n" + ("Total patients completed the visit: " + Patient.getCompletedPatients()) + "\r\n" + ("Average time spent by all patients completed the visit: " + Patient.getTotalTime() / Patient.getCompletedPatients()));
    }
}
