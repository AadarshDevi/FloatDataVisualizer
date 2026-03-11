package com.alphagen.studio.FloatDataVisualizer;

import com.alphagen.studio.FloatDataVisualizer.buoyui.BuoyUI;
import javafx.application.Application;

public class Launcher {

	public static void main(String[] args) {

		System.out.println("Starting FloatDataVisualizer");
		Application.launch(BuoyUI.class, args);
	}

}