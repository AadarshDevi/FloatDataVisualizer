package com.alphagen.studio.FloatDataVisualizer.buoyui.lib.fxml;

import com.alphagen.studio.FloatDataVisualizer.buoyui.Controller;
import javafx.scene.layout.Pane;

public record PanePackage<K extends Controller>(Pane pane, K controller) {
}