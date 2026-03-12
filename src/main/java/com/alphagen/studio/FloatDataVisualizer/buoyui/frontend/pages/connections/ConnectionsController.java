package com.alphagen.studio.FloatDataVisualizer.buoyui.frontend.pages.connections;

import com.alphagen.studio.FloatDataVisualizer.buoyui.backend.data.ConnectionConfig;
import com.alphagen.studio.FloatDataVisualizer.buoyui.backend.data.FloatConfig;
import com.alphagen.studio.FloatDataVisualizer.buoyui.backend.processor.ConnectionProcessor;
import com.alphagen.studio.FloatDataVisualizer.buoyui.frontend.managers.ConnectionManager;
import com.alphagen.studio.FloatDataVisualizer.buoyui.frontend.managers.DataCardManager;
import com.alphagen.studio.FloatDataVisualizer.buoyui.frontend.managers.StageManager;
import com.alphagen.studio.FloatDataVisualizer.buoyui.frontend.pages.PageConstants;
import com.alphagen.studio.FloatDataVisualizer.buoyui.frontend.pages.connections.datacard.DataCardController;
import com.alphagen.studio.FloatDataVisualizer.buoyui.frontend.pages.connections.editor.ConnectionEditorController;
import com.alphagen.studio.FloatDataVisualizer.buoyui.frontend.util.StageUtil;
import com.alphagen.studio.FloatDataVisualizer.buoyui.lib.fxml.NodePackage;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.TilePane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.util.ArrayList;

public class ConnectionsController {

	@FXML public TilePane connections;

	@Setter
	@Getter
	private ConnectionConfig currentConnectionConfig;
	@Setter
	@Getter
	private FloatConfig currentFloatConfig;

	@FXML
	public void initialize() {}

	@FXML
	public void test() {
		System.out.println("Created Connection");
	}

	@FXML
	public void createConnection() {

		System.out.println("Opening ConnectionEditorUI");
		BorderPane connectionCreatorPane = null;
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(PageConstants.CONNECTIONS_EDITOR_PAGE);
			connectionCreatorPane = fxmlLoader.load();
			ConnectionEditorController cec = fxmlLoader.getController();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		Stage editor = getConnectionEditor(connectionCreatorPane);
		editor.showAndWait();

		if (ConnectionManager.getCurrentConnection() == null) {
			return;
		}
		System.out.println("Creating Connection");

		// better datacard manager/processor
		NodePackage<DataCardController> dcnp = DataCardManager.create(ConnectionManager.getCurrentConnection());

		connections.getChildren().add(dcnp.node());
		System.out.println("Data 1-2: " + dcnp.node().hashCode());

		boolean success = ConnectionProcessor.writeConnection(currentConnectionConfig);
		if (!success) {
			// fixme replace with error alert
			System.out.println("Connection Writing Failed");
			System.exit(-1);
		}

		ConnectionProcessor.readAllConnections();
	}

	public Stage getConnectionEditor(BorderPane pane) {
		Scene scene = new Scene(pane);
		Stage stage = new Stage();
		stage.setScene(scene);
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.initOwner(StageManager.getMainStage());
		stage.setTitle("Float Data Visualizer");
		StageUtil.createInvisPane(stage, scene, pane);
		StageManager.setConnectionCreatorStage(stage);
		return stage;
	}

	public void deleteConnection(Button deleteButton) {
		connections.getChildren().remove(deleteButton);
	}

	@FXML
	public void quitApp() {
		System.out.println("App Quit");
		Platform.exit();
	}

	@FXML
	public void deleteAll() {
		// todo add conformation alert before delete all
		System.out.println("Deleting All Connections");
		connections.getChildren().removeAll(connections.getChildren());
	}

	public void setConnectionConfigs(ArrayList<ConnectionConfig> connectionsList) {
//		System.out.println("Adding ConnectionConfigs");
		int count = connectionsList.size();
		for (ConnectionConfig connectionConfig : connectionsList) {
			boolean success = connections.getChildren().add(DataCardManager.create(connectionConfig).node());
			if (!success) {
				System.out.println("Connection adding Failed: " + connectionConfig.connectionName());
				count--;
			}
		}
		System.out.println("Added Connections: " + count);
	}
}