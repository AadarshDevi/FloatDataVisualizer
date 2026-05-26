package com.alphagen.studio.FloatDataVisualizer.buoyui.frontend.managers;

import com.alphagen.studio.FloatDataVisualizer.buoyui.backend.constants.FolderConstants;
import com.alphagen.studio.FloatDataVisualizer.buoyui.backend.data.ConnectionConfig;
import lombok.Getter;
import lombok.Setter;

import java.io.File;

public class Connections {

	@Setter
	@Getter
	private static ConnectionConfig currentConnection;

	private static Connections connections;

	public static Connections getInstance() {
		if (connections == null)
			connections = new Connections();
		return connections;
	}

	public boolean create() {return true;}

	public boolean delete(String connectionName) {
		Path connectionFile = FolderConstants.CONNECTIONS.resolve(connectionName + FolderConstants.FLOAT_CONNECTION_FILE_EXTENSION);
		try {
			Files.deleteIfExists(connectionFile);
			return true;
		} catch (IOException e) {
			System.err.printf("%s > Unable to delete connection: %s%n", e.getMessage(), connectionName);
			return false;
		}
	}

	public boolean deleteAll() {
		Path connectionsFolderPath = Paths.get(FolderConstants.CONNECTIONS.toString());
		try (Stream<Path> paths = Files.list(connectionsFolderPath)) {
			AtomicInteger fileCount = new AtomicInteger();
			paths.forEach(path -> {
				boolean _ = path.toFile().delete();
				fileCount.getAndIncrement();
			});
			System.out.println("Connections Deleted: " + fileCount.get());
			return true;
		} catch (IOException e) {
			System.out.println("Unable to delete Connections");
			return false;
		}
	}

	public boolean importing() {return true;}

	public boolean importingAll() {return true;}

	public boolean export() {return true;}

	public boolean exportAll() {return true;}

	public boolean rename() {return true;}

	public boolean edit() {return true;}

//	public boolean
}