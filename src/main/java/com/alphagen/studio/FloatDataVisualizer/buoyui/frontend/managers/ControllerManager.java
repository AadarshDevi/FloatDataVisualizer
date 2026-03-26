package com.alphagen.studio.FloatDataVisualizer.buoyui.frontend.managers;

import com.alphagen.studio.FloatDataVisualizer.buoyui.frontend.pages.connections.ConnectionsController;
import com.alphagen.studio.FloatDataVisualizer.buoyui.frontend.pages.connections.editor.ConnectionEditorController;
import lombok.Getter;
import lombok.Setter;

public class ControllerManager {

	@Getter
	@Setter
	private static ConnectionsController connectionsController;

	@Getter
	@Setter
	private static ConnectionEditorController connectionEditorController;
}