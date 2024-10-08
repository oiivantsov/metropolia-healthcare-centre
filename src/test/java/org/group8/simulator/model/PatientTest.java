package org.group8.simulator.model;

import org.group8.simulator.framework.Trace;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.group8.simulator.framework.Clock;
import static org.junit.jupiter.api.Assertions.*;

class PatientTest {

    @BeforeEach
    public void setUp() {
        // reset the static counters, trace level and clock
        Patient.reset();
        Clock.getInstance().setTime(0.0);
        Trace.setTraceLevel(Trace.Level.INFO);
    }

    @Test
    public void testPatientArrival() {
        Clock.getInstance().setTime(10.0);
        Patient patient = new Patient();

        assertEquals(1, Patient.getTotalPatients());
        assertEquals(10.0, patient.getArrivalTime());
    }

    @Test
    public void testPatientDeparture() {
        Clock.getInstance().setTime(5.0);
        Patient patient = new Patient();

        patient.setDepartureTime(15.0);

        assertEquals(15.0, patient.getDepartureTime());
    }

    @Test
    public void testReportCalculation() {
        Clock.getInstance().setTime(5.0);
        Patient patient = new Patient();

        patient.setDepartureTime(20.0);

        patient.report();

        assertEquals(15.0, Patient.getTotalTime());  // total time should be 15 (20 - 5)
        assertEquals(1, Patient.getCompletedPatients());  // one patient should be completed
    }

    @Test
    public void testMultiplePatients() {
        // 1st patient
        Clock.getInstance().setTime(5.0);
        Patient patient1 = new Patient();
        patient1.setDepartureTime(10.0);
        patient1.report();

        // 2nd patient
        Clock.getInstance().setTime(12.0);
        Patient patient2 = new Patient();
        patient2.setDepartureTime(22.0);
        patient2.report();

        assertEquals(2, Patient.getTotalPatients());  // 2 patients should have been created
        assertEquals(15.0, Patient.getTotalTime());  // total time should be 15 (5 for first, 10 for second)
        assertEquals(2, Patient.getCompletedPatients());  // 2 patients should have completed
    }

    @Test
    public void testReset() {
        // add some patients first
        Clock.getInstance().setTime(10.0);
        Patient patient1 = new Patient();
        patient1.setDepartureTime(20.0);
        patient1.report();

        Patient patient2 = new Patient();
        patient2.setDepartureTime(30.0);
        patient2.report();

        // reset the statistics
        Patient.reset();

        assertEquals(0, Patient.getTotalPatients());  // patient counter should be reset
        assertEquals(0, Patient.getTotalTime());  // total time should be reset
        assertEquals(0, Patient.getCompletedPatients());  // completed patients should be reset
    }
}
