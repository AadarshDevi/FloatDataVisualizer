package com.alphagen.studio.FloatDataVisualizer.buoyui.lib.fxml;

import com.alphagen.studio.FloatDataVisualizer.buoyui.Controller;
import javafx.scene.Node;
import lombok.Builder;

@Builder
public record NodePackage<K extends Controller>(Node node, K controller) {
}