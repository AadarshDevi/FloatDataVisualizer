package com.alphagen.studio.FloatDataVisualizer.core.data;

import lombok.Singular;

import java.util.List;

public record ConnectionConfig(
        int id,
        String name,
        OperatingSystem os,
        Version version,
        ConnectionType type,
        String serialPort,
        int baudRate,
        String startFlag,
        String endFlag,
        TeamInfo teamInfo,
        @Singular List<SensorUnit> sensorData,
        @Singular List<Integer> commandList
) {
}
