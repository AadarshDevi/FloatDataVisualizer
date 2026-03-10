package com.alphagen.studio.FloatDataVisualizer.buoyui.backend;

import com.alphagen.studio.FloatDataVisualizer.buoyui.backend.app.AppData;
import com.alphagen.studio.FloatDataVisualizer.buoyui.backend.app.PlatformDetector;
import lombok.Getter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public class Backend {

    @Getter private static Path PROJECTROOT;

    public static Backend backend;

    private Backend() throws IOException {
        PROJECTROOT = PlatformDetector.getPROJECTROOT();
        Files.createDirectories(PROJECTROOT);
    }

    public static Backend getBackend() throws IOException {
        if (backend == null) backend = new Backend();
        return backend;
    }

    public boolean verifyFolder(String folder) throws IOException {
        if (!Files.isDirectory(PROJECTROOT.resolve(folder)))
            Files.createDirectories(PROJECTROOT.resolve(folder));
        return true;
    }

    public boolean verifyFile(String file) throws IOException {
        if (!Files.isRegularFile(PROJECTROOT.resolve(file)))
            Files.createFile(PROJECTROOT.resolve(file));
        return true;
    }

    public boolean verifyRoot() {
        return Files.isDirectory(PROJECTROOT);
    }

//	public ArrayList<ConnectionConfig> getConnections() {
//		try {
//			Folder connectionsfolder = rootSystem.getFolder(FolderConstants.CONNECTIONS);
//			System.out.println("Getting Connections Folder: " + connectionsfolder.getPath());
//
//			String[] files = connectionsfolder.list();
//			if (files == null || files.length == 0) return null;
//			System.out.println("Folder Content Count: " + files.length);
//
//			ArrayList<ConnectionConfig> connections = ConnectionProcessor.readAll(files);
//
//		} catch (FolderNotFoundException e) {
//			System.out.println("Connections Folder not found");
//		}
//
//		return null;
//	}
}