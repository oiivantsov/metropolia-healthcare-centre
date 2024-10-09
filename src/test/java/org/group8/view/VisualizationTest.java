package org.group8.view;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

class VisualizationTest {

    private Visualization visualization;
    private GraphicsContext mockGc;

    @BeforeEach
    void setUp() {
        // mock the GraphicsContext to avoid real rendering
        mockGc = mock(GraphicsContext.class);

        // create a Visualization instance with mocked GraphicsContext
        visualization = new Visualization(300, 300) {
            @Override
            public GraphicsContext getGraphicsContext2D() {
                return mockGc;
            }
        };
    }

    @Test
    void testClearDisplay() {
        // reset the mock to clear any previous interactions
        reset(mockGc);

        visualization.clearDisplay();

        verify(mockGc).setFill(Color.LIGHTBLUE);
        verify(mockGc).fillRect(0, 0, 300, 300);
    }

}
