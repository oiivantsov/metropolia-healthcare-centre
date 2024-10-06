package org.group8.simulator.model;

import org.group8.controller.IControllerForP;
import org.group8.dao.ProbabilityDao;
import org.group8.dao.SimulationResultsDao;
import org.group8.distributions.Negexp;
import org.group8.distributions.Poisson;
import org.group8.simulator.framework.AbstractHealthCentre;
import org.group8.simulator.framework.ArrivalProcess;
import org.group8.simulator.framework.Clock;
import org.group8.simulator.framework.Event;

import java.util.List;
import java.util.Random;

public class HealthCentre extends AbstractHealthCentre {

    private ArrivalProcess checkInProcess;
    private ServicePoint checkIn, doctor, lab, xRay, treatment;
    private Random decisionMaker = new Random();

    public HealthCentre(IControllerForP controller) {
        super(controller);

        // Check-In process
        checkInProcess = createArrivalProcess("arrival", EventType.ARR_CHECKIN);

        // Define service points
        checkIn = createServicePoint("check-in", EventType.DEP_CHECKIN);
        doctor = createServicePoint("doctor", EventType.DEP_DOCTOR);
        lab = createServicePoint("lab", EventType.DEP_LAB);
        xRay = createServicePoint("xray", EventType.DEP_XRAY);
        treatment = createServicePoint("treatment", EventType.DEP_TREATMENT);
    }

    public ArrivalProcess createArrivalProcess(String name, EventType eventType) {
        Distribution distribution = controller.getDistributionObject(name);
        return switch (distribution.getDistribution()) {
            case "negexp" -> new ArrivalProcess(new Negexp(distribution.getAverageTime()), eventList, eventType);
            case "poisson" -> new ArrivalProcess(new Poisson(distribution.getAverageTime()), eventList, eventType);
            default -> null;
        };
    }

    public ServicePoint createServicePoint(String name, EventType eventType) {
        Distribution distribution = controller.getDistributionObject(name);
        return switch (distribution.getDistribution()) {
            case "negexp" -> new ServicePoint(new Negexp(distribution.getAverageTime()), eventList, eventType);
            case "poisson" -> new ServicePoint(new Poisson(distribution.getAverageTime()), eventList, eventType);
            default -> null;
        };
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
                controller.addPatientToCheckInCanvas();
                checkInProcess.generateNext();
                break;

            case DEP_CHECKIN:
                controller.removePatientFromCheckInCanvas();
                p = checkIn.removeFromQueue();
                doctor.addToQueue(p);  // Move to doctor
                controller.addPatientToDoctorCanvas();
                break;

            case DEP_DOCTOR:
                controller.removePatientFromDoctorCanvas();
                p = doctor.removeFromQueue();
                // decision-making process (random based on enum probabilities)
                nextStep = decisionMaker.nextDouble();
                if (nextStep < controller.getProbability("LAB")) {
                    lab.addToQueue(p);  // Lab
                    controller.addPatientToLabCanvas();
                } else if (nextStep < controller.getProbability("LAB") + controller.getProbability("XRAY")) {
                    xRay.addToQueue(p);  // X-ray
                    controller.addPatientToXRayCanvas();
                } else {
                    treatment.addToQueue(p);  // Treatment
                    controller.addPatientToTreatmentCanvas();
                }
                break;

            case DEP_LAB:
                controller.removePatientFromLabCanvas();
                // here we can add some logic to decide where to go next
                p = lab.removeFromQueue();
                treatment.addToQueue(p);  // After lab, go to treatment
                controller.addPatientToTreatmentCanvas();
                break;

            case DEP_XRAY:
                controller.removePatientFromXRayCanvas();
                // here we can add some logic to decide where to go next
                p = xRay.removeFromQueue();
                treatment.addToQueue(p);  // After x-ray, go to treatment
                controller.addPatientToTreatmentCanvas();
                break;

            case DEP_TREATMENT:
                controller.removePatientFromTreatmentCanvas();
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

        // Create the DAO instance
        SimulationResultsDao simulationResultsDao = new SimulationResultsDao();

        // Create the manager, passing the DAO
        SimulationResultsManager simulationResultsManager = new SimulationResultsManager(simulationResultsDao);

        // Assuming `controller` is your IControllerForP instance
        simulationResultsManager.gatherAndSaveSimulationData(controller);
    }

    public String getStatistics() {
        StringBuilder statisticsBuilder = new StringBuilder();

        statisticsBuilder.append(String.format("Simulation ended at time: %.2f\n", Clock.getInstance().getTime()));
        statisticsBuilder.append(String.format("Total patients arrived at healthcare center: %d\n", Patient.getTotalPatients()));
        statisticsBuilder.append(String.format("Total patients completed the visit: %d\n", Patient.getCompletedPatients()));

        // Calculating average time
        int completedPatients = Patient.getCompletedPatients();
        double averageTime = (completedPatients > 0) ? Patient.getTotalTime() / (double) completedPatients : 0.0;

        statisticsBuilder.append(String.format("Average time spent by all patients who completed the visit: %.2f\n", averageTime));

        return statisticsBuilder.toString();
    }

    public class SimulationResultsManager {

        private SimulationResultsDao simulationResultsDao;

        public SimulationResultsManager(SimulationResultsDao simulationResultsDao) {
            this.simulationResultsDao = simulationResultsDao;
        }

        public void gatherAndSaveSimulationData(IControllerForP controller) {

            // Calculation for patient completion time
            int completedPatients = Patient.getCompletedPatients();
            double averageTime = (completedPatients > 0) ? Patient.getTotalTime() / (double) completedPatients : 0.0;
            double endTime = Clock.getInstance().getTime();

            // Probabilities
            double labProbability = controller.getProbability("LAB");
            double xrayProbability = controller.getProbability("XRAY");
            double treatmentProbability = 1.0 - (labProbability + xrayProbability);

            // Time-related values
            double arrivalTime = controller.getAverageTime("arrival");
            double checkInTime = controller.getAverageTime("check-in");
            double doctorTime = controller.getAverageTime("doctor");
            double labTime = controller.getAverageTime("lab");
            double xrayTime = controller.getAverageTime("xray");
            double treatmentTime = controller.getAverageTime("treatment");

            // Create SimulationResults object using the simplified constructor
            SimulationResults simulationResults = new SimulationResults(
                    averageTime, Patient.getTotalPatients(), completedPatients,
                    labProbability, xrayProbability, treatmentProbability,
                    arrivalTime, checkInTime, doctorTime, labTime, xrayTime, treatmentTime, endTime
            );

            // Persist the new results
            simulationResultsDao.persist(simulationResults);
        }
    }



}
