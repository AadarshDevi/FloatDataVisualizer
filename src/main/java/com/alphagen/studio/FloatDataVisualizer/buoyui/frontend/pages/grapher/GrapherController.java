package com.alphagen.studio.FloatDataVisualizer.buoyui.frontend.pages.grapher;

import com.alphagen.studio.FloatDataVisualizer.buoyui.frontend.managers.StageManager;
import com.alphagen.studio.FloatDataVisualizer.buoyui.frontend.util.StageUtil;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

public class GrapherController {
	@FXML public TabPane graphPane;
	@FXML public Tab controlsTab;
	@FXML public Tab tableTab;

	@FXML public Button startDataTransfer;
	@FXML public Button stopDataTransfer;

	@FXML
	public void initialize() {
		System.out.println("Initializing Grapher");
	}

	@FXML
	public void startingDataTransfer() {}

	@FXML
	public void stopingDataTransfer() {}

	@FXML
	public void backHome() {
		Stage stage = StageManager.getMainStage();
		Scene scene = StageUtil.getConnectionsScene();
		stage.setScene(scene);
	}
}