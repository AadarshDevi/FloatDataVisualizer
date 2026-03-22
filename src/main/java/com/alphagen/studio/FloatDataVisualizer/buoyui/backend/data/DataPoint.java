package com.alphagen.studio.FloatDataVisualizer.buoyui.backend.data;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public record DataPoint(String teamInfo, int packetNum, double time, double[] measurements) {
}