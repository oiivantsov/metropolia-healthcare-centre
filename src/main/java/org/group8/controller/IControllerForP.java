package org.group8.controller;

import org.group8.simulator.model.Distribution;
import org.group8.simulator.model.Probability;

public interface IControllerForP {

    // Interface provided to the Healthcenter:

    void addPatientToCheckInCanvas();
    void addPatientToDoctorCanvas();
    void addPatientToXRayCanvas();
    void addPatientToLabCanvas();
    void addPatientToTreatmentCanvas();
    void onSimulationEnd();
    double getProbability(String decisionType);
    double getAverageTime(String event);
    String getDistribution(String event);
    Distribution getDistributionObject(String event);
}
