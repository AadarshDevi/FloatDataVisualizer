package com.alphagen.studio.FloatDataVisualizer.buoyui.frontend.pages.grapher;

import com.alphagen.studio.FloatDataVisualizer.buoyui.backend.data.ConnectionConfig;
import com.alphagen.studio.FloatDataVisualizer.buoyui.backend.data.DataPoint;
import com.alphagen.studio.FloatDataVisualizer.buoyui.backend.data.MeasurementConfig;
import com.alphagen.studio.FloatDataVisualizer.buoyui.backend.processor.DataPointProcessor;
import com.alphagen.studio.FloatDataVisualizer.buoyui.backend.processor.SerialProcessor;
import com.alphagen.studio.FloatDataVisualizer.buoyui.frontend.managers.ConnectionManager;
import com.alphagen.studio.FloatDataVisualizer.buoyui.frontend.managers.ControllerManager;
import com.alphagen.studio.FloatDataVisualizer.buoyui.frontend.managers.StageManager;
import com.alphagen.studio.FloatDataVisualizer.buoyui.frontend.pages.CardConstants;
import com.alphagen.studio.FloatDataVisualizer.buoyui.frontend.pages.grapher.scatterplot.ScatterPlotController;
import javafx.application.Platform;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import lombok.Setter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;

public class GrapherController {
	private final ExecutorService receiver = Executors.newSingleThreadExecutor();
	private final ExecutorService database = Executors.newSingleThreadExecutor();
	private final ExecutorService uiUpdater = Executors.newSingleThreadExecutor();
	@FXML public TabPane graphPane;
	@FXML public Tab controlsTab;
	@FXML public Tab tableTab;
	@FXML public Tab terminalTab;
	@FXML public Button startDataTransfer;
	@FXML public Button stopDataTransfer;
	@FXML public HBox checkBoxesContainer;
	@FXML public Label connection_name_label;
	@FXML public TableView<DataPoint> tableView;
	@FXML public Label startFlagLabel;
	@FXML public Label endFlagLabel;
	@FXML public TableColumn<DataPoint, String> timeTableCol;
	private Future<?> activeTask;
	private Future<?> activeDataBase;
	@Setter private ConnectionConfig connectionConfig;
	private SerialProcessor sp;
	private DataPointProcessor dpp;
	private Future<?> activeUIUpdater;

	// todo: add css in the fxml for the table rows, focused, selected, hover, normal, font

	@FXML
	public void initialize() {
		System.out.println();
		System.out.println(" >>> Serial Communication > Initializing");
		graphPane.getSelectionModel().select(2);
		terminalTab.setDisable(true);
		checkBoxesContainer.setDisable(true);
		checkBoxesContainer.setVisible(false);
		checkBoxesContainer.setManaged(false);

		this.connectionConfig = ConnectionManager.getCurrentConnection();

		if (connectionConfig == null) {
			System.out.println(" >>> Serial Communication > Null ConnectionConfig");
			return;
		}

		connection_name_label.setText(connectionConfig.connectionName());
		// todo: setup tabs > depth, pressure etc

		// todo:
		// 		1. get length of MeasurementsConfig
		// 		2. create checkbox
		// 			1. get size
		// 			2. get style
		// 			3. selected = false
		// 		3. create table col
		// 			1. get size
		// 			2. get style
		// 			3. add to table
		// 		4. put data
		// 		5. lock table col length and checkbox length

		endFlagLabel.setText(connectionConfig.floatConfig().endFlag());
		startFlagLabel.setText(connectionConfig.floatConfig().startFlag());

		@SuppressWarnings("unchecked")
		TableColumn<DataPoint, String> teamCol = (TableColumn<DataPoint, String>) tableView.getColumns().getFirst();
		teamCol.setCellValueFactory(dp -> new SimpleStringProperty(dp.getValue().teamInfo()));

		@SuppressWarnings("unchecked") // todo
		TableColumn<DataPoint, Number> packetNum = (TableColumn<DataPoint, Number>) tableView.getColumns().get(1);
		packetNum.setCellValueFactory(dp -> new SimpleIntegerProperty(dp.getValue().packetNum()));

		@SuppressWarnings("unchecked") // todo
		TableColumn<DataPoint, Double> timeCol = (TableColumn<DataPoint, Double>) tableView.getColumns().get(2);
		timeCol.setCellValueFactory(dp -> new SimpleDoubleProperty(dp.getValue().time()).asObject());

		MeasurementConfig[] measurementConfigs = connectionConfig.measurementConfigs();
		MeasurementConfig timeConfig = measurementConfigs[0];

		timeTableCol.setText(timeConfig.name() + " (" + timeConfig.unit() + ")");
		for (int i = 1; i < measurementConfigs.length; i++) {

			MeasurementConfig measurementConfig = measurementConfigs[i];

			// Tabs > ScatterPlots
			Tab tab = new Tab(measurementConfig.name());

			FXMLLoader fxmlLoader = new FXMLLoader(CardConstants.SCATTER_PLOT);
			BorderPane scatterPlot;
			try {
				scatterPlot = fxmlLoader.load();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}

			ScatterPlotController spc = fxmlLoader.getController();
			spc.setAxes(timeConfig, measurementConfig);
			scatterPlot.getProperties().put("controller", spc);
			tab.getProperties().put("plot", scatterPlot);
			tab.getProperties().put("plot_controller", spc);
			tab.setContent(scatterPlot);
			graphPane.getTabs().add(tab);

			// Table
			TableColumn<DataPoint, Double> col = new TableColumn<>();
			col.setText(measurementConfig.name() + " (" + measurementConfig.unit() + ")");
			col.setMinWidth(150);
			col.setPrefWidth(150);
			tableView.getColumns().add(col);


			int finalI = i;
			col.setCellValueFactory(dp -> {
				try {
					double num = dp.getValue().measurements()[finalI - 1];
					return new SimpleDoubleProperty(num).asObject();
				} catch (ArrayIndexOutOfBoundsException _) {
					System.out.println(" >>> Column will not get data because it receives less data");
				}
				return new SimpleObjectProperty<>(null);
			});


			// todo for selective export
//			CheckBox cb = new CheckBox();
//			cb.setMinWidth(150);
//			cb.setPrefWidth(150);
//			cb.setSelected(true);
//
//			// link the size of checkbox to table col
//			checkBoxesContainer.getChildren().add(cb);
		}

		stopingDataTransfer();
		sp = new SerialProcessor(connectionConfig);
		dpp = new DataPointProcessor();
		sp.setDpp(dpp);
		System.out.println(" >>> Grapher: SP & DPP > Ready");
	}

	@FXML
	public void stopingDataTransfer() {
		// disable stop button and enable start button
		if (activeTask != null && !activeTask.isCancelled()) {
			activeTask.cancel(true);
			activeDataBase.cancel(true);
			activeUIUpdater.cancel(true);
		}

		stopDataTransfer.setDisable(true);
		startDataTransfer.setDisable(false);

		if (dpp != null) {
			System.out.println(" >>> Parsed Array > " + dpp.getParsedArray().size());
			System.err.println(" >>> Serial Communication > Stop");
		}
	}

	@FXML
	public void startingDataTransfer() {
		// disable start button and enable stop button
		startDataTransfer.setDisable(true);
		stopDataTransfer.setDisable(false);

		for (int i = 3; i < graphPane.getTabs().size(); i++) {
			((ScatterPlotController) graphPane.getTabs().get(i).getProperties().get("plot_controller")).reset();
		}

		tableView.getItems().removeAll(tableView.getItems());

		System.out.println(" >>> Serial Communication > Start");

		activeTask = receiver.submit(sp);
		activeDataBase = database.submit(dpp);
		activeUIUpdater = uiUpdater.submit(() -> {
			System.out.println(" >>> Grapher > Running UI Updater");
			LinkedBlockingQueue<DataPoint> rawArray = dpp.getParsedArray();
			while (!Thread.currentThread().isInterrupted()) {
				DataPoint dp;
				try {
					dp = rawArray.take();
				} catch (InterruptedException e) {
					throw new RuntimeException(e);
				}

				Platform.runLater(() -> {
					System.out.println(" >>> Measurement Configs > " + Arrays.toString(dp.measurements()));
					double[] measures = dp.measurements();
					for (int i = 3; i < graphPane.getTabs().size(); i++) {
						int measureIndex = i - 3;
						ScatterPlotController spc = (ScatterPlotController) graphPane.getTabs().get(i).getProperties().get("plot_controller");
						try {
							spc.addData(dp.packetNum(), dp.time(), measures[measureIndex], measureIndex + 1);
						} catch (ArrayIndexOutOfBoundsException | NullPointerException _) {
						}
					}

					// todo insert data into table
					tableView.getItems().add(dp);
				});
			}
		});
		System.out.println(" >>> Serial Communication > Serial and Database");
	}

	@FXML
	public void backHome() {
		stopingDataTransfer();
		System.out.println(" >>> Serial Communication > Back Home");
		Stage stage = StageManager.getMainStage();
		Scene scene = StageManager.getConnectionsScene();
		ControllerManager.setGrapherController(null);
		stage.setScene(scene);
	}

	@FXML
	public void exportData() {

		ObservableList<DataPoint> list = tableView.getItems();

		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Export Screenshot");
		File rawPath = fileChooser.showSaveDialog(StageManager.getMainStage());

		if (rawPath == null) return;
		File file = new File(rawPath.getAbsolutePath() + ".csv");

		try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file))) {

			for (int i = 0; i < connectionConfig.measurementConfigs().length; i++) {
				bufferedWriter.write(connectionConfig.measurementConfigs()[i].name());
				bufferedWriter.write("(");
				bufferedWriter.write(connectionConfig.measurementConfigs()[i].unit());
				bufferedWriter.write(")");
				if (i != connectionConfig.measurementConfigs().length - 1) {
					bufferedWriter.write(",");
				}
			}
			bufferedWriter.newLine();

			for (DataPoint dp : list) {
				double[] measurements = dp.measurements();

				bufferedWriter.write(Double.toString(dp.time()));
				bufferedWriter.write(",");

				for (int i = 0; i < measurements.length; i++) {
					double measurement = measurements[i];
					bufferedWriter.write(Double.toString(measurement));
					if (i != measurements.length - 1) {
						bufferedWriter.write(",");
					}
				}
				bufferedWriter.newLine();
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@FXML
	public void exportRaw() {
		// todo: based on the tab, export data.
		//  if tab == terminal then export raw data
		//  if tab == table then export csv

		// todo same for the screen shot button
		//  	update the svg to a screen shot svg

		// todo add a shortcut to do the entire window screenshot

		ObservableList<DataPoint> list = tableView.getItems();

		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Export Screenshot");
		File rawPath = fileChooser.showSaveDialog(StageManager.getMainStage());

		if (rawPath == null) return;
		File file = new File(rawPath.getAbsolutePath() + ".csv");

		try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file))) {

			for (DataPoint dp : list) {
				bufferedWriter.write(dp.toRaw());
				bufferedWriter.newLine();
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

	}
}