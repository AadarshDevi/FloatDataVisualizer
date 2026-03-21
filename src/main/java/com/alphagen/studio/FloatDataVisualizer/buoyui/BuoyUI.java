package com.alphagen.studio.FloatDataVisualizer.buoyui;

import com.alphagen.studio.FloatDataVisualizer.Launcher;
import com.alphagen.studio.FloatDataVisualizer.buoyui.backend.Backend;
import com.alphagen.studio.FloatDataVisualizer.buoyui.backend.app.PlatformDetector;
import com.alphagen.studio.FloatDataVisualizer.buoyui.backend.data.ConnectionConfig;
import com.alphagen.studio.FloatDataVisualizer.buoyui.backend.processor.ConnectionProcessor;
import com.alphagen.studio.FloatDataVisualizer.buoyui.frontend.managers.ControllerManager;
import com.alphagen.studio.FloatDataVisualizer.buoyui.frontend.managers.StageManager;
import com.alphagen.studio.FloatDataVisualizer.buoyui.frontend.pages.PageConstants;
import com.alphagen.studio.FloatDataVisualizer.buoyui.frontend.pages.connections.ConnectionsController;
import com.alphagen.studio.FloatDataVisualizer.buoyui.frontend.util.StageUtil;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Objects;

public class BuoyUI extends Application {

	@Override
	public void start(Stage stage) throws Exception {

		PlatformDetector.detectPlatform();

		System.out.println("Initializing Backend");
		Backend backend = Backend.getBackend();

		System.out.println("Finding Folders");

		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.setHeaderText("Error");

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


		System.out.println("Loading ConnectionsUI");
//		FXMLLoader fxmlLoader = new FXMLLoader(PageConstants.GRAPHING_PAGE);
		FXMLLoader fxmlLoader = new FXMLLoader(PageConstants.CONNECTIONS_PAGE);
		BorderPane buoyUI = fxmlLoader.load();
		ConnectionsController bmc = fxmlLoader.getController();
//
//		BorderPane graphUI = fxmlLoader.load();
//		GrapherController gc = fxmlLoader.getController();

		ControllerManager.setConnectionsController(bmc);

		ArrayList<ConnectionConfig> connectionsList = ConnectionProcessor.readAllConnections();
		System.out.println("Connections Found: " + connectionsList.size());
		if (connectionsList != null) {
			bmc.setConnectionConfigs(connectionsList);
		}
//
//		Scene scene = new Scene(graphUI);
		Scene scene = new Scene(buoyUI);
		stage.setScene(scene);
		stage.setTitle("Float Data Visualizer");
		stage.getIcons().add(new Image(Objects.requireNonNull(Launcher.class.getResourceAsStream("buoyui/logos/ImageLogo.png"))));
		StageManager.setMainStage(stage);
//		StageUtil.createInvisPane(stage, scene, graphUI);
		StageUtil.createInvisPane(stage, scene, buoyUI);

		System.out.println("Opening App");
		stage.show();

//		StageManager.getMainStage();
//		FXMLLoader grapherLoader = new FXMLLoader(Launcher.class.getResource("/com/alphagen/studio/FloatDataVisualizer/buoyui/frontend/pages/grapher/Graping_v1.fxml"));
//		BorderPane grapherUI = grapherLoader.load();
//		GrapherController gc = grapherLoader.getController();
//
//		scene.setRoot(grapherUI);
//		stage.setScene(scene);
//		stage.show();
	}
}