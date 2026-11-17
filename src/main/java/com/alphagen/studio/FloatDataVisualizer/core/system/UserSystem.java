package com.alphagen.studio.FloatDataVisualizer.core.system;

import com.jthemedetecor.OsThemeDetector;
import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UserSystem {
    private static final Logger LOGGER = LogManager.getLogger(UserSystem.class);
    @Getter
    private final FileSystem fileSystem;
    @Getter
    private final OperatingSystem os;
    @Getter
    @Setter
    private SystemTheme theme;

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

    public enum OperatingSystem {
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


}
