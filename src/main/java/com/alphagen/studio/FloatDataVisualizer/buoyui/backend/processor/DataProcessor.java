package com.alphagen.studio.FloatDataVisualizer.buoyui.backend.processor;

import com.alphagen.studio.FloatDataVisualizer.buoyui.backend.data.DataPoint;

import java.util.concurrent.LinkedBlockingQueue;

public class DataProcessor implements Runnable {

	LinkedBlockingQueue<DataPoint> parsedArray = new LinkedBlockingQueue<>();
	LinkedBlockingQueue<DataPoint> rawArray = new LinkedBlockingQueue<>();

	// todo: add threading

	@Override
	public void run() {

	}
}