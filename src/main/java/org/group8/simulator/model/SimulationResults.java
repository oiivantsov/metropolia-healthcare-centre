package org.group8.simulator.model;

import jakarta.persistence.*;


/**
 * The {@code SimulationResults} class represents the results of a simulation in a medical setting.
 * It stores various statistics such as average times, total patients, completed visits,
 * and probabilities related to lab, x-ray, and treatment processes.
 */
@Entity
@Table(name = "simulation_results")
public class SimulationResults {

    /**
     * The unique identifier for a simulation result.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Simulation_id", nullable = false, unique = true)
    private long simulationId;

    /**
     * The average time taken per simulation (in minutes).
     */
    @Column(name = "average_time", nullable = false)
    private double averageTime;

    /**
     * The total number of patients involved in the simulation.
     */
    @Column(name = "total_patients",  nullable = false)
    private int totalPatients;

    /**
     * The total number of completed patient visits during the simulation.
     */
    @Column(name = "completed_visits", nullable = false)
    private int completedVisits;

    /**
     * The probability that a patient requires lab tests.
     */
    @Column(name = "lab_probability", nullable = false)
    private double labProbability;

    /**
     * The probability that a patient requires x-ray tests.
     */
    @Column(name = "xray_probability", nullable = false)
    private double xrayProbability;

    /**
     * The probability that a patient requires treatment.
     */
    @Column(name = "treatment_probability", nullable = false)
    private double treatmentProbability;

    /**
     * The time taken for a patient to arrive.
     */
    @Column(name = "arrival_time", nullable = false)
    private double arrivalTime;

    /**
     * The time taken for patient check-in.
     */
    @Column(name = "checkin_time", nullable = false)
    private double checkInTime;

    /**
     * The time spent with the doctor.
     */
    @Column(name = "doctor_time", nullable = false)
    private double doctorTime;

    /**
     * The time taken for lab tests.
     */
    @Column(name = "lab_time", nullable = false)
    private double labTime;

    /**
     * The time taken for x-ray tests.
     */
    @Column(name = "xray_time", nullable = false)
    private double xrayTime;

    /**
     * The time taken for treatment.
     */
    @Column(name = "treatment_time", nullable = false)
    private double treatmentTime;

    /**
     * The total end time of the simulation.
     */
    @Column(name = "end_time", nullable = false)
    private double endTime;

    /**
     * Simplified constructor for {@code SimulationResults} class.
     *
     * @param averageTime          the average time per simulation
     * @param totalPatients        the total number of patients
     * @param completedVisits      the total number of completed visits
     * @param labProbability       the probability of requiring lab tests
     * @param xrayProbability      the probability of requiring x-ray tests
     * @param treatmentProbability the probability of requiring treatment
     * @param arrivalTime          the patient arrival time
     * @param checkInTime          the patient check-in time
     * @param doctorTime           the time spent with the doctor
     * @param labTime              the time taken for lab tests
     * @param xrayTime             the time taken for x-ray tests
     * @param treatmentTime        the time taken for treatment
     * @param endTime              the total end time of the simulation
     */
    public SimulationResults (double averageTime, int totalPatients, int completedVisits,
                             double labProbability, double xrayProbability, double treatmentProbability,
                             double arrivalTime, double checkInTime, double doctorTime, double labTime,
                             double xrayTime, double treatmentTime, double endTime) {
        this.averageTime = averageTime;
        this.totalPatients = totalPatients;
        this.completedVisits = completedVisits;
        this.labProbability = labProbability;
        this.xrayProbability = xrayProbability;
        this.treatmentProbability = treatmentProbability;
        this.arrivalTime = arrivalTime;
        this.checkInTime = checkInTime;
        this.doctorTime = doctorTime;
        this.labTime = labTime;
        this.xrayTime = xrayTime;
        this.treatmentTime = treatmentTime;
        this.endTime = endTime;
    }

    /**
     * Default constructor for {@code SimulationResults}.
     */
    public SimulationResults() {}

    /**
     * Returns the unique simulation ID.
     *
     * @return the simulation ID
     */
    public long getSimulationId() { return simulationId; }

    /**
     * Returns the average time per simulation.
     *
     * @return the average time
     */
    public double getAverageTime() { return averageTime; }

    /**
     * Returns the total number of patients.
     *
     * @return the total patients
     */
    public int getTotalPatients() {
        return totalPatients;
    }

    /**
     * Returns the number of completed patient visits.
     *
     * @return the number of completed visits
     */
    public int getCompletedVisits() {
        return completedVisits;
    }

    /**
     * Returns the probability that a patient requires lab tests.
     *
     * @return the lab probability
     */
    public double getLabProbability() {
        return labProbability;
    }

    /**
     * Returns the probability that a patient requires x-ray tests.
     *
     * @return the x-ray probability
     */
    public double getXrayProbability() {
        return xrayProbability;
    }

    /**
     * Returns the probability that a patient requires treatment.
     *
     * @return the treatment probability
     */
    public double getTreatmentProbability() {
        return treatmentProbability;
    }

    /**
     * Returns the time taken for a patient to arrive.
     *
     * @return the arrival time
     */
    public double getArrivalTime() {
        return arrivalTime;
    }

    /**
     * Returns the patient check-in time.
     *
     * @return the check-in time
     */
    public double getCheckInTime() {
        return checkInTime;
    }

    /**
     * Returns the time spent with the doctor.
     *
     * @return the doctor time
     */
    public double getDoctorTime() {
        return doctorTime;
    }

    /**
     * Returns the time taken for lab tests.
     *
     * @return the lab time
     */
    public double getLabTime() {
        return labTime;
    }

    /**
     * Returns the time taken for x-ray tests.
     *
     * @return the x-ray time
     */
    public double getXrayTime() {
        return xrayTime;
    }

    /**
     * Returns the time taken for treatment.
     *
     * @return the treatment time
     */
    public double getTreatmentTime() {
        return treatmentTime;
    }

    /**
     * Returns the total end time of the simulation.
     *
     * @return the end time
     */
    public double getEndTime() {
        return endTime;
    }

}
