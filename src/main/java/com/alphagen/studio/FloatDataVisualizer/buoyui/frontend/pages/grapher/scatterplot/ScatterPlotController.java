package com.alphagen.studio.FloatDataVisualizer.buoyui.frontend.pages.grapher.scatterplot;

import com.alphagen.studio.FloatDataVisualizer.buoyui.backend.data.MeasurementConfig;
import javafx.fxml.FXML;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;

public class ScatterPlotController {
	@FXML public ScatterChart<Number, Number> scatterPlot;
	private int oldPacketNum = -1;
	private XYChart.Series<Number, Number> series;

	public void setAxes(MeasurementConfig time, MeasurementConfig measurement) {
		NumberAxis xAxis = (NumberAxis) scatterPlot.getXAxis();
		xAxis.setLabel(time.name() + " (" + time.unit() + ")");

		NumberAxis yAxis = (NumberAxis) scatterPlot.getYAxis();
		yAxis.setLabel(measurement.name() + " (" + measurement.unit() + ")");
	}

	public void addData(int packetNum, double time, double measure) {
		if (packetNum != oldPacketNum) {
			series = new XYChart.Series<>();
			series.setName("Profile " + packetNum);
			scatterPlot.getData().add(series);
			oldPacketNum = packetNum;
		}

		series.getData().add(new XYChart.Data<>(time, measure));

	}

}