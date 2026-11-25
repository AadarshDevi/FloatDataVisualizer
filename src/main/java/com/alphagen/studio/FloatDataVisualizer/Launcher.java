package com.alphagen.studio.FloatDataVisualizer;

import javafx.application.Application;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Launcher {

    private static final Logger LOGGER = LogManager.getLogger(Launcher.class);

    static void main(String[] args) {
        LOGGER.info("Starting FloatDataVisualizer");
//        Application.launch(BuoyUI.class, args);
        Application.launch(Main.class, args);
    }

}