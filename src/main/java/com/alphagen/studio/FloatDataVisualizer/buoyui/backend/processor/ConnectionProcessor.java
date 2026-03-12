package com.alphagen.studio.FloatDataVisualizer.buoyui.backend.processor;

import com.alphagen.studio.FloatDataVisualizer.buoyui.backend.app.AppData;
import com.alphagen.studio.FloatDataVisualizer.buoyui.backend.app.Platform;
import com.alphagen.studio.FloatDataVisualizer.buoyui.backend.app.PlatformDetector;
import com.alphagen.studio.FloatDataVisualizer.buoyui.backend.constants.FolderConstants;
import com.alphagen.studio.FloatDataVisualizer.buoyui.backend.data.ConnectionConfig;
import com.alphagen.studio.FloatDataVisualizer.buoyui.backend.data.ConnectionType;
import com.alphagen.studio.FloatDataVisualizer.buoyui.backend.data.FloatConfig;
import com.fazecast.jSerialComm.SerialPort;

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

	// todo: 	update connections: data format, start/end flags, unit measured
	// 		 	update related stuff

	public static boolean writeConnection(ConnectionConfig connectionConfig) {
		Path connectionPath = FolderConstants.CONNECTIONS.resolve(connectionConfig.connectionName() + FolderConstants.FLOAT_CONNECTION_FILE_EXTENSION);
		try {
			Files.createFile(connectionPath);
		} catch (IOException e) {
			System.err.println("Unable to create file: " + connectionPath);
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
			pw.println("connection_os=" + PlatformDetector.getOSPLATFORM());

		} catch (FileNotFoundException e) {
			System.err.println("Unable to write file: " + connectionPath);
			return false;
		}
		return true;
	}

	// fixme read all
	public static ArrayList<ConnectionConfig> readAllConnections() {
		Path connectionFolderPath = FolderConstants.CONNECTIONS;
		ArrayList<ConnectionConfig> connectionConfigs = new ArrayList<>();

		File folder = new File(connectionFolderPath.toString());
		if (!folder.exists()) {
			System.err.println("Folder does not exist");
			return null;
		}

		if (!folder.isDirectory()) {
			System.err.println("Folder is not a directory");
			return null;
		}

		if (Objects.requireNonNull(folder.listFiles()).length == 0) {
			System.err.println("Folder is empty");
			return null;
		}
		System.out.println("Connections Folder ready for reading");

		File[] files = folder.listFiles();
		assert files != null;
		for (File file : files) {
			try (FileInputStream br = new FileInputStream(file)) {

				Properties properties = new Properties();
				properties.load(br);
				
				ConnectionConfig connectionConfig = readConnectionConfig(properties);

				if (connectionConfig != null)
					connectionConfigs.add(connectionConfig);
			} catch (IOException e) {
				System.err.println("Error reading properties file: " + file.getAbsolutePath());
			}

		}
		return connectionConfigs;
	}

	public static ConnectionConfig readConnectionConfig(Properties properties) {
		try {
			Platform platform = Platform.valueOf(properties.getProperty("connection_os"));

			if (!platform.equals(PlatformDetector.getOSPLATFORM())) {
				System.err.println("Cannot Use Connection: Incompatible OS > Platform (" + PlatformDetector.getOSPLATFORM() + ") != Connection (" + platform + ")");
				return null;
			}

			String connectionVersion = properties.getProperty("connection_version");
			if (!connectionVersion.equals(AppData.CONNECTION_VERSION)) {
				System.err.println("Cannot Use Connection: Incompatible Connection Version > Platform (" + AppData.CONNECTION_VERSION + ") != Connection (" + connectionVersion + ")");
				return null;
			}
			return parseConnectionConfig(properties);

		} catch (NullPointerException e) {
			return null;
		}
	}

	private static ConnectionConfig parseConnectionConfig(Properties properties) {
		return ConnectionConfig.builder()
				.connectionName(properties.getProperty("connection_name"))
				.baudRate(Integer.parseInt(properties.getProperty("connection_baud_rate")))
				.port(SerialPort.getCommPort(properties.getProperty("connection_port")))
				.portType(ConnectionType.valueOf(properties.getProperty("connection_port_type")))
				.floatConfig(parseFloatConfig(properties))
				.build();
	}

	private static FloatConfig parseFloatConfig(Properties properties) {
		return FloatConfig.builder()
				.teamData(properties.getProperty("team_info"))
				.startFlag(properties.getProperty("start_data_transfer"))
				.endFlag(properties.getProperty("end_data_transfer"))
				.measurement(properties.getProperty("measurement"))
				.measurementUnit(properties.getProperty("measurement_unit"))
				.build();
	}
}