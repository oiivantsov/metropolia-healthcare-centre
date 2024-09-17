package org.group8.model;

public enum DecisionProbability {
    LAB(0.3),
    XRAY(0.4),
    TREATMENT(0.3);

    private final double probability;

    DecisionProbability(double probability) {
        this.probability = probability;
    }

    public double getProbability() {
        return probability;
    }
}
