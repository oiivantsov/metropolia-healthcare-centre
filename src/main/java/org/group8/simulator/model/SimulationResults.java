package org.group8.simulator.model;

import jakarta.persistence.*;

/**
 * The SimulationResults class represents the results of a simulation run.
 * It stores various metrics such as average time, probabilities, and times spent at different service points.
 * This class is mapped to the "simulation_results" table in the database.
 */
@Entity
@Table(name = "simulation_results")
public class SimulationResults {

    /**
     * The unique identifier for the simulation results.
     * This field serves as the primary key for the "simulation_results" table.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Simulation_id", nullable = false, unique = true)
    private long simulationId;

    /**
     * The average time patients spend in the system.
     */
    @Column(name = "average_time", nullable = false)
    private double averageTime;

    /**
     * The total number of patients that arrived at the simulation.
     */
    @Column(name = "total_patients",  nullable = false)
    private int totalPatients;

    /**
     * The number of patients that completed their visit in the simulation.
     */
    @Column(name = "completed_visits", nullable = false)
    private int completedVisits;

    /**
     * The probability of a patient requiring lab services.
     */
    @Column(name = "lab_probability", nullable = false)
    private double labProbability;

    /**
     * The probability of a patient requiring an X-ray.
     */
    @Column(name = "xray_probability", nullable = false)
    private double xrayProbability;

    /**
     * The probability of a patient requiring treatment.
     */
    @Column(name = "treatment_probability", nullable = false)
    private double treatmentProbability;

    /**
     * The average time between patient arrivals.
     */
    @Column(name = "arrival_time", nullable = false)
    private double arrivalTime;

    /**
     * The average time patients spend at the check-in desk.
     */
    @Column(name = "checkin_time", nullable = false)
    private double checkInTime;

    /**
     * The average time patients spend with the doctor.
     */
    @Column(name = "doctor_time", nullable = false)
    private double doctorTime;

    /**
     * The average time patients spend in the lab.
     */
    @Column(name = "lab_time", nullable = false)
    private double labTime;

    /**
     * The average time patients spend in the X-ray room.
     */
    @Column(name = "xray_time", nullable = false)
    private double xrayTime;

    /**
     * The average time patients spend in treatment.
     */
    @Column(name = "treatment_time", nullable = false)
    private double treatmentTime;

    /**
     * The average time patients spend in the system.
     */
    @Column(name = "end_time", nullable = false)
    private double endTime;

    // Simplified constructor
    /**
     * Constructs a SimulationResults object with all the necessary metrics for a simulation run.
     *
     * @param averageTime          the average time spent by patients
     * @param totalPatients        the total number of patients
     * @param completedVisits      the number of patients who completed their visits
     * @param labProbability       the probability of being referred to the lab
     * @param xrayProbability      the probability of being referred to x-ray
     * @param treatmentProbability the probability of being referred to treatment
     * @param arrivalTime          the average arrival time
     * @param checkInTime          the time spent at check-in
     * @param doctorTime           the time spent with the doctor
     * @param labTime              the time spent in the lab
     * @param xrayTime             the time spent in x-ray
     * @param treatmentTime        the time spent in treatment
     * @param endTime              the total simulation end time
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

    public SimulationResults() {}

    // Getters
    public long getSimulationId() { return simulationId; }

    public double getAverageTime() { return averageTime; }

    public int getTotalPatients() {
        return totalPatients;
    }

    public int getCompletedVisits() {
        return completedVisits;
    }

    public double getLabProbability() {
        return labProbability;
    }

    public double getXrayProbability() {
        return xrayProbability;
    }

    public double getTreatmentProbability() {
        return treatmentProbability;
    }

    public double getArrivalTime() {
        return arrivalTime;
    }

    public double getCheckInTime() {
        return checkInTime;
    }

    public double getDoctorTime() {
        return doctorTime;
    }

    public double getLabTime() {
        return labTime;
    }

    public double getXrayTime() {
        return xrayTime;
    }

    public double getTreatmentTime() {
        return treatmentTime;
    }

    public double getEndTime() {
        return endTime;
    }

}
