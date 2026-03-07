package com.alphagen.studio.FloatDataVisualizer.buoyui.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.TilePane;
import javafx.scene.paint.Color;
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
//		FXMLLoader fxmlLoader1 = new FXMLLoader(Launcher.class.getResource("buoyui/ui/connections/DataCard.fxml"));
//		FXMLLoader fxmlLoader3 = new FXMLLoader(Launcher.class.getResource("buoyui/ui/connections/DataCard.fxml"));
//
//		try {
//			Button dataCard1 = fxmlLoader1.load();
//			DataCardController dcc1 = fxmlLoader1.getController();
//			dcc1.setDataCard("ESP32 Devkit C", 115200, "////.//COM3", ConnectionType.SERIAL);
//			connections.getChildren().add(dataCard1);
//
//			Button dataCard3 = fxmlLoader3.load();
//			DataCardController dcc3 = fxmlLoader3.getController();
//			dcc3.setDataCard("Arduino Uno Q", 9600, "//dev//ttyamo3", ConnectionType.WIRELESS);
//			connections.getChildren().add(dataCard3);
//
//		} catch (IOException e) {
//			throw new RuntimeException(e);
//		}

		FXMLLoader fxmlLoader = new FXMLLoader(ConnectionsController.class.getResource("buoyui/ui/connections/ce1.fxml"));
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

		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle("Information: Create Connection");
		alert.setHeaderText("Create Connection");
		alert.setGraphic(null);
		alert.showAndWait();

		Alert alert2 = new Alert(Alert.AlertType.CONFIRMATION);
		alert2.setTitle("Confirmation: Create Connection");
		alert2.setHeaderText("Create Connection");
		alert2.setGraphic(null);
		alert2.showAndWait();

		Alert alert3 = new Alert(Alert.AlertType.ERROR);
		alert3.setTitle("Error: Create Connection");
		alert3.setHeaderText("Create Connection");
		alert3.setGraphic(null);
		alert3.showAndWait();

		Alert alert5 = new Alert(Alert.AlertType.WARNING);
		alert5.setTitle("Warning: Create Connection");
		alert5.setHeaderText("Create Connection");
		alert5.setGraphic(null);
		alert5.showAndWait();
	}

	@FXML
	public void quitApp() {
		System.out.println("App Quit");
		System.exit(0);
	}
}