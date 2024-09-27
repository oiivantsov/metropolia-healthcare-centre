package org.group8;

import org.group8.simulator.framework.Trace;
import org.group8.view.HealthcenterGUI;

public class Simulator {
    
    public static void main(String[] args) {
        Trace.setTraceLevel(Trace.Level.INFO);
        HealthcenterGUI.launch(HealthcenterGUI.class);
    }
}
