package com.alphagen.studio.FloatDataVisualizer.buoyui.backend.lib.jFileSystem;

import com.alphagen.studio.FloatDataVisualizer.buoyui.backend.lib.jFileSystem.exceptions.FileNotCreatedException;
import com.alphagen.studio.FloatDataVisualizer.buoyui.backend.lib.jFileSystem.exceptions.FolderNotCreatedException;
import lombok.Getter;

import java.io.File;
import java.io.IOException;

public class JFileSystem {

	@Getter private static JFileSystem jFileSystem;
	@Getter public final String rootPath;

	public JFileSystem(String rootPath) throws FolderNotCreatedException {
		createFolder(rootPath);
		this.rootPath = rootPath;
		System.out.println("JFileSystem has been successfully created: " + rootPath);
	}

	public Folder createFolder(String folderPath) throws FolderNotCreatedException {
		Folder folder = new Folder(folderPath);
		if (!folder.exists() || !folder.isDirectory()) {
			boolean success = folder.mkdirs();
			if (!success) {
				System.out.println("Unable to create Folder: " + folderPath);
				throw new FolderNotCreatedException(folderPath);
			}
		}
		System.out.println("Folder has been successfully created: " + folderPath);
		return folder;
	}

	public static void createJFileSystem(String rootPath) throws FolderNotCreatedException {
		if (jFileSystem == null) jFileSystem = new JFileSystem(rootPath);
		System.out.println("JFileSystem created");
	}

	public File createFile(String filePath) throws FileNotCreatedException, IOException {
		File file = new File(filePath);
		if (!file.exists() || !file.isDirectory()) {
			boolean success = file.createNewFile();
			if (!success) {
				System.out.println("Unable to create File: " + filePath);
				throw new FileNotCreatedException(filePath);
			}
		}
		System.out.println("File has been successfully created: " + filePath);
		return file;
	}

	// todo
	public boolean folderExists() {
		return false;
	}

	// todo
	public boolean fileExists() {
		return false;
	}

	// todo
	public boolean getFolder() {
		return false;
	}

	// todo
	public boolean getFile() {
		return false;
	}

	// todo
	public boolean deleteFolder() {
		return false;
	}

	// todo
	public boolean deleteFile() {
		return false;
	}

	// todo
	public boolean duplicateFolder() {
		return false;
	}

	// todo
	public boolean duplicateFile() {
		return false;
	}
}