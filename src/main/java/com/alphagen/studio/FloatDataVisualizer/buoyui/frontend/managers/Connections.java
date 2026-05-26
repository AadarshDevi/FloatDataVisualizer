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

	public boolean delete() {return true;}

	public boolean deleteAll() {

		File connFolder = new File(FolderConstants.CONNECTIONS.toString());
		File[] files = connFolder.listFiles();
		System.out.println("Deleted All Connections: " + files.length);

		for (File file : files) {
			file.delete();
		}

		return true;
	}

	public boolean importing() {return true;}

	public boolean importingAll() {return true;}

	public boolean export() {return true;}

	public boolean exportAll() {return true;}

	public boolean rename() {return true;}

	public boolean edit() {return true;}

//	public boolean
}