package org.group8.simulator.model;

import org.group8.simulator.framework.IEventType;

/**
 * The {@code EventType} enum represents different types of events that occur in the simulation.
 * Each event corresponds to a specific step in the patient's journey through the healthcare center.
 */
public enum EventType implements IEventType {
    ARR_CHECKIN, DEP_CHECKIN, DEP_DOCTOR, DEP_LAB, DEP_XRAY, DEP_TREATMENT;
}
