package com.alphagen.studio.FloatDataVisualizer.buoyui.frontend.util;

import com.alphagen.studio.FloatDataVisualizer.buoyui.backend.data.ConnectionConfig;
import com.alphagen.studio.FloatDataVisualizer.buoyui.backend.processor.ConnectionProcessor;
import com.alphagen.studio.FloatDataVisualizer.buoyui.frontend.managers.ControllerManager;
import com.alphagen.studio.FloatDataVisualizer.buoyui.frontend.managers.StageManager;
import com.alphagen.studio.FloatDataVisualizer.buoyui.frontend.pages.PageConstants;
import com.alphagen.studio.FloatDataVisualizer.buoyui.frontend.pages.connections.ConnectionsController;
import com.alphagen.studio.FloatDataVisualizer.buoyui.frontend.pages.grapher.GrapherController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.Getter;

import java.io.IOException;
import java.util.ArrayList;

public class StageUtil {

	@Getter private static Scene connectionsScene;

	@Getter private static Scene graphingScene;

	public static void setConnectionsScene() {
		System.out.println("Loading ConnectionsUI");
		FXMLLoader fxmlLoader = new FXMLLoader(PageConstants.CONNECTIONS_PAGE);
		BorderPane buoyUI = null;
		try {
			buoyUI = fxmlLoader.load();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		ConnectionsController cc = fxmlLoader.getController();

		ControllerManager.setConnectionsController(cc);

		ArrayList<ConnectionConfig> connectionsList = ConnectionProcessor.readAllConnections();
		System.out.println("Connections Found: " + connectionsList.size());
		if (connectionsList != null)
			cc.setConnectionConfigs(connectionsList);

		Scene scene = new Scene(buoyUI);
		buoyUI.getProperties().put("grapher", cc);
		StageManager.createInvisPane(scene, buoyUI);
		connectionsScene = scene;
	}

	public static void setGraphingScene() {

		System.out.println("Loading GraphingUI");

		FXMLLoader grapherLoader = new FXMLLoader(PageConstants.GRAPHING_PAGE);
		BorderPane grapherUI = null;
		try {
			grapherUI = grapherLoader.load();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		GrapherController gc = grapherLoader.getController();
		Scene scene = new Scene(grapherUI);
		grapherUI.getProperties().put("grapher", gc);
		StageManager.createInvisPane(scene, grapherUI);
		graphingScene = scene;
	}

	public static void exceptionAlert(boolean success, int exceptionNum, String errorMessage, String positiveLog) {
		if (!success) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Exception " + exceptionNum);
			alert.setHeaderText("Error");
			alert.setContentText(errorMessage);
			alert.showAndWait();
			System.exit(-1);
		}
		System.out.println(positiveLog);
		System.out.println();
	}

	public static Stage getConnectionEditor(BorderPane pane) {
		Scene scene = new Scene(pane);
		Stage stage = new Stage();
		stage.setScene(scene);
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.initOwner(StageManager.getMainStage());
		stage.setTitle("Float Data Visualizer");
		StageManager.createInvisPane(scene, pane);
		StageManager.setConnectionCreatorStage(stage);
		return stage;
	}
}