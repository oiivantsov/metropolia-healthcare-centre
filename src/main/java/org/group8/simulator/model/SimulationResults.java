package org.group8.simulator.model;

import jakarta.persistence.*;

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

    // Simplified constructor
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
