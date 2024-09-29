package org.group8.view;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Visualization extends Canvas implements IVisualization {

    private final GraphicsContext gc;

    double i = 0;
    double j = 10;

    public Visualization(int w, int h) {
        super(w, h);
        gc = this.getGraphicsContext2D();
        clearDisplay();
    }

    public void clearDisplay() {
        gc.setFill(Color.LIGHTBLUE);
        gc.fillRect(0, 0, this.getWidth(), this.getHeight());

        // reset the position of the patients when new simulation starts
        i = 0;
        j = 10;
    }

    public void newPatient() {
        gc.setFill(Color.GREEN);
        gc.fillOval(i, j, 10, 10);

        i = (i + 10) % this.getWidth();
        // j = (j + 12) % this.getHeight();
        if (i == 0) j += 10;
    }
}

