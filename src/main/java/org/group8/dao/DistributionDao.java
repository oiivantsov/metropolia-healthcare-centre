package org.group8.dao;

import jakarta.persistence.EntityManager;
import org.group8.datasource.MariaDbJpaConnection;
import org.group8.simulator.model.Distribution;

/**
 * Provides methods to persist, find, update and delete Distribution records in the database.
 */
public class DistributionDao {

    /**
     * Persists a new Distribution entity in the database.
     * @param averageTime The Distribution entity to be persisted
     */
    public void persist(Distribution averageTime) {
        EntityManager em = MariaDbJpaConnection.getInstance();
        em.getTransaction().begin();
        em.persist(averageTime);
        em.getTransaction().commit();
    }

    /**
     * Finds a Distribution entity by its event identifier.
     * @param event The event identifier of the Distribution entity to be found
     * @return The found Distribution entity
     */
    public Distribution find(String event) {
        EntityManager em = MariaDbJpaConnection.getInstance();
        Distribution distribution = em.find(Distribution.class, event);
        return distribution;
    }

    /**
     * Updates an existing Distribution entity in the database.
     * @param distribution The Distribution entity to be updated
     */
    public void update(Distribution distribution) {
        EntityManager em = MariaDbJpaConnection.getInstance();
        em.getTransaction().begin();
        em.merge(distribution);
        em.getTransaction().commit();
    }

    /**
     * Deletes a Distribution entity from the database.
     * @param distribution The distribution entity to be deleted
     */
    public void delete(Distribution distribution) {
        EntityManager em = MariaDbJpaConnection.getInstance();
        em.getTransaction().begin();
        em.remove(distribution);
        em.getTransaction().commit();
    }
}
