package com.alphagen.studio.FloatDataVisualizer.core.system;

import com.jthemedetecor.OsThemeDetector;
import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class UserSystem {
    private static final Logger LOGGER = LogManager.getLogger(UserSystem.class);
    @Getter
    private final FileSystem fileSystem;
    @Getter
    @Setter
    private final SystemTheme theme;
    @Getter
    private final OperatingSystem os;

    public UserSystem() {
        this.os = detectOS();
        fileSystem = new FileSystem(os);
        this.theme = detectTheme();
    }

    public OperatingSystem detectOS() {
        String osName = java.lang.System.getProperty("os.name").toLowerCase();
        OperatingSystem os = OperatingSystem.OTHER;
        if (osName.contains("win")) {
            os = OperatingSystem.WINDOWS;
        } else if (osName.contains("mac")) {
            os = OperatingSystem.MACOS;
        } else if (osName.contains("nux")) {
            os = OperatingSystem.LINUX;
        }

        LOGGER.info("Operating System: {}", os);
        return os;
    }

    public SystemTheme detectTheme() {
        OsThemeDetector detector = OsThemeDetector.getDetector();
        SystemTheme theme = SystemTheme.OTHER;

        boolean isDarkTheme = detector.isDark();

        if (isDarkTheme) {
            theme = SystemTheme.DARK;
        } else {
            theme = SystemTheme.LIGHT;
        }

        LOGGER.info("System Theme: {}", theme);
        return theme;
    }

    enum OperatingSystem {
        WINDOWS,
        MACOS,
        LINUX,
        OTHER
    }

    enum SystemTheme {
        LIGHT,
        DARK,
        OTHER
    }

    record SystemFile(
            OperatingSystem os,
            SystemTheme theme
    ) {

    }

    class FileSystem {
        private final Path rootAppSettingsPath;
        private final Path rootAppDataPath;

        public FileSystem(OperatingSystem os) {

            // getting os
            this.os = os;

            // setting base file locations
            rootAppDataPath = getRootAppDataPath(os);
            rootAppSettingsPath = getRootAppSettingsPath(os);
        }

        public Path getRootAppSettingsPath() {
            Path path = null;
            // todo: implement win11/10 folder paths
            switch (this.os) {
                case WINDOWS:
                    String settingsFolderPath = System.getProperty("APPDATA");
                    if (settingsFolderPath == null) {
                        path = Path.of(System.getProperty("user.home"), "AppData", "FloatDataVisualizer", "3.0.0");
                        break;
                    }
                    path = Path.of(settingsFolderPath, "FloatDataVisualizer", "3.0.0");
                case LINUX:
                case MACOS:
                case OTHER:
                default:
            }
            return path;
        }

        public Path getRootAppDataPath() {

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

        record FilePath(Path settingsFilePath, Path databaseFilePath) {
        }
    }
}
