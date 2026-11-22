package com.alphagen.studio.FloatDataVisualizer.core.data;

import com.alphagen.studio.FloatDataVisualizer.buoyui.backend.data.FloatConfig;
import com.alphagen.studio.FloatDataVisualizer.buoyui.backend.data.MeasurementConfig;
import com.alphagen.studio.FloatDataVisualizer.core.system.UserSystem;
import lombok.Builder;
import lombok.Singular;
import org.apache.logging.log4j.LogManager;

import java.util.Arrays;
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

    /**
     * Converts the old BuoyUI.ConnectionConfig to the newer Core.ConnectionConfig
     *
     * @param oldConfig old connection config
     * @return new connection config
     * @see com.alphagen.studio.FloatDataVisualizer.buoyui.backend.data.ConnectionConfig
     */
    public static ConnectionConfig toNew(com.alphagen.studio.FloatDataVisualizer.buoyui.backend.data.ConnectionConfig oldConfig) {
        LogManager.getLogger(ConnectionConfig.class).info("Converting OldConfig to NewConfig.");
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
                        .number(oldConfig.floatConfig().teamData().split("-")[0])
                        .name(oldConfig.floatConfig().teamData().split("-")[1])
                        .build()
                )
                .sensors(Arrays.stream(oldConfig.measurementConfigs())
                        .map(SensorUnit::toSensorUnit)
                        .toList()
                )
                .build();
    }
}
