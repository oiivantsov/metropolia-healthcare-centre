package org.group8.simulator.model;

import org.group8.simulator.framework.Trace;
import org.group8.simulator.framework.EventList;
import org.group8.distributions.SampleGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ServicePointTest {

    private ServicePoint servicePoint;
    private SampleGenerator mockGenerator;
    private EventList mockEventList;
    private EventType mockEventType;
    private Patient mockPatient;

    @BeforeEach
    void setUp() {
        // use mock to simulate the dependencies
        mockGenerator = mock(SampleGenerator.class);
        mockEventList = mock(EventList.class);
        mockEventType = mock(EventType.class);
        mockPatient = mock(Patient.class);
        servicePoint = new ServicePoint(mockGenerator, mockEventList, mockEventType);
        Trace.setTraceLevel(Trace.Level.INFO);
    }

    @Test
    void testAddToQueue() {
        servicePoint.addToQueue(mockPatient);
        assertTrue(servicePoint.hasQueue());
    }

    @Test
    void testRemoveFromQueue() {
        servicePoint.addToQueue(mockPatient);
        Patient removedPatient = servicePoint.removeFromQueue();
        assertEquals(mockPatient, removedPatient);
        assertFalse(servicePoint.hasQueue());
    }

    @Test
    void testStartService() {
        when(mockGenerator.sampleAsDouble()).thenReturn(5.0); // sample time returned by generator
        servicePoint.addToQueue(mockPatient);
        servicePoint.startService();

        assertTrue(servicePoint.isBusy());
        verify(mockEventList).add(any()); // verify that an event was added to the event list
    }

    @Test
    void testStartServiceWhenQueueIsEmpty() {
        servicePoint.startService();
        assertFalse(servicePoint.isBusy()); // service should not start if queue is empty
    }

    @Test
    void testIsBusy() {
        assertFalse(servicePoint.isBusy()); // initially not busy
        servicePoint.addToQueue(mockPatient);
        servicePoint.startService();
        assertTrue(servicePoint.isBusy()); // busy after starting service
    }

    @Test
    void testHasQueue() {
        assertFalse(servicePoint.hasQueue()); // initially no queue
        servicePoint.addToQueue(mockPatient);
        assertTrue(servicePoint.hasQueue()); // queue should be true after adding a patient
    }
}
