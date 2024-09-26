package org.group8;

import org.group8.framework.AbstractHealthCentre;
import org.group8.framework.Trace;
import org.group8.model.HealthCentre;
import org.group8.view.HealthcenterGUI;
public class Simulator {
    
    public static void main(String[] args) {
        Trace.setTraceLevel(Trace.Level.INFO);
        HealthcenterGUI.launch(HealthcenterGUI.class);
        //AbstractHealthCentre hc = new HealthCentre();
        //hc.setSimulationTime(1000);
        //hc.run();
    }
}
