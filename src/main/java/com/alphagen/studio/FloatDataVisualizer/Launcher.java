package com.alphagen.studio.FloatDataVisualizer;

import com.alphagen.studio.FloatDataVisualizer.buoyui.BuoyUI;
import javafx.application.Application;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Launcher {

	private static final Logger LOGGER = LogManager.getLogger(Launcher.class);

	public static void main(String[] args) {
		LOGGER.info("Starting FloatDataVisualizer");
		Application.launch(BuoyUI.class, args);
	}

}