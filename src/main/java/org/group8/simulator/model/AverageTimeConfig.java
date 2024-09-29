package org.group8.simulator.model;

public class AverageTimeConfig {
    // Static fields for average times
    private static double averageCheckInTime = 3;
    private static double averageDoctorTime = 5;
    private static double averageLabTime = 10;
    private static double averageXRayTime = 8;
    private static double averageTreatmentTime = 12;
    private static double averageArrivalTime = 15;

    // Static setter method to set all parameters at once
    public static void setAllParameters(double avgCheckIn, double avgDoctor, double avgLab, double avgXRay, double avgTreatment, double avgArrival) {
        if (avgCheckIn > 0) averageCheckInTime = avgCheckIn;
        if (avgDoctor > 0) averageDoctorTime = avgDoctor;
        if (avgLab > 0) averageLabTime = avgLab;
        if (avgXRay > 0) averageXRayTime = avgXRay;
        if (avgTreatment > 0) averageTreatmentTime = avgTreatment;
        if (avgArrival > 0) averageArrivalTime = avgArrival;
    }

    // Static getter methods
    public static double getAverageCheckInTime() {
        return averageCheckInTime;
    }

    public static double getAverageDoctorTime() {
        return averageDoctorTime;
    }

    public static double getAverageLabTime() {
        return averageLabTime;
    }

    public static double getAverageXRayTime() {
        return averageXRayTime;
    }

    public static double getAverageTreatmentTime() {
        return averageTreatmentTime;
    }

    public static double getAverageArrivalTime() {
        return averageArrivalTime;
    }
}
