package org.group8.dao;

import jakarta.persistence.EntityManager;
import org.group8.datasource.MariaDbJpaConnection;
import org.group8.simulator.model.AverageTime;

public class AverageTimeDao {
    public void persist(AverageTime averageTime) {
        EntityManager em = MariaDbJpaConnection.getInstance();
        em.getTransaction().begin();
        em.persist(averageTime);
        em.getTransaction().commit();
    }

    public AverageTime find(String event) {
        EntityManager em = MariaDbJpaConnection.getInstance();
        AverageTime averageTime = em.find(AverageTime.class, event);
        return averageTime;
    }

    public void update(AverageTime averageTime) {
        EntityManager em = MariaDbJpaConnection.getInstance();
        em.getTransaction().begin();
        em.merge(averageTime);
        em.getTransaction().commit();
    }

    public void delete(AverageTime averageTime) {
        EntityManager em = MariaDbJpaConnection.getInstance();
        em.getTransaction().begin();
        em.remove(averageTime);
        em.getTransaction().commit();
    }
}
