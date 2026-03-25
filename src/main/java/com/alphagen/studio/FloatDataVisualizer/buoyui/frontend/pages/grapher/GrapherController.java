package com.alphagen.studio.FloatDataVisualizer.buoyui.frontend.pages.grapher;

import com.alphagen.studio.FloatDataVisualizer.buoyui.backend.data.ConnectionConfig;
import com.alphagen.studio.FloatDataVisualizer.buoyui.frontend.managers.StageManager;
import com.alphagen.studio.FloatDataVisualizer.buoyui.frontend.util.StageUtil;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;
import lombok.Setter;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GrapherController {
	private final ExecutorService receiver = Executors.newSingleThreadExecutor();
	private final ExecutorService database = Executors.newSingleThreadExecutor();
	@FXML public TabPane graphPane;
	@FXML public Tab controlsTab;
	@FXML public Tab tableTab;
	@FXML public Button startDataTransfer;
	@FXML public Button stopDataTransfer;
	@FXML public Label connection_name_label;
	@Setter private ConnectionConfig connectionConfig;

	// fixme: min/max col width > computed from pref
	// todo: add css in the fxml for the table rows, focused, selected, hover, normal, font

	@FXML
	public void initialize() {
		System.out.println(" >>> Serial Communication > Initializing");
	}

	@FXML
	public void startingDataTransfer() {
		System.out.println();
		System.out.println(" >>> Serial Communication > Start");
	}

	@FXML
	public void stopingDataTransfer() {
		System.out.println(" >>> Serial Communication > Stop");
	}

	@FXML
	public void backHome() {
		System.out.println(" >>> Serial Communication > Back Home");
		Stage stage = StageManager.getMainStage();
		Scene scene = StageUtil.getConnectionsScene();
		stage.setScene(scene);
	}

	public void setup() {
		if (connectionConfig == null) {
			System.out.println(" >>> Serial Communication > Null ConnectionConfig");
			return;
		}

		connection_name_label.setText(connectionConfig.connectionName());
		// todo: setup tabs > depth, pressure etc
	}

	// todo used to reset table, tabs
	public void reset() {
		// use an alert
		System.out.println(" >>> Serial Communication > Reset Grapher");
	}
}