package org.group8.datasource;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

/**
 * Singleton class for managing the EntityManager instance for MariaDB.
 */
public class MariaDbJpaConnection {

    private static EntityManagerFactory emf = null;
    private static EntityManager em = null;

    /**
     * Returns the singleton instance of EntityManager.
     * If the EntityManagerFactory or EntityManager is not initialized, it initializes them.
     * @return The singleton instance of EntityManager
     */
    public static EntityManager getInstance() {
        if (em == null) {
            if (emf == null) {
                emf = Persistence.createEntityManagerFactory("CompanyMariaDbUnit");
            }
            em = emf.createEntityManager();
        }
        return em;
    }
}
