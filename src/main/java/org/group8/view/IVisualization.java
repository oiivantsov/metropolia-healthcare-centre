package org.group8.view;

/**
 * The IVisualization interface defines methods for visualizing patient flow in the simulation.
 * It provides methods for clearing the display, adding new patients, and removing patients.
 */
public interface IVisualization {

    void clearDisplay();  // Method to clear the display
    void newPatient(String emoji);   // Method to add a new customer to the visualization
    void removePatient();   // Method to remove a customer from the visualization

}
