package com.alphagen.studio.FloatDataVisualizer.buoyui.backend.processor;

import com.alphagen.studio.FloatDataVisualizer.buoyui.backend.app.AppData;
import com.alphagen.studio.FloatDataVisualizer.buoyui.backend.app.Platform;
import com.alphagen.studio.FloatDataVisualizer.buoyui.backend.app.PlatformDetector;
import com.alphagen.studio.FloatDataVisualizer.buoyui.backend.constants.FolderConstants;
import com.alphagen.studio.FloatDataVisualizer.buoyui.backend.data.ConnectionConfig;
import com.alphagen.studio.FloatDataVisualizer.buoyui.backend.data.ConnectionType;
import com.alphagen.studio.FloatDataVisualizer.buoyui.backend.data.FloatConfig;
import com.alphagen.studio.FloatDataVisualizer.buoyui.backend.data.MeasurementConfig;
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

	public static boolean writeConnection(Path connectionPath, ConnectionConfig connectionConfig) {
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
			pw.println("packet=" + floatConfig.pkt());
			pw.println("start_data_transfer=" + floatConfig.startFlag());
			pw.println("end_data_transfer=" + floatConfig.endFlag());
			pw.println("");

			pw.println("# Measurement Configs");

			MeasurementConfig[] measurementConfigs = connectionConfig.measurementConfigs();
			for (int i = 0; i < measurementConfigs.length; i++) {
				MeasurementConfig measurementConfig = measurementConfigs[i];
				pw.println(String.format("measurement_name_%d=%s", i, measurementConfig.name()));
				pw.println(String.format("measurement_unit_%d=%s", i, measurementConfig.unit()));
				pw.println("");
			}

			pw.println("# Save Config");
			pw.println("connection_version=" + AppData.CONNECTION_VERSION);
			pw.println("connection_os=" + PlatformDetector.getOSPLATFORM());

		} catch (FileNotFoundException e) {
			System.err.println("Unable to write file: " + connectionPath);
			return false;
		}
		return true;
	}

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
		if (files == null) return null;
		System.out.println("Files found: " + files.length);

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
			.measurementConfigs(parseMeasurementConfigs(properties))
			.build();
	}

	private static FloatConfig parseFloatConfig(Properties properties) {
		return FloatConfig.builder()
			.teamData(properties.getProperty("team_info"))
			.pkt(properties.getProperty("packet"))
			.startFlag(properties.getProperty("start_data_transfer"))
			.endFlag(properties.getProperty("end_data_transfer"))
			.build();
	}

	private static MeasurementConfig[] parseMeasurementConfigs(Properties properties) {
		ArrayList<MeasurementConfig> measurementConfigs = new ArrayList<>();
		boolean parsing = true;
		int i = 0;

		while (parsing) {
			String name = properties.getProperty("measurement_name_" + i);
			String unit = properties.getProperty("measurement_unit_" + i);
			if (name != null && unit != null) {
				measurementConfigs.add(new MeasurementConfig(name, unit));
			} else
				parsing = false;
			i++;
		}

		return measurementConfigs.toArray(new MeasurementConfig[0]);
	}

	public static boolean renameConnection(ConnectionConfig oldConfig, ConnectionConfig newConfig) {

		// get old connection filepath
		Path source = getConnectionPath(oldConfig);

		if (source == null) return false;


		// create new connection filepath
		Path target = FolderConstants.CONNECTIONS
			.resolve(newConfig.connectionName() + FolderConstants.FLOAT_CONNECTION_FILE_EXTENSION);

		if (Files.exists(target)) {
			return true;
		}

		// copy file
		try {
			Files.move(source, target);
		} catch (IOException e) {
			return false;
		}

		// delete old connection file
		try {
			Files.delete(source);
		} catch (IOException e) {
			return false;
		}

		return true;
	}

	public static Path getConnectionPath(ConnectionConfig connectionConfig) {
		Path filePath = FolderConstants.CONNECTIONS.resolve(connectionConfig.connectionName() + FolderConstants.FLOAT_CONNECTION_FILE_EXTENSION);

		if (!Files.exists(filePath)) {
			return null;
		}

		return filePath;

	}
}