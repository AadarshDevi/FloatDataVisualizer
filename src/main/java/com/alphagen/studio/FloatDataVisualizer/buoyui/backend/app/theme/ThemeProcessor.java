package com.alphagen.studio.FloatDataVisualizer.buoyui.backend.app.theme;

import lombok.Getter;
import lombok.Setter;

import java.net.URL;

public class ThemeProcessor {

	private static final String cssThemePath = "/com/alphagen/studio/FloatDataVisualizer/buoyui";
	@Getter
	@Setter
	private static Theme theme = Theme.LIGHT;

	public static URL getThemeCSS() {

		URL url;

		url = switch (theme) {
			case DARK -> ThemeProcessor.class.getResource(cssThemePath + "/theme_dark.css");
			case LIGHT -> ThemeProcessor.class.getResource(cssThemePath + "/theme_light.css");
		};

		if (url == null) {
			throw new RuntimeException("Theme \"" + theme + "\" not found.");
		}

		return url;
	}
}