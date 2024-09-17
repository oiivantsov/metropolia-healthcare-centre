package org.group8;

import org.group8.framework.HealthCentre;
import org.group8.framework.Trace;
import org.group8.model.MyHealthCentre;

public class Simulator {

    public static void main(String[] args) {
        Trace.setTraceLevel(Trace.Level.INFO);
        HealthCentre hc = new MyHealthCentre();
        hc.setSimulationTime(500);
        hc.run();
    }
}
