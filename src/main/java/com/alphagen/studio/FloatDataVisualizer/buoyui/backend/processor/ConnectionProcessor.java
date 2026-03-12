package com.alphagen.studio.FloatDataVisualizer.buoyui.backend.processor;

import com.alphagen.studio.FloatDataVisualizer.buoyui.backend.app.AppData;
import com.alphagen.studio.FloatDataVisualizer.buoyui.backend.constants.FolderConstants;
import com.alphagen.studio.FloatDataVisualizer.buoyui.backend.data.ConnectionConfig;
import com.alphagen.studio.FloatDataVisualizer.buoyui.backend.data.FloatConfig;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Properties;

/**
 * Reads and Writes Connection files ({connection_name}.float.connection)
 */
public class ConnectionProcessor {
//	public static ArrayList<ConnectionConfig> readAll(String[] files) {
//		ArrayList<ConnectionConfig> connections = new ArrayList<>();
//		Properties prop = new Properties();
//		ConnectionConfig connection = new ConnectionConfig(prop.get("name").toString(), Integer.parseInt(prop.get("baudRate").toString()), (SerialPort) prop.get("port"), ConnectionType.valueOf(prop.get("connectionType").toString()), new FloatConfig());
//		connections.add(connection);
//		return connections;
//	}

	// todo: 	update connections: data format, start/end flags, unit measured
	// 		 	update related stuff
	public static boolean writeConnection(ConnectionConfig connectionConfig) {
		Path connectionPath = FolderConstants.CONNECTIONS.resolve(connectionConfig.connectionName() + ".float.connection");
		try {
			Files.createFile(connectionPath);
		} catch (IOException e) {
			System.out.println("Unable to create file: " + connectionPath);
			return false;
		}
		File file = new File(connectionPath.toString());
		try (PrintWriter pw = new PrintWriter(file)) {
			pw.println("# Connection Config");
			pw.println("connection_name=" + connectionConfig.connectionName());
			pw.println("connection_baud_rate=" + connectionConfig.baudRate());
			pw.println("connection_port=" + connectionConfig.port().getSystemPortName());
			pw.println("connection_port_type=" + connectionConfig.portType());
			pw.println("");

			FloatConfig floatConfig = connectionConfig.floatConfig();
			pw.println("# Float Config");
			pw.println("team_info=" + floatConfig.teamData());
			pw.println("start_data_transfer=" + floatConfig.startFlag());
			pw.println("end_data_transfer=" + floatConfig.endFlag());
			pw.println("");

			pw.println("# Measurement Config");
			pw.println("measurement_name=" + floatConfig.measurement());
			pw.println("measurement_unit=" + floatConfig.measurementUnit());
			pw.println("");

			pw.println("# Save Config");
			pw.println("connection_version=" + AppData.CONNECTION_VERSION);

		} catch (FileNotFoundException e) {
			System.out.println("Unable to write file: " + connectionPath);
			return false;
		}
		return true;
	}
}