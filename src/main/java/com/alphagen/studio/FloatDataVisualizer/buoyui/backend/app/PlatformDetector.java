package com.alphagen.studio.FloatDataVisualizer.buoyui.backend.app;

import lombok.Getter;

public class PlatformDetector {
    @Getter
    private static Platform OSPLATFORM;

    public PlatformDetector() {

        String osName = System.getProperty("os.name");
        if (osName.toLowerCase().contains("win"))
            OSPLATFORM = Platform.WIN11;
        else if (osName.toLowerCase().contains("mac") || osName.toLowerCase().contains("darwin")) {
            OSPLATFORM = Platform.MACOS;
        } else if (osName.toLowerCase().contains("linux")) {
            OSPLATFORM = Platform.LINUX;
        } else {
            OSPLATFORM = Platform.NULL;
        }
    }

    public void detectPlatform() {
    }
}
