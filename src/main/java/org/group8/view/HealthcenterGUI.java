package org.group8.view;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.event.EventHandler;
import javafx.scene.layout.FlowPane;
import javafx.scene.control.Spinner;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import org.group8.Simulator;
import org.group8.controller.HealthcenterController;
public class HealthcenterGUI extends Application {
    private Label result = new Label("Simulation output goes here");
    private HealthcenterController controller;

    public void start(Stage stage) {
        TextField numberField = new TextField();
        VBox vbox = new VBox(numberField);
        Button setButton = new Button("Set Timer");
        Button runButton = new Button("Run Simulation");
        FlowPane panel = new FlowPane();

        stage.setTitle("Health center simulation");

        Insets insets = new Insets(10, 10, 10, 10);
        panel.setMargin(vbox, insets);
        panel.setMargin(setButton, insets);
        panel.setMargin(runButton, insets);

        result.setMinWidth(30);
        result.setAlignment(Pos.BOTTOM_LEFT);
        panel.getChildren().add(vbox);
        panel.getChildren().add(setButton);
        panel.getChildren().add(runButton);
        panel.getChildren().add(result);
        Scene scene = new Scene(panel);
        stage.setScene(scene);
        stage.show();

        setButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                controller.setTime(Integer.parseInt(numberField.getText()));
            }
        });
        runButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                controller.runCentre();
            }
        });

        stage.show();
    }


    public void init() {
        controller = new HealthcenterController(this);
    }

    public void setResult(String result) {
        this.result.setText(result);
    }
}
