package com.alphagen.studio.FloatDataVisualizer.buoyui.frontend.managers;

import com.alphagen.studio.FloatDataVisualizer.buoyui.frontend.pages.connections.ConnectionsController;
import lombok.Getter;
import lombok.Setter;

public class ControllerManager {

	@Setter
	@Getter
	private static ConnectionsController connectionsController;
}