package com.alphagen.studio.FloatDataVisualizer.buoyui.frontend.managers;

import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;

public class StageManager {

	@Getter
	@Setter
	private static Stage mainStage;

	@Getter
	@Setter
	private static Stage connectionCreatorStage;
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