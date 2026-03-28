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

import java.util.Objects;

public class BuoyUI extends Application {

	@Override
	public void start(Stage stage) throws Exception {

		StageManager.setMainStage(stage);
		PlatformDetector.detectPlatform();

		System.out.println("Initializing Backend");
		Backend backend = Backend.getBackend();

		System.out.println("Finding Folders");

		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.setHeaderText(null);

		if (!backend.verifyRoot()) {
			alert.setTitle("Exception 1");
			alert.setContentText("Root folder is unable to be found or created.");
			alert.showAndWait();
			System.exit(-1);
		}

		if (!backend.verifyFolder("connections")) {
			alert.setTitle("Exception 2");
			alert.setContentText("Base folder is unable to be found or created.\nBase Folder: connections");
			alert.showAndWait();
			System.exit(-1);
		}

		if (!backend.verifyFolder("logs")) {
			alert.setTitle("Exception 3");
			alert.setContentText("Base folder is unable to be found or created.\nBase Folder: logs");
			alert.showAndWait();
			System.exit(-1);
		}

		if (!backend.verifyFile("float.settings")) {
			alert.setTitle("Exception 4");
			alert.setContentText("File is unable to be found or created.\nFile: float.settings");
			alert.showAndWait();
			System.exit(-1);
		}
		System.out.println("All startup folders and settings file ready.");

		// todo: read settings file

		StageManager.setConnectionsScene();
		Scene scene = StageManager.getConnectionsScene();
		stage.setScene(scene);
		stage.setTitle("Float Data Visualizer");
		stage.getIcons().add(new Image(Objects.requireNonNull(Launcher.class.getResourceAsStream("buoyui/logos/ImageLogo.png"))));

		System.out.println("Opening App");

		// needed for loading card
		// thread.interrupt();

		stage.show();

	}
}