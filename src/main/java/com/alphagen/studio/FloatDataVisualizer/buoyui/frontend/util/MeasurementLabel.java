package com.alphagen.studio.FloatDataVisualizer.buoyui.frontend.util;

import com.alphagen.studio.FloatDataVisualizer.buoyui.frontend.pages.PageConstants;
import javafx.geometry.Pos;
import javafx.scene.control.Label;

public class MeasurementLabel extends Label {
	public MeasurementLabel(String text) {
		super(text);
		setAlignment(Pos.CENTER);
		getStylesheets().add(PageConstants.CSS_LIGHT_THEME.toString());
		setPrefSize(140.0, 30);
		getStyleClass().addAll("labels", "measurement-labels");
	}
}