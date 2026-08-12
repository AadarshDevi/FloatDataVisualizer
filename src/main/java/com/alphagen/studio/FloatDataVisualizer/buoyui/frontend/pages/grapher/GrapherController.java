package com.alphagen.studio.FloatDataVisualizer.buoyui.frontend.pages.grapher;

import com.alphagen.studio.FloatDataVisualizer.buoyui.backend.app.theme.ThemeProcessor;
import com.alphagen.studio.FloatDataVisualizer.buoyui.backend.constants.Debug;
import com.alphagen.studio.FloatDataVisualizer.buoyui.backend.data.ConnectionConfig;
import com.alphagen.studio.FloatDataVisualizer.buoyui.backend.data.DataPoint;
import com.alphagen.studio.FloatDataVisualizer.buoyui.backend.data.FloatConfig;
import com.alphagen.studio.FloatDataVisualizer.buoyui.backend.data.MeasurementConfig;
import com.alphagen.studio.FloatDataVisualizer.buoyui.backend.processor.DataPointProcessor;
import com.alphagen.studio.FloatDataVisualizer.buoyui.backend.processor.SerialCommunicator;
import com.alphagen.studio.FloatDataVisualizer.buoyui.backend.settings.SettingsManager;
import com.alphagen.studio.FloatDataVisualizer.buoyui.frontend.managers.Connections;
import com.alphagen.studio.FloatDataVisualizer.buoyui.frontend.managers.ControllerManager;
import com.alphagen.studio.FloatDataVisualizer.buoyui.frontend.managers.StageManager;
import com.alphagen.studio.FloatDataVisualizer.buoyui.frontend.pages.CardConstants;
import com.alphagen.studio.FloatDataVisualizer.buoyui.frontend.pages.grapher.scatterplot.ScatterPlotController;
import com.alphagen.studio.FloatDataVisualizer.buoyui.frontend.util.MeasurementLabel;
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
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import lombok.Setter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class GrapherController {
    private final ExecutorService receiver = Executors.newSingleThreadExecutor();
    private final ExecutorService database = Executors.newSingleThreadExecutor();
    private final ExecutorService uiUpdater = Executors.newSingleThreadExecutor();
    private final AtomicBoolean running = new AtomicBoolean(false);
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
    @FXML public Label teamInfoLabel;
    @FXML public Label packetLabel;
    @FXML public Label baudRateLabel;
    @FXML public Label serialPortLabel;
    @FXML public Label rawDataFormatLabel;
    @FXML public TilePane measurementsTilePane;
    @FXML public TextArea terminalTextArea;
    @FXML public ScrollPane configScroll;
    @FXML public VBox configVBox;
    private SettingsManager sm;
    private Future<?> activeTask;
    private Future<?> activeDataBase;
    @Setter private ConnectionConfig connectionConfig;
    //	private SerialProcessor seri;
    private SerialCommunicator serialCommunicator;
    private DataPointProcessor dataPointProcessor;
    private Future<?> activeUIUpdater;

    @FXML
    public void initialize() {
        sm = SettingsManager.getInstance();
        System.out.println();
        System.out.println(" >>> Serial Communication > Initializing");
        graphPane.getSelectionModel().select(2);
        checkBoxesContainer.setDisable(true);
        checkBoxesContainer.setVisible(false);
        checkBoxesContainer.setManaged(false);

        this.connectionConfig = Connections.getCurrentConnection();

        if (connectionConfig == null) {
            System.out.println(" >>> Serial Communication > Null ConnectionConfig");
            return;
        }

        connection_name_label.setText(connectionConfig.connectionName());

        FloatConfig fc = connectionConfig.floatConfig();
        MeasurementConfig[] measurementConfigs = connectionConfig.measurementConfigs();
        teamInfoLabel.setText(fc.teamData());
        packetLabel.setText(fc.pkt());
        baudRateLabel.setText(Integer.toString(connectionConfig.baudRate()));
        serialPortLabel.setText(connectionConfig.port().getDescriptivePortName());
        StringBuilder measurementsString = new StringBuilder();
        for (MeasurementConfig measurementConfig : measurementConfigs) {
            measurementsString.append(",").append(measurementConfig.name()).append("(").append(measurementConfig.unit()).append(")");
            MeasurementLabel ml = new MeasurementLabel(measurementConfig.name() + " (" + measurementConfig.unit() + ")");
            ml.getStylesheets().clear(); // testme: theme
            ml.getStylesheets().add(ThemeProcessor.getThemeCSS().toString()); // testme: theme
            measurementsTilePane.getChildren().add(ml);
        }
        rawDataFormatLabel.setText(fc.teamData() + "," + fc.pkt() + measurementsString);
        endFlagLabel.setText(fc.endFlag());
        startFlagLabel.setText(fc.startFlag());

        // todo: update (7/29/2026) - better solution: https://stackoverflow.com/questions/27739833/adapt-tableview-menu-button
        @SuppressWarnings("unchecked")
        TableColumn<DataPoint, String> teamCol = (TableColumn<DataPoint, String>) tableView.getColumns().getFirst();
        teamCol.setCellValueFactory(dp -> new SimpleStringProperty(dp.getValue().teamInfo()));

        @SuppressWarnings("unchecked") // todo
        TableColumn<DataPoint, Number> packetNum = (TableColumn<DataPoint, Number>) tableView.getColumns().get(1);
        packetNum.setCellValueFactory(dp -> new SimpleIntegerProperty(dp.getValue().packetNum()));

        @SuppressWarnings("unchecked") // todo
        TableColumn<DataPoint, Double> timeCol = (TableColumn<DataPoint, Double>) tableView.getColumns().get(2);
        timeCol.setCellValueFactory(dp -> new SimpleDoubleProperty(dp.getValue().time()).asObject());

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

            scatterPlot.getStylesheets().clear(); // testme: theme
            scatterPlot.getStylesheets().add(ThemeProcessor.getThemeCSS().toString()); // testme: theme

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

        dataPointProcessor = new DataPointProcessor();
        serialCommunicator = new SerialCommunicator(connectionConfig, dataPointProcessor, sm.getVerboseTerminal());
//        sp = new SerialProcessor(connectionConfig);
//        sp.setDpp(dpp);
        System.out.println(" >>> Grapher: SP & DPP > Ready");
        stopingDataTransfer();

        // add context menu for terminal
        ContextMenu terminalMenu = new ContextMenu();
        terminalMenu.getStyleClass().add("popup-menu");

        // export terminal ouput
        MenuItem exportOutput = new MenuItem("Export Output");
        exportOutput.setOnAction(event -> {
            // code
        });
        exportOutput.setDisable(true);

        // autoscroll
        CheckMenuItem autoscrollTerminal = new CheckMenuItem("Auto-Scroll");
        autoscrollTerminal.setSelected(SettingsManager.getInstance().getAutoscrollTerminal());
        autoscrollTerminal.setOnAction(event -> {
            boolean autoscroll = !SettingsManager.getInstance().getAutoscrollTerminal();
            autoscrollTerminal.setSelected(autoscroll);
            SettingsManager.getInstance().setAutoscrollTerminal(autoscroll);
        });

        // verbose output
        CheckMenuItem verboseOutput = new CheckMenuItem("Verbose");
        verboseOutput.setSelected(sm.getVerboseTerminal());
        verboseOutput.setOnAction(event -> {
            boolean verbose = !sm.getVerboseTerminal();
            verboseOutput.setSelected(verbose);
            serialCommunicator.setVerbose(verbose);
            sm.setVerboseTerminal(verbose);
        });
        terminalMenu.getItems().addAll(exportOutput, autoscrollTerminal, verboseOutput);
        terminalTextArea.setContextMenu(terminalMenu);
    }

    // fixme later
    @FXML
    public void stopingDataTransfer() {
        
        // disable stop button and enable start button
        stopDataTransfer.setDisable(true);
        startDataTransfer.setDisable(false);

        if (activeTask != null) {
            activeTask.cancel(true);
            activeDataBase.cancel(true);
            activeUIUpdater.cancel(true);
        }

        if (dataPointProcessor != null) {
            System.out.println(" >>> Parsed Array > " + dataPointProcessor.getParsedArray().size());
            System.err.println(" >>> Serial Communication > Stop");
        }

        if (serialCommunicator == null) {
            Platform.runLater(() -> {
                System.err.println(" >>> Disconnected from Device");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Connection Exception");
                alert.setHeaderText(null);
                alert.setContentText("Disconnected from Hardware.");
                alert.showAndWait();
            });
            return;
        }

        if (serialCommunicator.isConnected()) {
            serialCommunicator.close();
            System.out.println(" [Debug] Port Closed? " + serialCommunicator.isConnected());
        }

        System.out.println("\n >>> [Debug] Stopping Data Transfer\n");
        running.set(false);
    }

    // fixme the app is broken here
    @FXML
    public void startingDataTransfer() {

        System.out.println("[StartData]");

        if (activeUIUpdater != null) {
            activeUIUpdater.cancel(true);
            activeTask.cancel(true);
            activeDataBase.cancel(true);
        }

        // check if the serial comms is still running
        if (serialCommunicator.isConnected()) {
            serialCommunicator.stop();
        }

        if (!serialCommunicator.open()) {
            System.err.println(" >>> Port not found");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Connection Exception");
            alert.setHeaderText(null);
            alert.setContentText("Hardware not connected to device. Serial Port disconnected and not found.");
            alert.showAndWait();
            return;
        }
        serialCommunicator.close();

        System.out.println("[StartData] Serial Ready");

        // disable buttons
        startDataTransfer.setDisable(true);
        stopDataTransfer.setDisable(false);

        System.out.println("[StartData] Buttons Set");

        for (int i = 3; i < graphPane.getTabs().size(); i++) {
            ((ScatterPlotController) graphPane.getTabs().get(i).getProperties().get("plot_controller")).reset();
        }

        System.out.println("[StartData] Graphs Reset");

        tableView.getItems().clear(); //.removeAll(tableView.getItems());
        terminalTextArea.clear();

        System.out.println("[StartData] Table & Terminal Reset");

        activeTask = receiver.submit(serialCommunicator);
        activeDataBase = database.submit(dataPointProcessor);
        activeUIUpdater = uiUpdater.submit(() -> {
            System.out.println("[UI Updater] Ready");
            while (!Thread.currentThread().isInterrupted()) {
                LinkedBlockingQueue<Object> linkedBlockingQueue = dataPointProcessor.getParsedArray();

                // 1. get data
                Object obj;

                try {
                    obj = linkedBlockingQueue.take();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                if (obj instanceof String terminalOutput) {
                    if (sm.getAutoscrollTerminal())
                        terminalTextArea.appendText(terminalOutput + "\n"); // textarea autoscroll
                    else
                        terminalTextArea.setText(terminalTextArea.getText() + terminalOutput + "\n");

                    if (terminalOutput.equals(connectionConfig.floatConfig().endFlag())) {
                        stopingDataTransfer();
                    }

                    continue;
                }

                DataPoint dataPoint = (DataPoint) obj;

                Platform.runLater(() -> {
                    // 2. add to table
                    tableView.getItems().add(dataPoint);
                    if (sm.getAutoscrollTable())
                        tableView.scrollTo(dataPoint);

                    if (sm.getAutoscrollTerminal())
                        terminalTextArea.appendText(dataPoint.toRaw() + "\n"); // textarea autoscroll
                    else
                        terminalTextArea.setText(terminalTextArea.getText() + dataPoint.toRaw() + "\n");

                    // 3. get measurements
                    double[] data = dataPoint.measurements();
                    for (int i = 3; i < graphPane.getTabs().size(); i++) {
                        // a. get ScatterPlotController
                        ScatterPlotController scatterPlotController = (ScatterPlotController) graphPane.getTabs().get(i).getProperties().get("plot_controller");

                        // b. get data (double) from data[] - 3 // 3 is the first data graph
                        // |--- dataIndex = i - 3
                        int dataIndex = i - 3;

                        // c. add packet num, time, data, index
                        scatterPlotController.addData(dataPoint.packetNum(), dataPoint.time(), data[dataIndex], dataIndex);
                    }
                });

            }
        });
        running.set(true);
    }

    @FXML
    public void backHome() {
        if (running.get())
            stopingDataTransfer();

        System.out.println(" >>> Serial Communication > Back Home");
        Stage stage = StageManager.getMainStage();
        Scene scene = StageManager.getConnectionsScene();
        ControllerManager.setGrapherController(null);
        stage.setScene(scene);
        if (Debug.useWindowModes) {
            ControllerManager.getConnectionsController().fullscreenApp();
            stage.setFullScreen(stage.isFullScreen()); // fixme: when going home, it goes to fullscreen
        }
    }

    @FXML
    public void exportData() {

        ObservableList<DataPoint> list = tableView.getItems();

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Export Screenshot");
        File rawPath = fileChooser.showSaveDialog(StageManager.getMainStage());
        File file;

        if (rawPath == null) return;
        if (rawPath.getName().endsWith(".csv"))
            file = new File(rawPath.getAbsolutePath());
        else
            file = new File(rawPath.getAbsolutePath() + ".csv");

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
        File file;

        if (rawPath == null) return;
        if (rawPath.getName().endsWith(".csv"))
            file = new File(rawPath.getAbsolutePath());
        else
            file = new File(rawPath.getAbsolutePath() + ".csv");

        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file))) {
            for (DataPoint dp : list) {
                bufferedWriter.write(dp.toRaw());
                bufferedWriter.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void fullscreen() { // fixme: when going here, fullscreen to windowed mode

        if (!Debug.useWindowModes) {
            return;
        }

        Stage stage = StageManager.getMainStage();
        System.out.println("isFullScreen: " + stage.isFullScreen());

        if (!stage.isFullScreen()) {
            System.out.println("Fullscreen: No");
            return;
        }

        System.out.println("Fullscreen: Yes");

        double scrollWidth = graphPane.getWidth() - 24;
        double scrollHeight = graphPane.getHeight() - 24;
        System.out.println("scrollWidth: " + scrollWidth + " scrollHeight: " + scrollHeight);

        double oldWidth = configScroll.getWidth() - 24;
        double oldHeight = configScroll.getHeight();
        System.out.println("oldWidth: " + oldWidth + " oldWidth: " + oldHeight);

        oldWidth = configScroll.getPrefWidth() - 24;
        oldHeight = configScroll.getPrefHeight();
        System.out.println("oldPrefWidth: " + oldWidth + " oldPrefWidth: " + oldHeight);

        stage.setFullScreen(true);

        double newWidth = configScroll.getWidth() - 24;
        double newHeight = (oldHeight * newWidth) / oldWidth;
        System.out.println("newWidth: " + newWidth + " newHeight: " + newHeight);

        configVBox.setPrefWidth(newWidth);
        configVBox.setPrefHeight(newHeight);
        System.out.println();


//		if (StageManager.getMainStage().isFullScreen()) {
//
//			double scrollWidth = graphPane.getWidth() - 24;
//			double scrollHeight = graphPane.getHeight() - 24;
//			System.out.println("scrollWidth: " + scrollWidth + " scrollHeight: " + scrollHeight);
//
//			spc.getScatterPlot().setPrefSize(scrollWidth, scrollHeight);
//			spc.getScatterPlot().setMinSize(scrollWidth, scrollHeight);
//
//			double oldWidth = spc.getScatterPlot().getWidth();
//			double oldHeight = spc.getScatterPlot().getHeight();
//			System.out.println("oldWidth: " + oldWidth + " oldHeight: " + oldHeight);
//
//			oldWidth = spc.getScatterPlot().getPrefWidth() - 24;
//			oldHeight = spc.getScatterPlot().getPrefHeight();
//			System.out.println("oldPrefWidth: " + oldWidth + " oldPrefWidth: " + oldHeight);
//
//			double newWidth = spc.getScrollPane().getWidth() - 24;
//			double newHeight = (oldHeight * newWidth) / oldWidth;
//			System.out.println("newWidth: " + newWidth + " newHeight: " + newHeight);
//
//			spc.getScatterPlot().setPrefWidth(newWidth);
//			spc.getScatterPlot().setPrefHeight(newHeight);
//
//			spc.getScatterPlot().setMinWidth(newWidth);
//			spc.getScatterPlot().setMinHeight(newHeight);
//			System.out.println();
//		}
    }
}