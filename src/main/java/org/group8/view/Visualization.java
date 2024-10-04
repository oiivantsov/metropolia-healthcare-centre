package org.group8.view;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

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

    public Visualization(int w, int h) {
        super(w, h);
        gc = this.getGraphicsContext2D();
        clearDisplay();
    }

    public void clearDisplay() {
        // Set the background color
        gc.setFill(Color.LIGHTBLUE);
        gc.fillRect(0, 0, this.getWidth(), this.getHeight());

        // Reset the position of the patients when a new simulation starts
        i = hPadding;
        j = vPadding;
    }

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

    public void removePatient() {

        // Check if we need to move to the previous row
        if (i <= hPadding) {
            i = this.getWidth() - hPadding;  // Move to the last column
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
