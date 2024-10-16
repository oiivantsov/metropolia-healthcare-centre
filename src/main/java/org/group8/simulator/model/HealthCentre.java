package org.group8.simulator.model;

import org.group8.controller.DataController;
import org.group8.controller.IControllerForP;
import org.group8.controller.IDataControlller;
import org.group8.distributions.Negexp;
import org.group8.distributions.Poisson;
import org.group8.simulator.framework.AbstractHealthCentre;
import org.group8.simulator.framework.ArrivalProcess;
import org.group8.simulator.framework.Clock;
import org.group8.simulator.framework.Event;

import java.util.Random;

/**
 * The HealthCentre class models a healthcare center simulation, handling patient
 * flow between various service points, such as check-in, doctor consultation,
 * lab, x-ray, and treatment.
 *
 * It extends {@link AbstractHealthCentre} and implements the logic for initializing
 * and processing events in the simulation, as well as gathering and saving statistics.
 */
public class HealthCentre extends AbstractHealthCentre {

    private ArrivalProcess checkInProcess;
    private ServicePoint checkIn, doctor, lab, xRay, treatment;
    private Random decisionMaker = new Random();
    private IDataControlller dataControlller = new DataController();

    // statics variables
    private int visitedLab = 0;
    private int visitedXRay = 0;
    private int visitedTreatment = 0;
    private int sentToSelfCare = 0;

    /**
     * Constructs a new HealthCentre with a controller responsible for managing
     * the graphical interface and simulation events.
     *
     * @param controller the controller responsible for managing simulation and UI
     */
    public HealthCentre(IControllerForP controller) {
        super(controller);

        // Initialize the check-in process and define service points
        checkInProcess = createArrivalProcess("arrival", EventType.ARR_CHECKIN);

        // Define service points
        checkIn = createServicePoint("check-in", EventType.DEP_CHECKIN);
        doctor = createServicePoint("doctor", EventType.DEP_DOCTOR);
        lab = createServicePoint("lab", EventType.DEP_LAB);
        xRay = createServicePoint("xray", EventType.DEP_XRAY);
        treatment = createServicePoint("treatment", EventType.DEP_TREATMENT);
    }

    /**
     * Creates an ArrivalProcess for the specified event and associates it with
     * a distribution.
     *
     * @param name      the name of the process (e.g., arrival)
     * @param eventType the type of event to generate (e.g., ARR_CHECKIN)
     * @return the created ArrivalProcess
     */
    public ArrivalProcess createArrivalProcess(String name, EventType eventType) {
        Distribution distribution = dataControlller.getDistributionObject(name);
        return switch (distribution.getDistribution()) {
            case "negexp" -> new ArrivalProcess(new Negexp(distribution.getAverageTime()), eventList, eventType);
            case "poisson" -> new ArrivalProcess(new Poisson(distribution.getAverageTime()), eventList, eventType);
            default -> null;
        };
    }

    /**
     * Creates a ServicePoint for the specified event and associates it with
     * a distribution.
     *
     * @param name      the name of the service point (e.g., check-in)
     * @param eventType the type of event to generate (e.g., DEP_CHECKIN)
     * @return the created ServicePoint
     */
    public ServicePoint createServicePoint(String name, EventType eventType) {
        Distribution distribution = dataControlller.getDistributionObject(name);
        return switch (distribution.getDistribution()) {
            case "negexp" -> new ServicePoint(new Negexp(distribution.getAverageTime()), eventList, eventType);
            case "poisson" -> new ServicePoint(new Poisson(distribution.getAverageTime()), eventList, eventType);
            default -> null;
        };
    }

    /**
     * Initializes the simulation by generating the first event in the check-in
     * process.
     */
    @Override
    protected void init() {
        // Initialize the first event (arrival at Check-In)
        checkInProcess.generateNext();
    }

    /**
     * Processes simulation events such as arrivals and departures from various
     * service points, making decisions about patient flow after doctor consultations.
     *
     * @param e the event to process
     */
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

                // Check if the patient leaves after the doctor consultation
                if (nextStep < dataControlller.getProbability("NO_TREATMENT")) {
                    // Patient leaves the healthcare centre without further treatment
                    sentToSelfCare++;
                    p.setDepartureTime(Clock.getInstance().getTime());
                    p.report();
                } else if (nextStep < dataControlller.getProbability("NO_TREATMENT") + dataControlller.getProbability("LAB")) {
                    lab.addToQueue(p);  // Lab
                    visitedLab++;
                    controller.addPatientToLabCanvas();
                } else if (nextStep < dataControlller.getProbability("NO_TREATMENT") + dataControlller.getProbability("LAB") + dataControlller.getProbability("XRAY")) {
                    xRay.addToQueue(p);  // X-ray
                    visitedXRay++;
                    controller.addPatientToXRayCanvas();
                } else {
                    treatment.addToQueue(p);  // Treatment
                    visitedTreatment++;
                    controller.addPatientToTreatmentCanvas();
                }
                break;

            case DEP_LAB:
                controller.removePatientFromLabCanvas();
                p = lab.removeFromQueue();
                treatment.addToQueue(p);  // After lab, go to treatment
                visitedTreatment++;
                controller.addPatientToTreatmentCanvas();
                break;

            case DEP_XRAY:
                controller.removePatientFromXRayCanvas();
                p = xRay.removeFromQueue();
                treatment.addToQueue(p);  // After x-ray, go to treatment
                visitedTreatment++;
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

    /**
     * Attempts to start service at any service point that is not currently busy
     * and has a queue.
     */
    @Override
    protected void tryEventC() {
        for (ServicePoint sp : new ServicePoint[]{checkIn, doctor, lab, xRay, treatment}) {
            if (!sp.isBusy() && sp.hasQueue()) {
                sp.startService();
            }
        }
    }

    /**
     * Gathers and prints statistics about the simulation run, including total
     * patients and average time spent in the healthcare center.
     */
    @Override
    protected void statistics() {
        System.out.println();
        System.out.println("--- Simulation statistics ---");
        System.out.println("Simulation ended at time " + Clock.getInstance().getTime());
        System.out.println("Total patients arrived at healthcare centre: " + Patient.getTotalPatients());
        System.out.println("Total patients completed the visit: " + Patient.getCompletedPatients());
        System.out.println("Average time spent by all patients completed the visit: " + Patient.getTotalTime() / Patient.getCompletedPatients());

        gatherAndSaveSimulationData();
    }

    /**
     * Retrieves and returns the statistics of the simulation run as a formatted string.
     *
     * @return a string containing simulation statistics
     */
    public String getStatistics() {
        StringBuilder statisticsBuilder = new StringBuilder();

        statisticsBuilder.append("----- Simulation Statistics -----\n");
        statisticsBuilder.append(String.format("Simulation ended at time: %.2f\n", Clock.getInstance().getTime()));
        statisticsBuilder.append(String.format("Total patients arrived at the healthcare center: %d\n", Patient.getTotalPatients()));
        statisticsBuilder.append(String.format("Total patients completed their visit: %d\n", Patient.getCompletedPatients()));
        statisticsBuilder.append("---------------------------------\n");

        // Visited service points
        statisticsBuilder.append("Service Point Visits:\n");
        statisticsBuilder.append(String.format("  Lab visits: %d\n", visitedLab));
        statisticsBuilder.append(String.format("  X-Ray visits: %d\n", visitedXRay));
        statisticsBuilder.append(String.format("  Treatment visits: %d\n", visitedTreatment));
        statisticsBuilder.append(String.format("  Sent to self-care: %d\n", sentToSelfCare));
        statisticsBuilder.append("---------------------------------\n");

        // Print utilization rates for each service point
        statisticsBuilder.append("Service Point Utilization Rates:\n");
        statisticsBuilder.append(String.format("  Check-In: %.2f%%\n", checkIn.getUtilizationRate() * 100));
        statisticsBuilder.append(String.format("  Doctor: %.2f%%\n", doctor.getUtilizationRate() * 100));
        statisticsBuilder.append(String.format("  Lab: %.2f%%\n", lab.getUtilizationRate() * 100));
        statisticsBuilder.append(String.format("  X-Ray: %.2f%%\n", xRay.getUtilizationRate() * 100));
        statisticsBuilder.append(String.format("  Treatment: %.2f%%\n", treatment.getUtilizationRate() * 100));
        statisticsBuilder.append("---------------------------------\n");

        // Calculating average time
        int completedPatients = Patient.getCompletedPatients();
        double averageTime = (completedPatients > 0) ? Patient.getTotalTime() / (double) completedPatients : 0.0;

        statisticsBuilder.append(String.format("Average time spent per patient (completed visits): %.2f time units\n", averageTime));
        statisticsBuilder.append("---------------------------------\n");

        statisticsBuilder.append("End of report\n");

        return statisticsBuilder.toString();
    }

    /**
     * Gathers simulation data, calculates statistics, and saves them using the
     * data controller, including utilization rates for each service point.
     */
    public void gatherAndSaveSimulationData() {

        // Calculation for patient completion time
        int completedPatients = Patient.getCompletedPatients();
        double averageTime = (completedPatients > 0) ? Patient.getTotalTime() / (double) completedPatients : 0.0;
        double endTime = Clock.getInstance().getTime();

        // Probabilities
        double labProbability = dataControlller.getProbability("LAB");
        double xrayProbability = dataControlller.getProbability("XRAY");
        double treatmentProbability = 1.0 - (labProbability + xrayProbability);

        // Time-related values
        double arrivalTime = dataControlller.getAverageTime("arrival");
        double checkInTime = dataControlller.getAverageTime("check-in");
        double doctorTime = dataControlller.getAverageTime("doctor");
        double labTime = dataControlller.getAverageTime("lab");
        double xrayTime = dataControlller.getAverageTime("xray");
        double treatmentTime = dataControlller.getAverageTime("treatment");

        // Utilization rates for each service point
        double checkInUtilization = checkIn.getUtilizationRate();
        double doctorUtilization = doctor.getUtilizationRate();
        double labUtilization = lab.getUtilizationRate();
        double xRayUtilization = xRay.getUtilizationRate();
        double treatmentUtilization = treatment.getUtilizationRate();

        // Create SimulationResults object using the new constructor including utilization rates
        SimulationResults simulationResults = new SimulationResults(
                averageTime, Patient.getTotalPatients(), completedPatients,
                labProbability, xrayProbability, treatmentProbability,
                arrivalTime, checkInTime, doctorTime, labTime, xrayTime, treatmentTime, endTime,
                checkInUtilization, doctorUtilization, labUtilization, xRayUtilization, treatmentUtilization
        );

        // Persist the simulation results using the data controller
        dataControlller.persistSimulationResults(simulationResults);
    }

}
