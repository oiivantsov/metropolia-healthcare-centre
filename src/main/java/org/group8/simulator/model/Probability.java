package org.group8.simulator.model;

import jakarta.persistence.*;

@Entity
@Table(name = "decision_probability")
public class Probability {

    @Id
    @Column(name = "decision_type")
    private String decisionType;

    @Column
    private double probability; // Default value

    public Probability() {
    }

    public Probability(String decisionType, double probability) {
        this.decisionType = decisionType;
        this.probability = probability;
    }

    public String getDecisionType() {
        return decisionType;
    }

    public void setDecisionType(String decisionType) {
        this.decisionType = decisionType;
    }

    public double getProbability() {
        return probability;
    }

    public void setProbability(double probability) {
        this.probability = probability;
    }

}
