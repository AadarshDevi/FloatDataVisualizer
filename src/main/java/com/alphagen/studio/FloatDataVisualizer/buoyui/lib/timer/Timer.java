package com.alphagen.studio.FloatDataVisualizer.buoyui.lib.timer;

public class Timer {

	private long startTime;
	private long duration;

	public Timer() {}

	public void start() {
		startTime = System.nanoTime();
	}

	public void stop() {
		duration = System.nanoTime() - startTime;
	}

	public void printSeconds() {
		System.out.println("Duration: " + getTimeInSeconds() + " s");
	}

	public long getTimeInSeconds() {
		return duration / 1_000_000_000L;
	}

	public void printMilliSeconds() {
		System.out.println("Duration: " + getTimeInMilliSeconds() + " ms");
	}

	public long getTimeInMilliSeconds() {
		return duration / 1_000_000L;
	}
}