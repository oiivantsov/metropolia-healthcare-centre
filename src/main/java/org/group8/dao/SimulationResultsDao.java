package org.group8.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.group8.datasource.MariaDbJpaConnection;
import org.group8.simulator.model.SimulationResults;

import java.util.List;

/**
 * Provides methods to persist, find, update and delete records of Simulation Results in the database.
 */
public class SimulationResultsDao {

    /**
     * Persists a new SimulationResults entity in the database.
     * @param simResults The SimulationResults entity to be persisted
     */
    public void persist(SimulationResults simResults) {
        EntityManager em = MariaDbJpaConnection.getInstance();
        em.getTransaction().begin();
        em.persist(simResults);  // This will create a new record
        em.getTransaction().commit();
    }

    /**
     * Finds a SimulationResults entity by its ID.
     * @param id The ID of the SimulationResults entity to find
     * @return The found SimulationResults entity
     */
    public SimulationResults find(Long id) {
        EntityManager em = MariaDbJpaConnection.getInstance();
        return em.find(SimulationResults.class, id);
    }

    /**
     * Updates an existing SimulationResults entity in the database.
     * @param simulationresults The SimulationResults entity to be updated
     */
    public void update(SimulationResults simulationresults) {
        EntityManager em = MariaDbJpaConnection.getInstance();
        em.getTransaction().begin();
        em.merge(simulationresults);  // This updates the record
        em.getTransaction().commit();
    }

    /**
     * Deletes a SimulationResults entity from the database.
     * @param simulationresults The SimulationResults entity to be deleted
     */
    public void delete(SimulationResults simulationresults) {
        EntityManager em = MariaDbJpaConnection.getInstance();
        em.getTransaction().begin();
        em.remove(simulationresults);  // This removes the record
        em.getTransaction().commit();
    }

    /**
     * Finds all SimulationResults entities in the database.
     * @return A list of all SimulationResults entities
     */
    public List<SimulationResults> findAll() {
        EntityManager em = MariaDbJpaConnection.getInstance();

        // Create a CriteriaBuilder from the EntityManager
        CriteriaBuilder cb = em.getCriteriaBuilder();

        // Create a CriteriaQuery that returns a list of SimulationResults
        CriteriaQuery<SimulationResults> cq = cb.createQuery(SimulationResults.class);

        // Define the root of the query (i.e., the SimulationResults entity)
        Root<SimulationResults> root = cq.from(SimulationResults.class);

        // Select all SimulationResults
        cq.select(root);

        // Execute the query and return the results
        return em.createQuery(cq).getResultList();
    }

}
