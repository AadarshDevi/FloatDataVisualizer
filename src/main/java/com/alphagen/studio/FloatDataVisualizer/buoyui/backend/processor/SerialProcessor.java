package com.alphagen.studio.FloatDataVisualizer.buoyui.backend.processor;

import com.alphagen.studio.FloatDataVisualizer.buoyui.backend.data.ConnectionConfig;
import lombok.Setter;

public class SerialProcessor implements Runnable {
	@Setter private final ConnectionConfig connectionConfig;

	public SerialProcessor(ConnectionConfig connectionConfig) {
		this.connectionConfig = connectionConfig;
	}

	@Override
	public void run() {

	}
}