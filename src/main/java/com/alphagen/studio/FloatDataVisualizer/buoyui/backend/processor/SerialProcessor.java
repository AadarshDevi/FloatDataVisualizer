package com.alphagen.studio.FloatDataVisualizer.buoyui.backend.processor;

import com.alphagen.studio.FloatDataVisualizer.buoyui.backend.data.ConnectionConfig;
import com.alphagen.studio.FloatDataVisualizer.buoyui.frontend.managers.ControllerManager;
import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortTimeoutException;
import lombok.Getter;
import lombok.Setter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.atomic.AtomicBoolean;

public class SerialProcessor implements Runnable {
	private final SerialPort sp;
	@Getter private final AtomicBoolean startDataTransfer = new AtomicBoolean(false);
	@Getter private final AtomicBoolean stopDataTransfer = new AtomicBoolean(false);
	@Getter private final AtomicBoolean disconnected = new AtomicBoolean(false);
	@Setter private ConnectionConfig connectionConfig;
	@Setter private DataPointProcessor dpp;

	public SerialProcessor(ConnectionConfig connectionConfig) {
		this.connectionConfig = connectionConfig;
		sp = connectionConfig.port();
		sp.setBaudRate(connectionConfig.baudRate());
		sp.setComPortTimeouts(SerialPort.TIMEOUT_READ_BLOCKING, 1000, 0);
	}

	@Override
	public void run() {

		disconnected.set(false);
		sp.openPort();
		System.out.println();

		startDataTransfer.set(false);
		stopDataTransfer.set(false);

		if (!sp.openPort()) {
			System.err.println("Unable to open port");
			return;
		}

		try (BufferedReader br = new BufferedReader(new InputStreamReader(
			sp.getInputStream()
		))) {
			while (!Thread.currentThread().isInterrupted()) {

				try {
					String rawline = br.readLine();

					if (rawline == null) {
						if (!sp.isOpen() || sp.bytesAvailable() == -1) {
							System.err.println(" >>> Disconnected from Serial Port");
							disconnected.set(true);
							stopDataTransfer.set(true);
							break;
						}
						continue;
					}
					String dataline = rawline.trim();

					if (dataline.equals(connectionConfig.floatConfig().startFlag())) {
						startDataTransfer.set(true);
						stopDataTransfer.set(false);
						continue;
					} else if (dataline.equals(connectionConfig.floatConfig().endFlag())) {
						stopDataTransfer.set(true);
						startDataTransfer.set(false);
						System.err.println("End of Transfer");
						break;
					}

					if (startDataTransfer.get() && !stopDataTransfer.get() && dataline.startsWith(connectionConfig.floatConfig().teamData())) {
//						System.out.println(dataline);
						dpp.getRawArray().put(dataline);
//						System.out.println(" >>> Raw Size > " + dpp.getRawArray().size());
					}
				} catch (SerialPortTimeoutException _) {
					if (Thread.currentThread().isInterrupted() || !sp.isOpen()) {
						break;
					}
				} catch (InterruptedException _) {
					System.err.println(" >>> Serial Processor > DataPointProcessor Interrupted");
					Thread.currentThread().interrupt();
					break;
				}
			}
		} catch (IOException e) {
			System.err.println("Hardware Disconnected: " + e.getMessage());
		} finally {
			sp.closePort();
			Thread.currentThread().interrupt();
			System.err.println(" >>> SerialPort Close Reached");
			System.out.println();

			startDataTransfer.set(false);
			stopDataTransfer.set(false);

			ControllerManager.getGrapherController().stopingDataTransfer();

			System.out.println(" >>> Hardware Status > Disconnected: " + disconnected);
		}
	}
}