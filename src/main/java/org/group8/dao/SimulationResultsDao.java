package org.group8.dao;

import jakarta.persistence.EntityManager;
import org.group8.datasource.MariaDbJpaConnection;
import org.group8.simulator.model.SimulationResults;

public class SimulationResultsDao {

    public void persist(SimulationResults simResults) {
        EntityManager em = MariaDbJpaConnection.getInstance();
        em.getTransaction().begin();
        em.persist(simResults);  // This will create a new record
        em.getTransaction().commit();
    }

    public SimulationResults find(Long id) {
        EntityManager em = MariaDbJpaConnection.getInstance();
        return em.find(SimulationResults.class, id);
    }

    public void update(SimulationResults simulationresults) {
        EntityManager em = MariaDbJpaConnection.getInstance();
        em.getTransaction().begin();
        em.merge(simulationresults);  // This updates the record
        em.getTransaction().commit();
    }

    public void delete(SimulationResults simulationresults) {
        EntityManager em = MariaDbJpaConnection.getInstance();
        em.getTransaction().begin();
        em.remove(simulationresults);  // This removes the record
        em.getTransaction().commit();
    }
}
