package com.alphagen.studio.FloatDataVisualizer.buoyui.frontend.util;

import com.alphagen.studio.FloatDataVisualizer.buoyui.backend.util.DeltaDrag;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class StageUtil {

	public static void createInvisPane(Stage stage, Scene scene, Pane pane) {

		scene.setFill(Color.TRANSPARENT);

		stage.initStyle(StageStyle.TRANSPARENT);

		DeltaDrag drag = new DeltaDrag();
		pane.setOnMousePressed(event -> {
			drag.setDeltaX(event.getSceneX());
			drag.setDeltaY(event.getSceneY());
		});

		// mouse dragged: move the stage
		pane.setOnMouseDragged(event -> {
			stage.setX(event.getScreenX() - drag.getDeltaX());
			stage.setY(event.getScreenY() - drag.getDeltaY());
		});
	}

	public static void exceptionAlert(boolean success, int exceptionNum, String errorMessage, String positiveLog) {
		// code
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
}