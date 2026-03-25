package com.alphagen.studio.FloatDataVisualizer.buoyui.frontend.managers;

import com.alphagen.studio.FloatDataVisualizer.buoyui.backend.util.DeltaDrag;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import lombok.Getter;
import lombok.Setter;

public class StageManager {

	@Getter
	private static Stage mainStage;

	@Getter
	@Setter
	private static Stage connectionCreatorStage;

	public static void setMainStage(Stage stage) {
		mainStage = stage;
		mainStage.initStyle(StageStyle.TRANSPARENT);
	}

	public static void createInvisPane(Scene scene, Pane pane) {

		scene.setFill(Color.TRANSPARENT);

		DeltaDrag drag = new DeltaDrag();
		pane.setOnMousePressed(event -> {
			drag.setDeltaX(event.getSceneX());
			drag.setDeltaY(event.getSceneY());
		});

		// mouse dragged: move the stage
		pane.setOnMouseDragged(event -> {
			mainStage.setX(event.getScreenX() - drag.getDeltaX());
			mainStage.setY(event.getScreenY() - drag.getDeltaY());
		});
	}
}