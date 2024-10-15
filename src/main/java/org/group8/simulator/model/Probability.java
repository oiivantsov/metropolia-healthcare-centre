package org.group8.simulator.model;

import jakarta.persistence.*;

/**
 * The {@code Probability} class represents the probability associated with a particular decision type.
 * It is mapped to a database table {@code decision_probability} using JPA annotations.
 */
@Entity
@Table(name = "decision_probability")
public class Probability {

    /**
     * The type of decision, which serves as the unique identifier for this probability entry.
     */
    @Id
    @Column(name = "decision_type")
    private String decisionType;

    /**
     * The probability value associated with the decision type.
     */
    @Column
    private double probability; // Default value

    /**
     * Default constructor for {@code Probability}.
     * Required by JPA.
     */
    public Probability() {
    }

    /**
     * Constructs a new {@code Probability} with a specified decision type and probability.
     *
     * @param decisionType the type of decision
     * @param probability  the probability value associated with the decision
     */
    public Probability(String decisionType, double probability) {
        this.decisionType = decisionType;
        this.probability = probability;
    }

    /**
     * Returns the type of decision.
     *
     * @return the decision type
     */
    public String getDecisionType() {
        return decisionType;
    }

    /**
     * Sets the type of decision.
     *
     * @param decisionType the decision type to set
     */
    public void setDecisionType(String decisionType) {
        this.decisionType = decisionType;
    }

    /**
     * Returns the probability value associated with the decision type.
     *
     * @return the probability value
     */
    public double getProbability() {
        return probability;
    }

    /**
     * Sets the probability value for the decision type.
     *
     * @param probability the probability value to set
     */
    public void setProbability(double probability) {
        this.probability = probability;
    }

}
