package org.group8.dao;

import jakarta.persistence.EntityManager;
import org.group8.simulator.model.Probability;
import org.group8.datasource.MariaDbJpaConnection;

public class ProbabilityDao {
    public void persist(Probability probability) {
        EntityManager em = MariaDbJpaConnection.getInstance();
        em.getTransaction().begin();
        em.persist(probability);
        em.getTransaction().commit();
    }

    public Probability find(String decisionType) {
        EntityManager em = MariaDbJpaConnection.getInstance();
        Probability probability = em.find(Probability.class, decisionType);
        return probability;
    }

    public void update(Probability probability) {
        EntityManager em = MariaDbJpaConnection.getInstance();
        em.getTransaction().begin();
        em.merge(probability);
        em.getTransaction().commit();
    }

    public void delete(Probability probability) {
        EntityManager em = MariaDbJpaConnection.getInstance();
        em.getTransaction().begin();
        em.remove(probability);
        em.getTransaction().commit();
    }
}
