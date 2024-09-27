package org.group8.simulator.model;

public enum DecisionProbability {
    LAB(0.45),
    XRAY(0.45),
    TREATMENT(0.1);

    private double probability;

    DecisionProbability(double probability) {
        this.probability = probability;
    }

    public double getProbability() {
        return probability;
    }

    public static void setProbabilities(double lab, double xray, double treatment) {
        LAB.probability = lab;
        XRAY.probability = xray;
        TREATMENT.probability = treatment;
    }
}
