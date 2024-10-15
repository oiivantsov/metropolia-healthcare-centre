package org.group8.view;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

/**
 * The Visualization class is a custom Canvas that represents various service
 * points in a health center simulation, such as treatment, doctor, lab,
 * x-ray, and check-in services. It utilizes images to visually represent
 * the current state and activities of these service points.
 *
 * This class implements the IVisualization interface and provides methods
 * to draw and update the visualization based on simulation events.
 *
 * <p>Key Features:</p>
 * <ul>
 *   <li>Handles drawing images for different service points.</li>
 *   <li>Provides padding and layout configuration for elements on the canvas.</li>
 *   <li>Utilizes various image resources for visual representation.</li>
 * </ul>
 *
 * <p>Usage:</p>
 * <p>To use this class, instantiate it within your GUI application and
 * call the appropriate methods to update the visual state of the service
 * points as the simulation progresses.</p>
 */
public class Visualization extends Canvas implements IVisualization {

    private final GraphicsContext gc;

    double i = 15;  // Start with some padding from the left
    double j = 15;  // Start with some padding from the top

    private final double hPadding = 15; // Horizontal padding
    private final double vPadding = 20; // Vertical padding
    private final double elementsPadding = 5; // Padding between elements
    private final double IMAGE_SIZE = 25; // Size of the images

    // Load images
    private final Image treatmentImage = new Image(getClass().getResourceAsStream("/images/treatment.png"));
    private final Image sickImage = new Image(getClass().getResourceAsStream("/images/sick.png"));
    private final Image xrayImage = new Image(getClass().getResourceAsStream("/images/xray.png"));
    private final Image labImage = new Image(getClass().getResourceAsStream("/images/lab.png"));
    private final Image doctorImage = new Image(getClass().getResourceAsStream("/images/doctor.png"));
    private final Image defaultImage = new Image(getClass().getResourceAsStream("/images/happy.png"));

    /**
     * Constructs a new {@code Visualization} object with the specified width
     * and height. Initializes the graphics context and clears the display to
     * prepare for drawing.
     *
     * @param w the width of the canvas
     * @param h the height of the canvas
     */
    public Visualization(int w, int h) {
        super(w, h);
        gc = this.getGraphicsContext2D();
        clearDisplay();
    }

    /**
     * Clears the current visualization display by resetting the canvas to its
     * initial state. This method sets the background color to light blue and
     * resets the positions of the patients for a new simulation.
     *
     * <p>This is typically called when starting a new simulation or
     * when the visualization needs to be refreshed.</p>
     *
     * <p>Effectively, this method:</p>
     * <ul>
     *   <li>Fills the entire canvas with a light blue background.</li>
     *   <li>Resets the coordinates used for positioning visual elements to their
     *       initial padding values, ensuring that new elements are drawn correctly.</li>
     * </ul>
     */
    public void clearDisplay() {
        // Set the background color
        gc.setFill(Color.LIGHTBLUE);
        gc.fillRect(0, 0, this.getWidth(), this.getHeight());

        // Reset the position of the patients when a new simulation starts
        i = hPadding;
        j = vPadding;
    }

    /**
     * Adds a new patient to the visualization canvas based on their current state.
     * The patient's state determines which image is drawn to represent them.
     * The method handles the positioning of the patient images to ensure they
     * do not overlap and wraps to the next row when reaching the canvas edge.
     *
     * <p>Patient states correspond to different images:</p>
     * <ul>
     *   <li>"treatment" - draws the treatment image.</li>
     *   <li>"sick" - draws the sick image.</li>
     *   <li>"xray" - draws the x-ray image.</li>
     *   <li>"lab" - draws the lab image.</li>
     *   <li>"doctor" - draws the doctor image.</li>
     *   <li>Any other state - draws a default happy image.</li>
     * </ul>
     *
     * <p>If the width of the canvas is exceeded, the method wraps the images
     * to the next row. If the height of the canvas is also exceeded, the
     * display is cleared to reset the visualization.</p>
     *
     * @param state the current state of the patient, determining the image to draw
     */
    public void newPatient(String state) {
        // Set the fill color (black or another color to ensure visibility)
        gc.setFill(Color.LIGHTBLUE);

        // Draw the corresponding image for the patient state
        switch (state) {
            case "treatment":
                gc.drawImage(treatmentImage, i, j, IMAGE_SIZE, IMAGE_SIZE); // Draws the treatment image
                break;
            case "sick":
                gc.drawImage(sickImage, i, j, IMAGE_SIZE, IMAGE_SIZE); // Draws the sick image
                break;
            case "xray":
                gc.drawImage(xrayImage, i, j, IMAGE_SIZE, IMAGE_SIZE); // Draws the x-ray image
                break;
            case "lab":
                gc.drawImage(labImage, i, j, IMAGE_SIZE, IMAGE_SIZE); // Draws the lab image
                break;
            case "doctor":
                gc.drawImage(doctorImage, i, j, IMAGE_SIZE, IMAGE_SIZE); // Draws the doctor image
                break;
            default:
                gc.drawImage(defaultImage, i, j, IMAGE_SIZE, IMAGE_SIZE); // Draws the default happy image
        }

        // Update position for the next patient
        i += IMAGE_SIZE + elementsPadding;  // Adjust horizontal spacing to avoid overlap

        if (i >= this.getWidth() - hPadding) {
            i = hPadding;
            j += IMAGE_SIZE + elementsPadding;  // Move down to the next row if reaching the edge
        }

        // Ensure patients stay within bounds vertically, reset if necessary
        if (j >= this.getHeight() - vPadding) {
            clearDisplay(); // Clear and reset if out of bounds
        }
    }

    /**
     * Removes the most recently added patient from the visualization canvas.
     * The method adjusts the position of the patient images and handles
     * the wrapping of images when the last patient is removed.
     *
     * <p>When a patient is removed, the following occurs:</p>
     * <ul>
     *   <li>If there are no patients in the current row, it moves to the last column of the previous row.</li>
     *   <li>If the top row is reached and no patients remain, the display is cleared and reset.</li>
     *   <li>The horizontal position is decremented to remove the last patient drawn.</li>
     *   <li>The canvas area occupied by the patient image is cleared, and the background is redrawn to maintain visual consistency.</li>
     * </ul>
     *
     * <p>This method ensures that the patient visualizations remain organized,
     * adjusting to the removal of patients dynamically.</p>
     */
    public void removePatient() {

        // Check if we need to move to the previous row
        if (i <= hPadding) {
            i = this.getWidth() - hPadding;  // Move to the last column
            if (j <= vPadding) {
                clearDisplay(); // Clear and reset if out of bounds
                return;
            }
            j -= (IMAGE_SIZE + elementsPadding);  // Move up to the previous row
        }

        // Adjust the horizontal position
        i -= (IMAGE_SIZE + elementsPadding);

        // Clear the last patient drawn
        gc.clearRect(i + 1, j + 1, IMAGE_SIZE - 2, IMAGE_SIZE - 2);
        // gc.clearRect(i, j, IMAGE_SIZE, IMAGE_SIZE);

        // Redraw the background to maintain consistency
        gc.setFill(Color.LIGHTBLUE);
        gc.fillRect(i, j, IMAGE_SIZE, IMAGE_SIZE);

    }
}
