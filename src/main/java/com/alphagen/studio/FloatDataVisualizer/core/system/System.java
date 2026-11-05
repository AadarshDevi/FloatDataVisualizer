package com.alphagen.studio.FloatDataVisualizer.core.system;

import com.jthemedetecor.OsThemeDetector;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class System {
    private static final Logger LOGGER = LogManager.getLogger(System.class);
    private static final boolean systemThemeOverride = false;


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
        private final OperatingSystem os;

        public FileSystem(OperatingSystem os) {
            this.os = os;
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

        public File createFile(String filename, Path filepath) throws IOException {
            Path path = Files.createFile(Path.of(String.valueOf(filepath), filename));
            return null;
        }

        record FilePath(Path settingsFilePath, Path databaseFilePath) {
        }
    }
}
