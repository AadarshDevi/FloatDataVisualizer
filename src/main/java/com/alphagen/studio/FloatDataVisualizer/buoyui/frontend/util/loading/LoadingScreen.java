package com.alphagen.studio.FloatDataVisualizer.buoyui.frontend.util.loading;

import com.alphagen.studio.FloatDataVisualizer.buoyui.backend.util.DeltaDrag;
import com.alphagen.studio.FloatDataVisualizer.buoyui.frontend.pages.PageConstants;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class LoadingScreen implements Runnable {

	private final Stage stage;

	public LoadingScreen() {
		stage = new Stage();
		setup();
	}

	public void setup() {
		FXMLLoader loader = new FXMLLoader(PageConstants.LOADING_SCREEN);
		BorderPane loadingScreen = null;
		try {
			loadingScreen = loader.load();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		LoadingScreenController lsc = loader.getController();

		Scene scene = new Scene(loadingScreen);
		scene.setFill(Color.TRANSPARENT);

		final DeltaDrag progressDrag = new DeltaDrag();

		loadingScreen.setOnMousePressed(event -> {
			progressDrag.setDeltaX(event.getSceneX());
			progressDrag.setDeltaY(event.getSceneY());
		});

		// mouse dragged: move the stage
		loadingScreen.setOnMouseDragged(event -> {
			stage.setX(event.getScreenX() - progressDrag.getDeltaX());
			stage.setY(event.getScreenY() - progressDrag.getDeltaY());
		});

		stage.setScene(scene);
		stage.initStyle(StageStyle.TRANSPARENT);
	}

	@Override
	public void run() {
		stage.show();

		while (!Thread.currentThread().isInterrupted()) {
		}
		stage.close();
	}
}