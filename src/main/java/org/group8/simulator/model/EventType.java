package org.group8.simulator.model;

import org.group8.simulator.framework.IEventType;

/**
 * Enumeration representing the various event types in the simulation.
 * These events correspond to the different stages in the healthcare process.
 */
public enum EventType implements IEventType {

    /**
     * Represents the event when a patient arrives for check-in.
     */
    ARR_CHECKIN,

    /**
     * Represents the event when a patient departs from check-in.
     */
    DEP_CHECKIN,

    /**
     * Represents the event when a patient departs from the doctor's consultation.
     */
    DEP_DOCTOR,

    /**
     * Represents the event when a patient departs from the lab.
     */
    DEP_LAB,

    /**
     * Represents the event when a patient departs from the x-ray.
     */
    DEP_XRAY,

    /**
     * Represents the event when a patient departs from the treatment area.
     */
    DEP_TREATMENT;
}
