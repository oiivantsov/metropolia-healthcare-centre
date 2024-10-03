package org.group8.dao;

import jakarta.persistence.EntityManager;
import org.group8.datasource.MariaDbJpaConnection;
import org.group8.simulator.model.Distribution;

public class DistributionDao {
    public void persist(Distribution averageTime) {
        EntityManager em = MariaDbJpaConnection.getInstance();
        em.getTransaction().begin();
        em.persist(averageTime);
        em.getTransaction().commit();
    }

    public Distribution find(String event) {
        EntityManager em = MariaDbJpaConnection.getInstance();
        Distribution distribution = em.find(Distribution.class, event);
        return distribution;
    }

    public void update(Distribution distribution) {
        EntityManager em = MariaDbJpaConnection.getInstance();
        em.getTransaction().begin();
        em.merge(distribution);
        em.getTransaction().commit();
    }

    public void delete(Distribution distribution) {
        EntityManager em = MariaDbJpaConnection.getInstance();
        em.getTransaction().begin();
        em.remove(distribution);
        em.getTransaction().commit();
    }
}
