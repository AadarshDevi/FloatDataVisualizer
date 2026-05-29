package com.alphagen.studio.FloatDataVisualizer.buoyui.backend.processor;

import com.alphagen.studio.FloatDataVisualizer.buoyui.backend.data.DataPoint;
import com.alphagen.studio.FloatDataVisualizer.buoyui.frontend.managers.Connections;
import lombok.Getter;

import java.util.Arrays;
import java.util.concurrent.LinkedBlockingQueue;

public class DataPointProcessor implements Runnable {

	@Getter private final LinkedBlockingQueue<DataPoint> parsedArray = new LinkedBlockingQueue<>();
	@Getter private final LinkedBlockingQueue<String> rawArray = new LinkedBlockingQueue<>();
	private final LinkedBlockingQueue<DataPoint> storedArray = new LinkedBlockingQueue<>();

	@Override
	public void run() {

		while (!Thread.currentThread().isInterrupted()) {
			String raw;
			try {
				raw = rawArray.take();
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}

			try {
				System.out.println(" >>> " + raw);

				String[] parsed = raw.split(",");

				String[] array;
				if (parsed.length > 4) {
					array = Arrays.copyOfRange(parsed, 3, parsed.length);
				} else {
					array = new String[]{
						parsed[3]
					};
				}

				DataPoint dp = DataPoint.builder()
					.teamInfo(parsed[0])
					.packetNum(Integer.parseInt(parsed[1].replaceAll(
						Connections.getCurrentConnection()
							.floatConfig().pkt(),
						""
					)))
					.time(Double.parseDouble(parsed[2].trim()))
					.measurements(Arrays.stream(array)
						.mapToDouble(Double::parseDouble)
						.toArray())
					.build();

				parsedArray.add(dp);

			} catch (Exception e) {
				System.err.println(Arrays.toString(e.getStackTrace()));
			}
		}
	}
}