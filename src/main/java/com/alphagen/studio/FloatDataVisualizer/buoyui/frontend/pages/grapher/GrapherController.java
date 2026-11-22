package com.alphagen.studio.FloatDataVisualizer.buoyui.frontend.pages.grapher;

import com.alphagen.studio.FloatDataVisualizer.buoyui.backend.app.theme.ThemeProcessor;
import com.alphagen.studio.FloatDataVisualizer.buoyui.backend.constants.Debug;
import com.alphagen.studio.FloatDataVisualizer.buoyui.backend.data.DataPoint;
import com.alphagen.studio.FloatDataVisualizer.buoyui.backend.processor.DataPointProcessor;
import com.alphagen.studio.FloatDataVisualizer.buoyui.backend.settings.SettingsManager;
import com.alphagen.studio.FloatDataVisualizer.buoyui.frontend.managers.Connections;
import com.alphagen.studio.FloatDataVisualizer.buoyui.frontend.managers.ControllerManager;
import com.alphagen.studio.FloatDataVisualizer.buoyui.frontend.managers.StageManager;
import com.alphagen.studio.FloatDataVisualizer.buoyui.frontend.pages.CardConstants;
import com.alphagen.studio.FloatDataVisualizer.buoyui.frontend.pages.grapher.scatterplot.ScatterPlotController;
import com.alphagen.studio.FloatDataVisualizer.buoyui.frontend.util.MeasurementLabel;
import com.alphagen.studio.FloatDataVisualizer.core.data.ConnectionConfig;
import com.alphagen.studio.FloatDataVisualizer.core.data.SensorUnit;
import com.alphagen.studio.FloatDataVisualizer.core.processor.SerialConnection;
import com.alphagen.studio.FloatDataVisualizer.expo.RunnableManager;
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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;

public class GrapherController {
    private static final Logger logger = LogManager.getLogger(GrapherController.class);
    //	private final ExecutorService receiver = Executors.newSingleThreadExecutor();
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
    @FXML public Label teamInfoLabel;
    @FXML public Label packetLabel;
    @FXML public Label baudRateLabel;
    @FXML public Label serialPortLabel;
    @FXML public Label rawDataFormatLabel;
    @FXML public TilePane measurementsTilePane;
    @FXML public TextArea terminalTextArea;
    //	@FXML public ScrollPane terminalScroll;
    @FXML public ScrollPane configScroll;
    //	@FXML public ScrollPane tableScroll;
    @FXML public VBox configVBox;
    private RunnableManager readerRunnableManager;
    private SettingsManager sm;
    //	private Future<?> activeTask;
    private Future<?> activeDataBase;
    @Setter private ConnectionConfig connectionConfig;
    //    private SerialProcessor sp;
    private DataPointProcessor dpp;
    private Future<?> activeUIUpdater;

    // todo: add css in the fxml for the table rows, focused, selected, hover, normal, font

    @FXML
    public void initialize() {
        sm = SettingsManager.getInstance();
        logger.info("Serial Communication > Initializing");
        graphPane.getSelectionModel().select(2);
        checkBoxesContainer.setDisable(true);
        checkBoxesContainer.setVisible(false);
        checkBoxesContainer.setManaged(false);

        this.connectionConfig = ConnectionConfig.toNew(Connections.getCurrentConnection());

        if (connectionConfig == null) {
            logger.info("Serial Communication > Null ConnectionConfig");
            return;
        }

        connection_name_label.setText(connectionConfig.name());
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

//        FloatConfig fc = connectionConfig.floatConfig();
//        MeasurementConfig[] measurementConfigs = connectionConfig.measurementConfigs();
        teamInfoLabel.setText(connectionConfig.teamInfo().toString());
        packetLabel.setText("pkt-");
        baudRateLabel.setText(Integer.toString(connectionConfig.baudRate()));
        serialPortLabel.setText(connectionConfig.port());
        StringBuilder measurementsString = new StringBuilder();
        for (SensorUnit sensorUnit : connectionConfig.sensors()) {
            measurementsString.append(",").append(sensorUnit.name()).append("(").append(sensorUnit.unit()).append(")");
            MeasurementLabel ml = new MeasurementLabel(sensorUnit.name() + " (" + sensorUnit.unit() + ")");
            ml.getStylesheets().clear(); // testme: theme
            ml.getStylesheets().add(ThemeProcessor.getThemeCSS().toString()); // testme: theme
            measurementsTilePane.getChildren().add(ml);
        }
        rawDataFormatLabel.setText(connectionConfig.teamInfo().toString() + ",pkt-" + measurementsString);
        endFlagLabel.setText(connectionConfig.endFlag());
        startFlagLabel.setText(connectionConfig.startFlag());

        @SuppressWarnings("unchecked")
        TableColumn<DataPoint, String> teamCol = (TableColumn<DataPoint, String>) tableView.getColumns().getFirst();
        teamCol.setCellValueFactory(dp -> new SimpleStringProperty(dp.getValue().teamInfo()));

        @SuppressWarnings("unchecked") // todo
        TableColumn<DataPoint, Number> packetNum = (TableColumn<DataPoint, Number>) tableView.getColumns().get(1);
        packetNum.setCellValueFactory(dp -> new SimpleIntegerProperty(dp.getValue().packetNum()));

        @SuppressWarnings("unchecked") // todo
        TableColumn<DataPoint, Double> timeCol = (TableColumn<DataPoint, Double>) tableView.getColumns().get(2);
        timeCol.setCellValueFactory(dp -> new SimpleDoubleProperty(dp.getValue().time()).asObject());

        SensorUnit timeConfig = connectionConfig.sensors().getFirst();

        timeTableCol.setText(timeConfig.name() + " (" + timeConfig.unit() + ")");

        for (int i = 1; i < connectionConfig.sensors().size(); i++) {

            SensorUnit sensorUnit = connectionConfig.sensors().get(i);

            // Tabs > ScatterPlots
            Tab tab = new Tab(sensorUnit.name());

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
            spc.setAxes(timeConfig, sensorUnit);
            scatterPlot.getProperties().put("controller", spc);
            tab.getProperties().put("plot", scatterPlot);
            tab.getProperties().put("plot_controller", spc);
            tab.setContent(scatterPlot);
            graphPane.getTabs().add(tab);

            // Table
            TableColumn<DataPoint, Double> col = new TableColumn<>();
            col.setText(sensorUnit.name() + " (" + sensorUnit.unit() + ")");
            col.setMinWidth(150);
            col.setPrefWidth(150);
            tableView.getColumns().add(col);


            int finalI = i;
            col.setCellValueFactory(dp -> {
                try {
                    double num = dp.getValue().measurements()[finalI - 1];
                    return new SimpleDoubleProperty(num).asObject();
                } catch (ArrayIndexOutOfBoundsException _) {
                    logger.info("Column will not get data because it receives less data");
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
                logger.info(data);
        dpp = new DataPointProcessor();
        sp.setDpp(dpp);
        System.out.println(" >>> Grapher: SP & DPP > Ready");
        logger.info("Grapher: SP & DPP > Ready");
    }

    @FXML
    public void stopingDataTransfer() {
        // disable stop button and enable start button
//		if (activeTask != null && !activeTask.isCancelled()) {
//			activeTask.cancel(true);
//			activeDataBase.cancel(true);
//			activeUIUpdater.cancel(true);
//		}

        if (readerRunnableManager.isRunning()) {
            readerRunnableManager.stop();
            activeDataBase.cancel(true);
            activeUIUpdater.cancel(true);
        }

        stopDataTransfer.setDisable(true);
        startDataTransfer.setDisable(false);

        if (dpp != null) {
            System.out.println(" >>> Parsed Array > " + dpp.getParsedArray().size());
            System.err.println(" >>> Serial Communication > Stop");
            logger.info("Parsed Array > " + dpp.getParsedArray().size());
            logger.error("Serial Communication > Stop");
        }

        if ((sp != null) && sp.getDisconnected().get()) {
            Platform.runLater(() -> {
                System.err.println(" >>> Disconnected from Device");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Connection Exception");
                alert.setHeaderText(null);
                alert.setContentText("Disconnected from Hardware.");
                alert.showAndWait();
            });
        }

        if (connectionConfig.port().isOpen()) {
            connectionConfig.port().closePort();
            System.out.println(" [Debug] Port Closed? " + connectionConfig.port().isOpen());
        }

        logger.info("\n[Debug] Stopping Data Transfer\n");
    }

    @FXML
    public void startingDataTransfer() {
        // todo conditional with new COnnectionManager
//        if (!readerRunnableManager.isRunning()) {
//            logger.error("Port not found");
//            Alert alert = new Alert(Alert.AlertType.ERROR);
//            alert.setTitle("Connection Exception");
//            alert.setHeaderText(null);
//            alert.setContentText("Hardware not connected to device. Serial Port disconnected and not found.");
//            alert.showAndWait();
//            return;
//        }
        readerRunnableManager.stop();

        // disable start button and enable stop button
        startDataTransfer.setDisable(true);
        stopDataTransfer.setDisable(false);

        for (int i = 3; i < graphPane.getTabs().size(); i++) {
            ((ScatterPlotController) graphPane.getTabs().get(i).getProperties().get("plot_controller")).reset();
        }

        tableView.getItems().removeAll(tableView.getItems());
        terminalTextArea.clear();

        logger.info("Serial Communication > Start");

//		activeTask = receiver.submit(sp);
//        SerialConnection serialConnection = new SerialConnection(
//                connectionConfig, data -> {
//        });
        readerRunnableManager.start();
        activeDataBase = database.submit(dpp);
        activeUIUpdater = uiUpdater.submit(() -> {
            logger.info("Grapher > Running UI Updater");
            LinkedBlockingQueue<DataPoint> rawArray = dpp.getParsedArray();
            while (!Thread.currentThread().isInterrupted()) {
                DataPoint dp;
                try {
                    dp = rawArray.take();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                Platform.runLater(() -> {
//					logger.info("Measurement Configs > " + Arrays.toString(dp.measurements()));
                    double[] measures = dp.measurements();
                    for (int i = 3; i < graphPane.getTabs().size(); i++) {
                        int measureIndex = i - 3;
                        ScatterPlotController spc = (ScatterPlotController) graphPane.getTabs().get(i).getProperties().get("plot_controller");
                        try {
                            spc.addData(dp.packetNum(), dp.time(), measures[measureIndex], measureIndex);
                        } catch (ArrayIndexOutOfBoundsException | NullPointerException _) {
                        }
                    }

                    tableView.getItems().add(dp);
                    if (sm.getAutoscrollTable())
                        tableView.scrollTo(tableView.getItems().size() - 1); // table autoscroll

                    if (sm.getAutoscrollTerminal())
                        terminalTextArea.appendText(dp.toRaw() + "\n"); // textarea autoscroll
                    else
                        terminalTextArea.setText(terminalTextArea.getText() + dp.toRaw() + "\n");
                });
            }
        });
        logger.info("Serial Communication > Serial and Database");
    }

    @FXML
    public void backHome() {
        stopingDataTransfer();
        logger.info("Serial Communication > Back Home");
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

            for (int i = 0; i < connectionConfig.sensors().size(); i++) {
                bufferedWriter.write(connectionConfig.sensors().get(i).name());
                bufferedWriter.write("(");
                bufferedWriter.write(connectionConfig.sensors().get(i).unit());
                bufferedWriter.write(")");
                if (i != connectionConfig.sensors().size() - 1) {
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
        logger.info("isFullScreen: " + stage.isFullScreen());

        if (!stage.isFullScreen()) {
            logger.info("Fullscreen: No");
            return;
        }

        logger.info("Fullscreen: Yes");

        double scrollWidth = graphPane.getWidth() - 24;
        double scrollHeight = graphPane.getHeight() - 24;
        logger.info("scrollWidth: " + scrollWidth + " scrollHeight: " + scrollHeight);

        double oldWidth = configScroll.getWidth() - 24;
        double oldHeight = configScroll.getHeight();
        logger.info("oldWidth: " + oldWidth + " oldWidth: " + oldHeight);

        oldWidth = configScroll.getPrefWidth() - 24;
        oldHeight = configScroll.getPrefHeight();
        logger.info("oldPrefWidth: " + oldWidth + " oldPrefWidth: " + oldHeight);

        stage.setFullScreen(true);

        double newWidth = configScroll.getWidth() - 24;
        double newHeight = (oldHeight * newWidth) / oldWidth;
        logger.info("newWidth: " + newWidth + " newHeight: " + newHeight);

        configVBox.setPrefWidth(newWidth);
        configVBox.setPrefHeight(newHeight);

//		if (StageManager.getMainStage().isFullScreen()) {
//
//			double scrollWidth = graphPane.getWidth() - 24;
//			double scrollHeight = graphPane.getHeight() - 24;
//			logger.info("scrollWidth: " + scrollWidth + " scrollHeight: " + scrollHeight);
//
//			spc.getScatterPlot().setPrefSize(scrollWidth, scrollHeight);
//			spc.getScatterPlot().setMinSize(scrollWidth, scrollHeight);
//
//			double oldWidth = spc.getScatterPlot().getWidth();
//			double oldHeight = spc.getScatterPlot().getHeight();
//			logger.info("oldWidth: " + oldWidth + " oldHeight: " + oldHeight);
//
//			oldWidth = spc.getScatterPlot().getPrefWidth() - 24;
//			oldHeight = spc.getScatterPlot().getPrefHeight();
//			logger.info("oldPrefWidth: " + oldWidth + " oldPrefWidth: " + oldHeight);
//
//			double newWidth = spc.getScrollPane().getWidth() - 24;
//			double newHeight = (oldHeight * newWidth) / oldWidth;
//			logger.info("newWidth: " + newWidth + " newHeight: " + newHeight);
//
//			spc.getScatterPlot().setPrefWidth(newWidth);
//			spc.getScatterPlot().setPrefHeight(newHeight);
//
//			spc.getScatterPlot().setMinWidth(newWidth);
//			spc.getScatterPlot().setMinHeight(newHeight);
//			logger.info();
//		}
    }
}