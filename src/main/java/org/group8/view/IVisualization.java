package org.group8.view;

/**
 * The IVisualization interface defines the contract for visualizing
 * patient-related information in the Health Center application.
 * It provides methods for managing the display of patients in
 * various service points, allowing for dynamic updates during
 * the simulation.
 *
 * <p>This interface is intended to be implemented by classes
 * that handle the graphical representation of patients within
 * the GUI, facilitating the addition and removal of patient
 * visualizations.</p>
 */
public interface IVisualization {

    /**
     * Clears the current display of the visualization.
     * This method is typically called when a new simulation starts
     * or when the display needs to be reset.
     */
    void clearDisplay();  // Method to clear the display

    /**
     * Adds a new patient to the visualization using a specified emoji.
     *
     * @param emoji a String representing the emoji or visual
     *              representation of the patient's current state.
     */
    void newPatient(String emoji);   // Method to add a new customer to the visualization

    /**
     * Removes the most recently added patient from the visualization.
     * This method is used to update the display when a patient
     * leaves or is processed, ensuring that the visualization remains
     * accurate.
     */
    void removePatient();   // Method to remove a customer from the visualization

}
