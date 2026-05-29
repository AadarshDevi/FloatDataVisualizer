package com.alphagen.studio.FloatDataVisualizer.buoyui;

import com.alphagen.studio.FloatDataVisualizer.Launcher;
import com.alphagen.studio.FloatDataVisualizer.buoyui.backend.Backend;
import com.alphagen.studio.FloatDataVisualizer.buoyui.backend.app.PlatformDetector;
import com.alphagen.studio.FloatDataVisualizer.buoyui.frontend.managers.StageManager;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Objects;

public class BuoyUI extends Application {

	private static final Logger LOGGER = LogManager.getLogger(BuoyUI.class);

	private final String FOLDER_CONNECTIONS = "connections";
	private final String FILE_LOG = "float.log";
	private final String FILE_SETTINGS = "float.settings";

	@Override
	public void start(Stage stage) throws Exception {

		StageManager.setMainStage(stage);
		PlatformDetector.detectPlatform();

		LOGGER.info("Initializing Backend");
		Backend backend = Backend.getBackend();

		LOGGER.info("Finding Folders");

		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.setHeaderText(null);

		if (!backend.verifyRoot()) {
			alert.setTitle("Exception 1");
			alert.setContentText("Root folder is unable to be found or created.");
			alert.showAndWait();
			LOGGER.error("Exception 1: Root folder is unable to be found or created.");
			System.exit(-1);
		}

		if (!backend.verifyFolder(FOLDER_CONNECTIONS)) {
			alert.setTitle("Exception 2");
			alert.setContentText("Base folder is unable to be found or created.\nBase Folder: connections");
			alert.showAndWait();
			LOGGER.error("Exception 2: Base folder is unable to be found or created: {}", FOLDER_CONNECTIONS);
			System.exit(-1);
		}

		if (!backend.verifyFile(FILE_LOG)) {
			alert.setTitle("Exception 3");
			alert.setContentText("Base folder is unable to be found or created.\nBase Folder: logs");
			alert.showAndWait();
			LOGGER.error("Exception 3: File is unable to be found or created: {}", FILE_LOG);
			System.exit(-1);
		}

		if (!backend.verifyFile(FILE_SETTINGS)) {
			alert.setTitle("Exception 4");
			alert.setContentText("");
			alert.showAndWait();
			LOGGER.error("Exception 4: File is unable to be found or created: {}", FILE_SETTINGS);
			System.exit(-1);
		}
		LOGGER.info("All startup folders and files are ready.");

		// todo: read settings file

		StageManager.setConnectionsScene();
		Scene scene = StageManager.getConnectionsScene();
		stage.setScene(scene);
		stage.setTitle("Float Data Visualizer");
		stage.getIcons().add(new Image(Objects.requireNonNull(Launcher.class.getResourceAsStream("buoyui/logos/ImageLogo.png"))));

		LOGGER.info("Opening App");
		stage.show();
	}
}