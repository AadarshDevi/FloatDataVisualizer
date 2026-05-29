package com.alphagen.studio.FloatDataVisualizer.buoyui.frontend.managers;

import com.alphagen.studio.FloatDataVisualizer.buoyui.backend.data.ConnectionConfig;
import com.alphagen.studio.FloatDataVisualizer.buoyui.frontend.pages.PageConstants;
import com.alphagen.studio.FloatDataVisualizer.buoyui.frontend.pages.connections.ConnectionsController;
import com.alphagen.studio.FloatDataVisualizer.buoyui.frontend.pages.grapher.GrapherController;
import com.alphagen.studio.FloatDataVisualizer.buoyui.frontend.util.StageUtil;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;

public class StageManager {

	private static final Logger LOGGER = LogManager.getLogger(StageManager.class);
	@Getter
	private static Stage mainStage;
	@Getter
	@Setter
	private static Stage connectionCreatorStage;
	@Getter private static Scene connectionsScene;
	@Getter private static Scene graphingScene;

	public static void setMainStage(Stage stage) {
		mainStage = stage;
		mainStage.initStyle(StageStyle.TRANSPARENT);
	}

	public static void setConnectionsScene() {
		LOGGER.info("Loading ConnectionsUI");
		FXMLLoader fxmlLoader = new FXMLLoader(PageConstants.CONNECTIONS_PAGE);
		BorderPane buoyUI = null;
		try {
			buoyUI = fxmlLoader.load();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		ConnectionsController cc = fxmlLoader.getController();

		ControllerManager.setConnectionsController(cc);

		ArrayList<ConnectionConfig> connectionsList = Connections.Processor.readAllConnections();
		if (connectionsList != null) {
			LOGGER.info("Connections Found: {}", connectionsList.size());
			cc.setConnectionConfigs(connectionsList);
		}

		Scene scene = new Scene(buoyUI);
		buoyUI.getProperties().put("grapher", cc);
		StageUtil.customTitleBarDrag(StageManager.getMainStage(), scene, buoyUI);
		StageManager.getMainStage().initStyle(StageStyle.TRANSPARENT);
		connectionsScene = scene;
	}


	public static GrapherController setGraphingScene() {

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
//		StageManager.createInvisPane(scene, grapherUI);
		StageUtil.customTitleBarDrag(StageManager.getMainStage(), scene, grapherUI);
//		StageManager.getMainStage().initStyle(StageStyle.TRANSPARENT);
		graphingScene = scene;
		return gc;
	}
}