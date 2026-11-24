package com.alphagen.studio.FloatDataVisualizer.core.system;

import com.alphagen.studio.FloatDataVisualizer.core.data.OperatingSystem;
import com.alphagen.studio.FloatDataVisualizer.core.data.Version;
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
        this.theme = detectTheme(SystemTheme.DARK); // fixme: need to read from settings file
    }

    public static UserSystem getInstance() {
        if (userSystem == null) userSystem = new UserSystem();
        return userSystem;
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

    public SystemTheme detectTheme(SystemTheme userTheme) {
        OsThemeDetector detector = OsThemeDetector.getDetector();
        SystemTheme theme = SystemTheme.SYSTEM;

        boolean isDarkTheme = detector.isDark();

        if (isDarkTheme) {
            theme = SystemTheme.DARK;
        } else {
            theme = SystemTheme.LIGHT;
        }

        LOGGER.info("System Theme: {}", theme);
        return theme;
    }

    enum SystemTheme {
        LIGHT,
        DARK,
        OTHER,
        SYSTEM
    }

    record SystemFile(
            OperatingSystem os,
            SystemTheme theme,
            Version appVersion
    ) {
    }


}
