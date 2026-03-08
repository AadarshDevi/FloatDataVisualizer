package com.alphagen.studio.FloatDataVisualizer.buoyui.connections;

import com.alphagen.studio.FloatDataVisualizer.buoyui.managers.StageManager;
import com.alphagen.studio.FloatDataVisualizer.buoyui.util.DeltaDrag;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.TilePane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class ConnectionsController {

	@FXML public Button connection_add;
	@FXML public Button app_quit;
	@FXML public TilePane connections;

	@FXML
	public void test() {
		System.out.println("Created Connection");
	}

	@FXML
	public void createConnection() {

		System.out.println("Opening ConnectionEditorUI");

		FXMLLoader fxmlLoader = new FXMLLoader(ConnectionsController.class.getResource(ConnectionUIConstants.CONNECTION_EDITOR));
		BorderPane connectionCreatorPane = null;
		try {
			connectionCreatorPane = fxmlLoader.load();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		ConnectionEditorController cec = fxmlLoader.getController();

		Scene connectionCreatorScene = new Scene(connectionCreatorPane);
		connectionCreatorScene.setFill(Color.TRANSPARENT);

		Stage connectionCreatorStage = new Stage();
		connectionCreatorStage.setScene(connectionCreatorScene);
		connectionCreatorStage.initStyle(StageStyle.TRANSPARENT);
		connectionCreatorStage.initModality(Modality.APPLICATION_MODAL);
		connectionCreatorStage.initOwner(StageManager.getMainStage());
		connectionCreatorStage.setTitle("Float Data Visualizer");

		StageManager.setConnectionCreatorStage(connectionCreatorStage);
		final DeltaDrag connectionEditorDrag = new DeltaDrag();

		connectionCreatorPane.setOnMousePressed(event -> {
			connectionEditorDrag.setDeltaX(event.getSceneX());
			connectionEditorDrag.setDeltaY(event.getSceneY());
		});

		// mouse dragged: move the stage
		connectionCreatorPane.setOnMouseDragged(event -> {
			connectionCreatorStage.setX(event.getScreenX() - connectionEditorDrag.getDeltaX());
			connectionCreatorStage.setY(event.getScreenY() - connectionEditorDrag.getDeltaY());
		});

		connectionCreatorStage.showAndWait();

		try {

			System.out.println("Creating Connection");

			if (ConnectionManager.getCurrentConnection() == null) return;

//			FXMLLoader
			fxmlLoader = new FXMLLoader(ConnectionsController.class.getResource(ConnectionUIConstants.DATA_CARD));
			Button dataCard = fxmlLoader.load();
			DataCardController dcc = fxmlLoader.getController();
			dcc.setDataCard(
					ConnectionManager.getCurrentConnection().getName(),
					ConnectionManager.getCurrentConnection().getBaudRate(),
					ConnectionManager.getCurrentConnection().getPort(),
					ConnectionManager.getCurrentConnection().getConnectionType()
			);
//			dcc.setDataCard(
//					"Serial Comms",
//					115200,
//					"COM3",
//					ConnectionType.WIRELESS
//			);
			connections.getChildren().add(dataCard);
			System.out.println("Data 1-2: " + dataCard.hashCode());

		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public void deleteConnection(Button deleteButton) {
		connections.getChildren().remove(deleteButton);
	}

	@FXML
	public void quitApp() {
		System.out.println("App Quit");
		System.exit(0);
	}
}