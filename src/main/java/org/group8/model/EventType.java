package org.group8.model;

import org.group8.framework.IEventType;

public enum EventType implements IEventType {
    ARR_CHECKIN, DEP_CHECKIN, ARR_DOCTOR, DEP_DOCTOR, ARR_LAB, DEP_LAB, ARR_XRAY, DEP_XRAY, ARR_TREATMENT, DEP_TREATMENT, END_VISIT;
}
