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
}
