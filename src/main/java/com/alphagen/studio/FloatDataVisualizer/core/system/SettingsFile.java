package com.alphagen.studio.FloatDataVisualizer.core.system;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

public class SettingsFile {
    private static final Logger LOGGER = LogManager.getLogger(SettingsFile.class);

    // private fields ...

    public static boolean write(Path settingsFilepath, SettingsRecord settingsRecord) {
        try (BufferedWriter bw = new BufferedWriter(
                new FileWriter(settingsFilepath.toString())
        )) {
            bw.write(String.valueOf(settingsRecord));
        } catch (IOException e) {
            LOGGER.error(e);
            return false;
        }
        return true;
    }

    record SettingsRecord(
            String appVersion,
            String connectionVersion
    ) {

    }
}
