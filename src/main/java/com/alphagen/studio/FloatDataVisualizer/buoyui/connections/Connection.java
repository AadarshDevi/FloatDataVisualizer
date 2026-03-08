package com.alphagen.studio.FloatDataVisualizer.buoyui.connections;

import lombok.Data;

@Data
public class Connection {

	private String name;
	private int baudRate;
	private String port;
	private ConnectionType connectionType;

	public Connection(String connectionName, int baudRate, String port, ConnectionType portType) {
		this.name = connectionName;
		this.baudRate = baudRate;
		this.port = port;
		this.connectionType = portType;
	}
}