package com.alphagen.studio.FloatDataVisualizer.buoyui.frontend.managers;

import com.alphagen.studio.FloatDataVisualizer.buoyui.Controller;
import com.alphagen.studio.FloatDataVisualizer.buoyui.backend.data.ConnectionConfig;
import com.alphagen.studio.FloatDataVisualizer.buoyui.frontend.pages.CardConstants;
import com.alphagen.studio.FloatDataVisualizer.buoyui.frontend.pages.connections.datacard.DataCardController;
import com.alphagen.studio.FloatDataVisualizer.buoyui.lib.fxml.NodePackage;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;

import java.io.IOException;

public class DataCardManager {
	public static <K extends Controller> NodePackage<K> create(ConnectionConfig connectionConfig) {
		FXMLLoader fxmlLoader = new FXMLLoader(CardConstants.CONNECTION_DATA_CARD);
		Button dataCard = null;  // Explicit type arg helps inference
		try {
			dataCard = fxmlLoader.load();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		DataCardController dcc = fxmlLoader.getController();
		dcc.setConnection(connectionConfig);

		// Cast only if DataCardController isn't K-compatible
		return NodePackage.<K>builder()
				.node(dataCard)
				.controller((K) dcc)  // Safe if hierarchy correct
				.build();
	}

}