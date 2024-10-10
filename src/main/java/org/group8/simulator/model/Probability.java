package org.group8.simulator.model;

import jakarta.persistence.*;

/**
 * The Probability class represents a decision probability in the simulation.
 * Each probability corresponds to a specific decision type, which is stored in the database.
 */
@Entity
@Table(name = "decision_probability")
public class Probability {

    /**
     * The type of decision associated with this probability (e.g., lab, x-ray, treatment).
     * This field serves as the primary key for the "decision_probability" table.
     */
    @Id
    @Column(name = "decision_type")
    private String decisionType;

    /**
     * The probability value associated with the decision type.
     */
    @Column
    private double probability;

    /**
     * Default constructor for the Probability class.
     * Required by JPA for entity instantiation.
     */
    public Probability() {
    }

    /**
     * Constructs a Probability object with a specified decision type and probability value.
     *
     * @param decisionType the type of decision (e.g., lab, x-ray, treatment)
     * @param probability  the probability associated with the decision type
     */
    public Probability(String decisionType, double probability) {
        this.decisionType = decisionType;
        this.probability = probability;
    }

    /**
     * Returns the decision type associated with this probability.
     *
     * @return the decision type
     */
    public String getDecisionType() {
        return decisionType;
    }

    /**
     * Sets the decision type for this probability.
     *
     * @param decisionType the decision type to set
     */
    public void setDecisionType(String decisionType) {
        this.decisionType = decisionType;
    }

    /**
     * Returns the probability value associated with this decision.
     *
     * @return the probability value
     */
    public double getProbability() {
        return probability;
    }

    /**
     * Sets the probability value for this decision.
     *
     * @param probability the probability value to set
     */
    public void setProbability(double probability) {
        this.probability = probability;
    }
}
