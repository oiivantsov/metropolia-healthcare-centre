package org.group8;

import io.github.cdimascio.dotenv.Dotenv;
import org.group8.simulator.framework.Trace;
import org.group8.view.HealthcenterGUI;

/**
 * The main class for starting the Healthcenter simulation application
 */
public class Main {
    /**
     * The main method which is the entry point for the application
     * @param args The command line arguments that are not used
     */
    public static void main(String[] args) {
        // load env variables (you need to create a .env file in the root of the project)
        Dotenv dotenv = Dotenv.load();
        System.setProperty("JDBC_URL", dotenv.get("JDBC_URL"));
        System.setProperty("JDBC_USER", dotenv.get("JDBC_USER"));
        System.setProperty("JDBC_PASSWORD", dotenv.get("JDBC_PASSWORD"));

        // start the application
        Trace.setTraceLevel(Trace.Level.INFO);
        HealthcenterGUI.launch(HealthcenterGUI.class);
    }
}
