package com.alphagen.studio.FloatDataVisualizer.core.data;

import lombok.Builder;
import lombok.Singular;

import java.util.List;

/**
 *
 * A configuration file for a connection with a data receiver.
 * <p>
 * {@link SensorUnit} has the same fields as the old {@link MeasurementConfig}.
 * <p>
 * {@link TeamInfo} replaces {@link FloatConfig}. Team num and name are placed here. Start and end flags go directly in to {@link ConnectionConfig}
 *
 * @param name      name of the connection
 * @param os        operating system the connection was created on
 * @param version   version of the connection config format
 * @param type      type of communication with the data receiver
 * @param port      port of the connection fixme
 * @param baudRate  baud rate of the connection fixme
 * @param startFlag flag used to tell the app to start reading data
 * @param endFlag   flag used to tell the app to stop reading data
 * @param teamInfo  name and number of the team
 * @param sensors   the data being measured
 * @param commands  commands that would be sent to data receiver
 * @see TeamInfo
 * @see SensorUnit
 * @see MeasurementConfig
 */
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
    public static ConnectionConfig toNew(com.alphagen.studio.FloatDataVisualizer.buoyui.backend.data.ConnectionConfig oldConfig) {
        return ConnectionConfig.builder()
                .name(oldConfig.connectionName())
                .os(new UserSystem().detectOS())
                .version(new Version(3, 0, 0))
                .type(ConnectionType.SERIAL)
                .port(oldConfig.port().getSystemPortName())
                .baudRate(oldConfig.baudRate())
                .startFlag(oldConfig.floatConfig().startFlag())
                .endFlag(oldConfig.floatConfig().endFlag())
                .teamInfo(TeamInfo.builder()
                        .teamNum(oldConfig.floatConfig().teamData().split("-")[0])
                        .teamName(oldConfig.floatConfig().teamData().split("-")[1])
                        .build()
                )
                .sensors(Arrays.stream(oldConfig.measurementConfigs())
                        .map(SensorUnit::toSensorUnit)
                        .toList()
                )
                .build();
    }
}
