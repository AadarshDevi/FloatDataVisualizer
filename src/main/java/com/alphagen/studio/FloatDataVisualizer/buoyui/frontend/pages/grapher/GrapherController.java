package com.alphagen.studio.FloatDataVisualizer.buoyui.frontend.pages.grapher;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

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
}