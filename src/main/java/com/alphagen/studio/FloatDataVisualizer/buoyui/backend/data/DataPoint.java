package com.alphagen.studio.FloatDataVisualizer.buoyui.backend.data;

import lombok.Builder;

@Builder
public record DataPoint(String teamInfo, int packetNum, double time, double[] measurements) {
}