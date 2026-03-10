package com.alphagen.studio.FloatDataVisualizer.buoyui.frontend.managers;

import com.alphagen.studio.FloatDataVisualizer.buoyui.backend.data.ConnectionConfig;
import com.alphagen.studio.FloatDataVisualizer.buoyui.backend.data.ConnectionType;
import com.alphagen.studio.FloatDataVisualizer.buoyui.backend.data.FloatConfig;
import com.fazecast.jSerialComm.SerialPort;
import lombok.Getter;
import lombok.Setter;

public class ConnectionManager {

	@Setter
	@Getter
	private static ConnectionConfig currentConnection;

	public ConnectionManager() {
		currentConnection = null;
	}

	public ConnectionManager(String connectionName, int baudRate, SerialPort port, ConnectionType portType, FloatConfig floatConfig) {
		createConnection(connectionName, baudRate, port, portType, floatConfig);
	}

	public void createConnection(String connectionName, int baudRate, SerialPort port, ConnectionType portType, FloatConfig floatConfig) {
		currentConnection = new ConnectionConfig(connectionName, baudRate, port, portType, floatConfig);
	}

	public ConnectionManager(ConnectionConfig connection) {
		createConnection(connection);
	}

	public void createConnection(ConnectionConfig connection) {
		currentConnection = connection;
	}
}