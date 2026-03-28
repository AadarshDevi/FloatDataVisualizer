package com.alphagen.studio.FloatDataVisualizer.buoyui.frontend.util;

import com.alphagen.studio.FloatDataVisualizer.buoyui.backend.util.DeltaDrag;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class StageUtil {


	public static void customTitleBarDrag(Stage stage, Scene scene, BorderPane borderPane) {
		scene.setFill(Color.TRANSPARENT);

		DeltaDrag drag = new DeltaDrag();
		borderPane.setOnMousePressed(event -> {
			drag.setDeltaX(event.getSceneX());
			drag.setDeltaY(event.getSceneY());
		});

		borderPane.setOnMouseDragged(event -> {
			stage.setX(event.getScreenX() - drag.getDeltaX());
			stage.setY(event.getScreenY() - drag.getDeltaY());
		});
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

	public static void setStageInit(Stage stage) {
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.initStyle(StageStyle.TRANSPARENT);
	}

//	public static Stage getConnectionEditor(BorderPane pane) {
//		Scene scene = new Scene(pane);
//		Stage stage = new Stage();
//		stage.setScene(scene);
//		stage.initModality(Modality.APPLICATION_MODAL);
//		stage.initOwner(StageManager.getMainStage());
//		stage.setTitle("Float Data Visualizer");
//		StageManager.setConnectionCreatorStage(stage);
//		StageManager.createInvisPane(scene, pane);
//		return stage;
//	}
}