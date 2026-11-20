package com.alphagen.studio.FloatDataVisualizer.expo;


import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TestGraphic extends Scene {
    private static final Logger logger = LogManager.getLogger(TestGraphic.class);
    private final SerialReader reader;

    public TestGraphic() {

        super(new Pane());

        Button startButton = new Button("Start");
        startButton.setPrefSize(60, 20);

        Button stopButton = new Button("Stop");
        stopButton.setPrefSize(60, 20);

        Label label = new Label("-");
        label.setPadding(new Insets(0, 0, 0, 5));

        reader = new SerialReader(data -> {
            Platform.runLater(() -> {
                label.setText(String.format("Time: %.2f\tDepth: %.2f", data.time(), data.depth()));
            });
        });

        stopButton.setVisible(false);

        startButton.setOnAction(event -> {
            startButton.setVisible(false);
            startButton.setManaged(false);
            stopButton.setVisible(true);
        });

        stopButton.setOnAction(event -> {
            stopButton.setVisible(false);
            stopButton.setManaged(false);
            startButton.setVisible(true);
        });

        VBox vbox = new VBox(10);
        vbox.setPrefSize(500, 200);
        vbox.getChildren().add(startButton);
        vbox.getChildren().add(stopButton);

        super(vbox);
    }

}
