package com.alphagen.studio.FloatDataVisualizer.buoyui.backend.data;

import lombok.Builder;

@Builder
public record DataPoint(String teamInfo, int packetNum, double time, double[] measurements) {
	public String toRaw() {
		StringBuilder sb = new StringBuilder().append(teamInfo).append(",").append(packetNum).append(",").append(time).append(",");
		for (int i = 0; i < measurements.length; i++) {
			sb.append(measurements[i]);
			if (i != measurements.length - 1) {
				sb.append(",");
			}
		}
		return sb.toString();
	}
}