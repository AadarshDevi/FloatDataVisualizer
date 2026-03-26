package com.alphagen.studio.FloatDataVisualizer.buoyui.backend.processor;

import com.alphagen.studio.FloatDataVisualizer.buoyui.backend.data.ConnectionConfig;
import com.fazecast.jSerialComm.SerialPort;
import lombok.Getter;
import lombok.Setter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.atomic.AtomicBoolean;

public class SerialProcessor implements Runnable {
	private final SerialPort sp;
	private final AtomicBoolean startDataTransfer = new AtomicBoolean(false);
	@Getter private final AtomicBoolean stopDataTransfer = new AtomicBoolean(false);
	@Setter private ConnectionConfig connectionConfig;

	public SerialProcessor(ConnectionConfig connectionConfig) {
		this.connectionConfig = connectionConfig;
		sp = connectionConfig.port();
		sp.setBaudRate(connectionConfig.baudRate());
		sp.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 0, 0);
	}

	@Override
	public void run() {

		sp.openPort();
		while (!Thread.currentThread().isInterrupted()) {
			try (BufferedReader br = new BufferedReader(new InputStreamReader(
					sp.getInputStreamWithSuppressedTimeoutExceptions()
			))) {

				String dataline;

				while ((dataline = br.readLine()) != null) {

					if (!sp.isOpen()) {
						end();
						System.err.println("Disconnected from Serial Port");
//						throw new IOException("Disconnected from Serial Port");
					} else if (sp.getInputStreamWithSuppressedTimeoutExceptions().available() == 0
							|| sp.getInputStreamWithSuppressedTimeoutExceptions().available() == -1) {
						end();
						System.err.println("Disconnected (-1) from Serial Port");
					}

					if (dataline.equals(connectionConfig.floatConfig().startFlag())) {
						startDataTransfer.set(true);
					} else if (dataline.equals(connectionConfig.floatConfig().endFlag())) {
						end();
						System.err.println("End of Transfer (Up)");
					} else if (dataline.isBlank()) {
						end();
						System.err.println("Disconnected (Blank) from Serial Port");
					}

					if (startDataTransfer.get() && !stopDataTransfer.get()) {
						System.out.print("sf: " + startDataTransfer + ", ef: " + stopDataTransfer + "\t\t");
						System.out.println(dataline);
					} else if (stopDataTransfer.get()) {
						end();
						System.err.println("End of Transfer");
					}
				}

				if (!sp.isOpen()) {
					end();
					System.err.println("Disconnected (In) from Serial Port");
				}

			} catch (IOException _) {
				if (!sp.isOpen()) {
					end();
					System.err.println("Disconnected (In) from Serial Port");
				}
			}

			if (!sp.isOpen()) {
				end();
				System.err.println("Disconnected (out) from Serial Port");
			}

		}

		end();
		System.err.println(" >>> Reached SerialPort Close");
		System.out.println();
	}

	public void end() {
		stopDataTransfer.set(true);
		sp.closePort();
		Thread.currentThread().interrupt();
	}
}