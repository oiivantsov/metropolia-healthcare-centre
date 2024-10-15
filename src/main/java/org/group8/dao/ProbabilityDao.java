package org.group8.dao;

import jakarta.persistence.EntityManager;
import org.group8.simulator.model.Probability;
import org.group8.datasource.MariaDbJpaConnection;

/**
 * Provides methods to persist, find, update and delete probability records in the database.
 */
public class ProbabilityDao {

    /**
     * Persists a new probability entity in the database.
     * @param probability The probability entity to be persisted
     */
    public void persist(Probability probability) {
        EntityManager em = MariaDbJpaConnection.getInstance();
        em.getTransaction().begin();
        em.persist(probability);
        em.getTransaction().commit();
    }

    /**
     * Finds a probability entity by its decision type.
     * @param decisionType The decision type of the Probability entity to be found
     * @return The found Probability entity
     */
    public Probability find(String decisionType) {
        EntityManager em = MariaDbJpaConnection.getInstance();
        Probability probability = em.find(Probability.class, decisionType);
        return probability;
    }

    /**
     * Updates an existing Probability entity in the database.
     * @param probability The Probability entity to be updated
     */
    public void update(Probability probability) {
        EntityManager em = MariaDbJpaConnection.getInstance();
        em.getTransaction().begin();
        em.merge(probability);
        em.getTransaction().commit();
    }

    /**
     * Deletes a Probability entity from the database.
     * @param probability The Probability entity to be deleted
     */
    public void delete(Probability probability) {
        EntityManager em = MariaDbJpaConnection.getInstance();
        em.getTransaction().begin();
        em.remove(probability);
        em.getTransaction().commit();
    }
}
