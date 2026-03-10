package com.alphagen.studio.FloatDataVisualizer.buoyui.backend.data;

import com.fazecast.jSerialComm.SerialPort;
import lombok.Builder;

@Builder
public record ConnectionConfig(String connectionName, int baudRate, SerialPort port, ConnectionType portType,
							   FloatConfig floatConfig) {}