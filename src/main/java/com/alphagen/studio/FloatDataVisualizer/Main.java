package com.alphagen.studio.FloatDataVisualizer;


import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Main extends Application {
    private static final Logger logger = LogManager.getLogger(Main.class);

    @Override
    public void start(Stage stage) throws Exception {
        Button startButton = new Button("Start");
        startButton.setPrefSize(60, 20);

        Button stopButton = new Button("Stop");
        stopButton.setPrefSize(60, 20);

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

        Scene scene = new Scene(vbox);
        stage.setScene(scene);
        stage.setTitle("MateFloat FloatDataVisualizer Data Reader Testing UI Panel");
        logger.info("Application has been started.");
        stage.show();
    }
}
