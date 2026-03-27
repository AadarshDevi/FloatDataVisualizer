package com.alphagen.studio.FloatDataVisualizer.buoyui.frontend.managers;

import com.alphagen.studio.FloatDataVisualizer.buoyui.backend.data.ConnectionConfig;
import lombok.Getter;
import lombok.Setter;

public class ConnectionManager {

	@Setter
	@Getter
	private static ConnectionConfig currentConnection;
}