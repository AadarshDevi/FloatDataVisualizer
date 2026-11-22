package com.alphagen.studio.FloatDataVisualizer.core.data;

import lombok.Builder;
import lombok.Singular;

import java.util.List;

@Builder
public record ConnectionConfig(
        String name,
        OperatingSystem os,
        Version version,
        ConnectionType type,
        String port,
        int baudRate,
        String startFlag,
        String endFlag,
        TeamInfo teamInfo,
        @Singular List<SensorUnit> sensors,
        @Singular List<Integer> commands
) {
}
