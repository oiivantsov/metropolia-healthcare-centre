package org.group8.simulator.model;

import jakarta.persistence.*;

/**
 * The SimulationResults class represents the results of a simulation run.
 * It stores various metrics such as average time, probabilities, times spent at different service points,
 * and utilization rates for each service point. This class is mapped to the "simulation_results" table in the database.
 */
@Entity
@Table(name = "simulation_results")
public class SimulationResults {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Simulation_id", nullable = false, unique = true)
    private long simulationId;

    @Column(name = "average_time", nullable = false)
    private double averageTime;

    @Column(name = "total_patients",  nullable = false)
    private int totalPatients;

    @Column(name = "completed_visits", nullable = false)
    private int completedVisits;

    @Column(name = "lab_probability", nullable = false)
    private double labProbability;

    @Column(name = "xray_probability", nullable = false)
    private double xrayProbability;

    @Column(name = "treatment_probability", nullable = false)
    private double treatmentProbability;

    @Column(name = "no_treatment_probability", nullable = false)
    private double noTreatmentProbability;

    @Column(name = "arrival_time", nullable = false)
    private double arrivalTime;

    @Column(name = "checkin_time", nullable = false)
    private double checkInTime;

    @Column(name = "doctor_time", nullable = false)
    private double doctorTime;

    @Column(name = "lab_time", nullable = false)
    private double labTime;

    @Column(name = "xray_time", nullable = false)
    private double xrayTime;

    @Column(name = "treatment_time", nullable = false)
    private double treatmentTime;

    @Column(name = "end_time", nullable = false)
    private double endTime;

    // fields for utilization rates

    @Column(name = "checkin_utilization", nullable = false)
    private double checkInUtilization;

    @Column(name = "doctor_utilization", nullable = false)
    private double doctorUtilization;

    @Column(name = "lab_utilization", nullable = false)
    private double labUtilization;

    @Column(name = "xray_utilization", nullable = false)
    private double xrayUtilization;

    @Column(name = "treatment_utilization", nullable = false)
    private double treatmentUtilization;

    /**
     * Constructs a SimulationResults object with all the necessary metrics for a simulation run, including utilization rates.
     *
     * @param averageTime          the average time spent by patients
     * @param totalPatients        the total number of patients
     * @param completedVisits      the number of patients who completed their visits
     * @param labProbability       the probability of being referred to the lab
     * @param xrayProbability      the probability of being referred to x-ray
     * @param treatmentProbability the probability of being referred to treatment
     * @param noTreatmentProbability the probability of not being referred to treatment
     * @param arrivalTime          the average arrival time
     * @param checkInTime          the time spent at check-in
     * @param doctorTime           the time spent with the doctor
     * @param labTime              the time spent in the lab
     * @param xrayTime             the time spent in x-ray
     * @param treatmentTime        the time spent in treatment
     * @param endTime              the total simulation end time
     * @param checkInUtilization   the utilization rate of the check-in service point
     * @param doctorUtilization    the utilization rate of the doctor service point
     * @param labUtilization       the utilization rate of the lab service point
     * @param xrayUtilization      the utilization rate of the x-ray service point
     * @param treatmentUtilization the utilization rate of the treatment service point
     */
    public SimulationResults(
            double averageTime, int totalPatients, int completedVisits,
            double labProbability, double xrayProbability, double treatmentProbability, double noTreatmentProbability,
            double arrivalTime, double checkInTime, double doctorTime, double labTime,
            double xrayTime, double treatmentTime, double endTime,
            double checkInUtilization, double doctorUtilization, double labUtilization,
            double xrayUtilization, double treatmentUtilization) {

        this.averageTime = averageTime;
        this.totalPatients = totalPatients;
        this.completedVisits = completedVisits;
        this.labProbability = labProbability;
        this.xrayProbability = xrayProbability;
        this.treatmentProbability = treatmentProbability;
        this.noTreatmentProbability = noTreatmentProbability;
        this.arrivalTime = arrivalTime;
        this.checkInTime = checkInTime;
        this.doctorTime = doctorTime;
        this.labTime = labTime;
        this.xrayTime = xrayTime;
        this.treatmentTime = treatmentTime;
        this.endTime = endTime;
        this.checkInUtilization = checkInUtilization;
        this.doctorUtilization = doctorUtilization;
        this.labUtilization = labUtilization;
        this.xrayUtilization = xrayUtilization;
        this.treatmentUtilization = treatmentUtilization;
    }

    public SimulationResults() {}

    // Getters for utilization fields

    public double getCheckInUtilization() {
        return checkInUtilization;
    }

    public double getDoctorUtilization() {
        return doctorUtilization;
    }

    public double getLabUtilization() {
        return labUtilization;
    }

    public double getXrayUtilization() {
        return xrayUtilization;
    }

    public double getTreatmentUtilization() {
        return treatmentUtilization;
    }

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

    public double getNoTreatmentProbability() {
        return noTreatmentProbability;
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
