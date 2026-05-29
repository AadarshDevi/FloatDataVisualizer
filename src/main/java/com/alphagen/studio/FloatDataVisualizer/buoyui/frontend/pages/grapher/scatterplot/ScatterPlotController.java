package com.alphagen.studio.FloatDataVisualizer.buoyui.frontend.pages.grapher.scatterplot;

import com.alphagen.studio.FloatDataVisualizer.buoyui.backend.data.ConnectionConfig;
import com.alphagen.studio.FloatDataVisualizer.buoyui.backend.data.MeasurementConfig;
import com.alphagen.studio.FloatDataVisualizer.buoyui.frontend.managers.Connections;
import com.alphagen.studio.FloatDataVisualizer.buoyui.frontend.managers.StageManager;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tooltip;
import javafx.scene.image.WritableImage;
import javafx.stage.FileChooser;
import lombok.Getter;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class ScatterPlotController {
	private static final ConnectionConfig cc = Connections.getCurrentConnection();
	private final double zoomX = 1.0;
	private final double zoomY = 1.0;
	@Getter
	@FXML
	public ScatterChart<Number, Number> scatterPlot;
	@FXML public ContextMenu contextMenu;
	@Getter
	@FXML
	ScrollPane scrollPane;
	private int oldPacketNum = -1;
	private XYChart.Series<Number, Number> series;

	@FXML
	public void initialize() {

		ContextMenu cm = new ContextMenu();
		cm.getStyleClass().add("popup-menu");
		MenuItem screenshot = new MenuItem("Screenshot");
		screenshot.setOnAction(event -> screenshotGraph());
		MenuItem resetSize = new MenuItem("Reset Size");
		resetSize.setOnAction(event -> resetGraphSize());
		cm.getItems().addAll(screenshot, resetSize);

		scatterPlot.setOnContextMenuRequested(contextMenuEvent -> {
			cm.show(scatterPlot, contextMenuEvent.getScreenX(), contextMenuEvent.getScreenY());
		});

		scatterPlot.setOnScroll(scrollEvent -> {
			if (scrollEvent.isControlDown() && scrollEvent.isAltDown() && scatterPlot.isHover()) {
				// 1. Handle Vertical Zoom (DeltaY)
				if (scrollEvent.getDeltaY() != 0) {
					double factor = (scrollEvent.getDeltaY() > 0) ? 1.1 : 0.9;
					scatterPlot.setPrefHeight(scatterPlot.getPrefHeight() * factor);
					scatterPlot.setPrefWidth(scatterPlot.getPrefWidth() * factor);
				}
				scrollEvent.consume();
			} else if (scrollEvent.isControlDown() && scatterPlot.isHover()) {

				// 1. Handle Vertical Zoom (DeltaY)
				if (scrollEvent.getDeltaY() != 0) {
					double vFactor = (scrollEvent.getDeltaY() > 0) ? 1.1 : 0.9;
					scatterPlot.setPrefHeight(scatterPlot.getPrefHeight() * vFactor);
				}

				scrollEvent.consume();
			} else if (scrollEvent.isAltDown() && scatterPlot.isHover()) {

				// 2. Handle Horizontal Zoom (DeltaX)
				// Note: Many mice don't have a horizontal wheel,
				// so you might want to map this to a different key combo instead.
				if (scrollEvent.getDeltaX() != 0) {
					double hFactor = (scrollEvent.getDeltaX() > 0) ? 1.1 : 0.9;
					scatterPlot.setPrefWidth(scatterPlot.getPrefWidth() * hFactor);
				}

				scrollEvent.consume();
			}

			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
		});
	}

	public void screenshotGraph() {
		WritableImage writableImage = scatterPlot.snapshot(new SnapshotParameters(), null);

		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Export Screenshot");
		File rawPath = fileChooser.showSaveDialog(StageManager.getMainStage());
		if (rawPath != null) {
			String filePath;
			if (rawPath.getAbsolutePath().endsWith(".png")) {
				filePath = rawPath.getAbsolutePath();
			} else {
				filePath = rawPath.getAbsolutePath() + ".png";
			}
			File file = new File(filePath);
			System.out.println(" >>> File Opened > " + file.getAbsolutePath());
			try {
				ImageIO.write(SwingFXUtils.fromFXImage(writableImage, null), "png", file);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}

	@FXML
	public void resetGraphSize() {
		scatterPlot.setPrefSize(1105.0, 475.0);
	}

	public void setAxes(MeasurementConfig time, MeasurementConfig measurement) {
		NumberAxis xAxis = (NumberAxis) scatterPlot.getXAxis();
		xAxis.setLabel(time.name() + " (" + time.unit() + ")");

		NumberAxis yAxis = (NumberAxis) scatterPlot.getYAxis();
		yAxis.setLabel(measurement.name() + " (" + measurement.unit() + ")");
	}

	public void addData(int packetNum, double time, double measure, int measureIndex) {
		if (packetNum != oldPacketNum) {
			series = new XYChart.Series<>();
			series.setName("Profile " + packetNum);
			scatterPlot.getData().add(series);
			oldPacketNum = packetNum;
		}

		XYChart.Data<Number, Number> data = new XYChart.Data<>(time, measure);

		// hovering datapoint on graph shows data
		Platform.runLater(() -> {
			Node node = data.getNode();
			if (node != null) {
				Tooltip.install(node, new Tooltip("Time: " + time + cc.measurementConfigs()[0].unit() + ", " + cc.measurementConfigs()[measureIndex].name() + ": " + measure + cc.measurementConfigs()[measureIndex].unit()));
			} else {
				System.out.println("ERROR: DataPointUI is null!!");
			}
		});
		series.getData().add(data);

	}

	public void reset() {
		oldPacketNum = -1;
		scatterPlot.getData().removeAll(scatterPlot.getData());
	}
}