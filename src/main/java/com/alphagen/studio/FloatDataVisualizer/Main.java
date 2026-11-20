package com.alphagen.studio.FloatDataVisualizer;


import com.alphagen.studio.FloatDataVisualizer.expo.TestGraphic;
import javafx.application.Application;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Main extends Application {
    private static final Logger logger = LogManager.getLogger(Main.class);

    @Override
    public void start(Stage stage) throws Exception {
        TestGraphic testGraphic = new TestGraphic();
        stage.setScene(testGraphic);
        stage.setTitle("MateFloat FloatDataVisualizer Data Reader Testing UI Panel");
        logger.info("Application has been started.");
        stage.show();
    }
}
