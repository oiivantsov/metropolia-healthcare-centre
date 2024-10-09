package org.group8.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import org.group8.datasource.MariaDbJpaConnection;
import org.group8.simulator.model.Distribution;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.*;

class DistributionDaoTest {

    private DistributionDao distributionDao;
    private EntityManager mockEm;
    private EntityTransaction mockTransaction;
    private Distribution mockDistribution;

    @BeforeEach
    void setUp() {
        // clears previous mocks if any
        Mockito.clearAllCaches();

        // mock entitymanager and entitytransaction
        mockEm = mock(EntityManager.class);
        mockTransaction = mock(EntityTransaction.class);
        mockDistribution = mock(Distribution.class);

        // mock mariadbjpaconnection to return the mocked entitymanager
        Mockito.mockStatic(MariaDbJpaConnection.class);
        when(MariaDbJpaConnection.getInstance()).thenReturn(mockEm);

        // set up mocked behavior for transaction
        when(mockEm.getTransaction()).thenReturn(mockTransaction);

        // initialize distributiondao
        distributionDao = new DistributionDao();
    }

    @Test
    void testPersist() {
        distributionDao.persist(mockDistribution);

        // verify transaction and persist methods are called
        verify(mockTransaction).begin();
        verify(mockEm).persist(mockDistribution);
        verify(mockTransaction).commit();
    }

    @Test
    void testFind() {
        String event = "testEvent";
        when(mockEm.find(Distribution.class, event)).thenReturn(mockDistribution);

        Distribution result = distributionDao.find(event);

        // verify find is called with correct parameters
        verify(mockEm).find(Distribution.class, event);
        assertSame(mockDistribution, result);
    }

    @Test
    void testUpdate() {
        distributionDao.update(mockDistribution);

        // verify transaction and merge methods are called
        verify(mockTransaction).begin();
        verify(mockEm).merge(mockDistribution);
        verify(mockTransaction).commit();
    }

    @Test
    void testDelete() {
        distributionDao.delete(mockDistribution);

        // verify transaction and remove methods are called
        verify(mockTransaction).begin();
        verify(mockEm).remove(mockDistribution);
        verify(mockTransaction).commit();
    }
}
