package com.alphagen.studio.FloatDataVisualizer.floatui;


import javafx.scene.text.Font;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;

public class FontManager {

    private static final Logger logger = LogManager.getLogger(FontManager.class);
    private static final String FONTS_ROOT_PATH = "/com/alphagen/studio/FloatDataVisualizer/floatui/fonts";
    public static final String CASCADIA_MON_MEDIUM = FONTS_ROOT_PATH + "/cascadia_mono/CascadiaMono-Medium.ttf";

    public static Font getFont(String fontPath, int fontSize) {
        Font font;
        try (InputStream fontStream = FontManager.class.getResourceAsStream(fontPath)) {
            font = (fontStream != null) ? Font.loadFont(fontStream, fontSize) : null;
        } catch (IOException e) {
            font = null;
        }

        if (font == null) {
            logger.warn("Loading Monospace Font.");
            font = Font.font("Monospace", fontSize);
        }
        return font;
    }

    public static class Library {
        public static final String CASCADIA_MON_MEDIUM = FONTS_ROOT_PATH + "/cascadia_mono/CascadiaMono-Medium.ttf";
    }
}
