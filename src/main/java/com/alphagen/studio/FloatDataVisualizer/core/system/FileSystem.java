package com.alphagen.studio.FloatDataVisualizer.core.system;


import com.alphagen.studio.FloatDataVisualizer.core.AppInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileSystem {
    private static final Logger logger = LogManager.getLogger(FileSystem.class);

    private final Path rootAppSettingsPath;
    private final Path rootAppDataPath;

    public FileSystem(UserSystem.OperatingSystem os) {

        // setting base file locations
        rootAppDataPath = getRootAppDataPath(os);
        rootAppSettingsPath = getRootAppSettingsPath(os);
    }

    public Path getRootAppSettingsPath(UserSystem.OperatingSystem os) {
        Path path = null;
        // todo: implement win11/10 folder paths
        switch (os) {
            case WINDOWS:
                String settingsFolderPath = System.getProperty("APPDATA");
                if (settingsFolderPath == null) {
                    path = Path.of(System.getProperty("user.home"), "AppData", "Roaming", AppInfo.appName, AppInfo.appVersion);
                    break;
                }
                path = Path.of(settingsFolderPath, AppInfo.appName, AppInfo.appVersion);
                break;
            case MACOS:
                // todo: implement macos folder paths
            case LINUX:
            case MACOS:
            case OTHER:
            default:
        }
        return path;
    }

    public Path getRootAppDataPath(UserSystem.OperatingSystem os) {

    }

    public boolean validFile(String filepath) {
        return validFile(Path.of(filepath));
    }

    public boolean validFile(Path filepath) {
        return Files.isRegularFile(filepath);
    }

    public boolean validFolder(String folderPath) {
        return validFolder(Path.of(folderPath));
    }

    public boolean validFolder(Path folderPath) {
        return Files.isDirectory(folderPath);
    }

    public Path createFile(String filename, String filepath) throws IOException {
        return createFile(filename, Path.of(filepath));
    }

    public Path createFile(String filename, Path filepath) throws IOException {
        Path newFilepath = filepath.resolve(filename);
        if (!validFolder(newFilepath))
            Files.createFile(newFilepath);
        return newFilepath;
    }

    public Path createFolder(String folderName, String folderPath) throws IOException {
        return createFolder(folderName, Path.of(folderPath));
    }

    public Path createFolder(String folderName, Path folderPath) throws IOException {
        Path newFolderPath = folderPath.resolve(folderName);
        if (!validFolder(newFolderPath))
            Files.createDirectories(newFolderPath);
        return newFolderPath;
    }
}
