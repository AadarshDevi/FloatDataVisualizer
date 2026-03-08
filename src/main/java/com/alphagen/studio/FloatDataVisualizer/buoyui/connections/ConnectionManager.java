package com.alphagen.studio.FloatDataVisualizer.buoyui.connections;

import lombok.Getter;
import lombok.Setter;

public class ConnectionManager {

	@Setter
	@Getter
	private static Connection currentConnection;

	public ConnectionManager() {
		currentConnection = null;
	}

	public ConnectionManager(String connectionName, int baudRate, String port, ConnectionType portType) {
		createConnection(connectionName, baudRate, port, portType);
	}

	public void createConnection(String connectionName, int baudRate, String port, ConnectionType portType) {
		currentConnection = new Connection(connectionName, baudRate, port, portType);
	}

	public ConnectionManager(Connection connection) {
		createConnection(connection);
	}

	public void createConnection(Connection connection) {
		currentConnection = connection;
	}
}