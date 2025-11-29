package com.alphagen.studio.FloatDataVisualizer.datarecorder;

import com.alphagen.studio.FloatDataVisualizer.Launcher;
import com.alphagen.studio.FloatDataVisualizer.data.DataConfigurator;
import com.alphagen.studio.FloatDataVisualizer.data.DataKeeper;
import com.alphagen.studio.FloatDataVisualizer.data.DataPointRecord;
import com.alphagen.studio.FloatDataVisualizer.datawriter.DataWriter;
import com.alphagen.studio.FloatDataVisualizer.filepaths.DataPath;
import com.alphagen.studio.FloatDataVisualizer.filepaths.DataPathFactory;
import com.alphagen.studio.FloatDataVisualizer.log.Exitter;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.image.WritableImage;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class DataPlotter implements Exitter {

    // mi_edit = mi_export

    private final int SCATTER_CHART_PREF_WIDTH = 824;
    @FXML
    private MenuItem mi_close;
    @FXML
    private MenuItem mi_edit_scatterchart_fit_view;
    @FXML
    private MenuItem mi_edit_scatterchart_full_view;
    @FXML
    private MenuItem mi_edit_scatterchart_screenshot;
    @FXML
    private MenuItem mi_edit_tableview_screenshot;

    private int groupCount = 1;
    private double initialWidth;
    private ObservableList<XYChart.Series<Double, Double>> list;
    private int oldPacketNum = -1;
    // <Double, Double> : DataTypes of X-Axis and Y-Axis

    @FXML
    private ScatterChart<Double, Double> scatterChart;
    @FXML
    private NumberAxis xAxis;
    @FXML
    private NumberAxis yAxis;

    @FXML
    private TableView<DataPointRecord> tableView;
    @FXML
    private TableColumn<DataPointRecord, Double> tc_time;
    @FXML
    private TableColumn<DataPointRecord, Double> tc_depth;
    @FXML
    private TableColumn<DataPointRecord, Integer> tc_pkt;
    private DataConfigurator dataConfigurator;

    @FXML
    private MenuItem mi_edit_scatterchart_increase_width;
    @FXML
    private MenuItem mi_edit_scatterchart_decrease_width;
    @FXML
    private MenuItem mi_edit_tableview_increase_width;
    @FXML
    private MenuItem mi_edit_tableview_decrease_width;

    @FXML
    private MenuItem mi_export_data_raw_data;
    @FXML
    private MenuItem mi_export_data_csv_data;

    @FXML
    private MenuItem mi_help_filepath_app;
    @FXML
    private MenuItem mi_help_filepath_screenshots;
    @FXML
    private MenuItem mi_help_filepath_csvs;
    @FXML
    private MenuItem mi_help_filepath_settings_file_app_config;

    @FXML
    public void initialize() {

        DataPath dataPath = DataPathFactory.getFilePathFactory().getFilePath();

        dataConfigurator = DataConfigurator.getInstance();

        mi_edit_scatterchart_fit_view.setOnAction(event -> {
            scatterChart.setPrefWidth(SCATTER_CHART_PREF_WIDTH);
        });
        mi_edit_scatterchart_full_view.setOnAction(event -> {
            scatterChart.setPrefWidth(initialWidth * groupCount);
        });
        mi_edit_scatterchart_screenshot.setOnAction(event -> {
            int screenshotcount = 1;
            WritableImage writableImage = scatterChart.snapshot(new SnapshotParameters(), null);
            File file = null;
            do {
                file = new File(dataPath.getScatterPath() + "float_data_graph_screenshot_" + screenshotcount + ".png");
                screenshotcount++;
            } while (file.exists() && file.isFile());
            try {
                ImageIO.write(SwingFXUtils.fromFXImage(writableImage, null), "png", file);
                System.out.println("DATA: Screenshot saved at " + file.getAbsolutePath());
//                JOptionPane.showMessageDialog(null, "DATA: Screenshot saved to " + file.getAbsolutePath(), "DataImager", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                System.err.println("ERROR: Input error in Snapshot ScatterChart MenuItem.");
                JOptionPane.showMessageDialog(null, "Input error in Snapshot ScatterChart MenuItem.", "DataPlotter Exception", JOptionPane.ERROR_MESSAGE);
            }

        });

        mi_edit_tableview_screenshot.setOnAction(event -> {
            int screenshotcount = 1;
            WritableImage writableImage = tableView.snapshot(new SnapshotParameters(), null);
            File file = null;
            do {
                file = new File(dataPath.getTablePath() + "float_data_table_screenshot_" + screenshotcount + ".png");
                screenshotcount++;
            } while (file.exists() && file.isFile());

            try {
                ImageIO.write(SwingFXUtils.fromFXImage(writableImage, null), "png", file);
                System.out.println("DATA: Screenshot saved at " + file.getAbsolutePath());
//                JOptionPane.showMessageDialog(null, "DATA: Screenshot saved to " + file.getAbsolutePath(), "DataImager", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                System.err.println("ERROR: Input error in Snapshot TableView MenuItem.");
                JOptionPane.showMessageDialog(null, "Input error in Snapshot TableView MenuItem.", "DataPlotter Exception", JOptionPane.ERROR_MESSAGE);
            }

        });

        mi_export_data_raw_data.setOnAction(event -> {
            writeRawData();
        });

        mi_close.setOnAction(event -> {
            if (!Launcher.isDataReceiverAlive()) {
                Launcher.killDataReceiver();
                Launcher.killDataKeeper();
            }
            System.out.println("LOG: Closing DataRecorder");
            System.exit(0);
            exit(Exitter.NO_ERROR);
        });

        mi_help_filepath_app.setOnAction(event -> {
            showCustomCopyJOptionPane("App Filepath", DataPathFactory.getFilePathFactory().getFilePath().getBasepath());
        });

        mi_help_filepath_screenshots.setOnAction(event -> {
            showCustomCopyJOptionPane("Screenshot Filepath", DataPathFactory.getFilePathFactory().getFilePath().getScatterPath().replace("scatterchart/", ""));
        });

        mi_help_filepath_csvs.setOnAction(event -> {
            showCustomCopyJOptionPane("CSV Filepath", DataPathFactory.getFilePathFactory().getFilePath().getCSVPath().replace("/data/", "/data"));
        });

        mi_help_filepath_settings_file_app_config.setOnAction(event -> {
            showCustomCopyJOptionPane("App Config Filepath", DataPathFactory.getFilePathFactory().getFilePath().getSettingsPath());
        });

        xAxis = new NumberAxis();
        xAxis.setAutoRanging(false);
        xAxis.setLowerBound(0);
        xAxis.setUpperBound(5);

        yAxis = new NumberAxis();
        yAxis.setAutoRanging(false);

        // New Data Accessor
        tc_time.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().time()));
        tc_depth.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().depth()));
        tc_pkt.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().packetId()));

        list = scatterChart.getData();

//        scatterChart.getData().add(createDataGroup(0));
        scatterChart.setPrefWidth(SCATTER_CHART_PREF_WIDTH);
        scatterChart.requestLayout();

        initialWidth = scatterChart.getPrefWidth();
        tableView.setFixedCellSize(24);
        tableView.setPrefHeight((tableView.getFixedCellSize() + 5) * 24);

        mi_edit_tableview_increase_width.setDisable(true);
        mi_edit_tableview_decrease_width.setDisable(true);

        mi_edit_tableview_increase_width.setVisible(false);
        mi_edit_tableview_decrease_width.setVisible(false);

        tc_time.setText("Time (" + dataConfigurator.TIME_UNIT + ")");
        tc_depth.setText(dataConfigurator.UNIT2_NAME + " (" + dataConfigurator.UNIT2_UNIT + ")");
    }

    public XYChart.Series<Double, Double> createDataGroup(int groupId) {
        XYChart.Series<Double, Double> xyChartSeries = new XYChart.Series<>();
        xyChartSeries.setName(dataConfigurator.getDataGroupName() + groupId);
        return xyChartSeries;
    }

    public void writeDataPoint(DataPointRecord dataPointRecord) {

        // Addition: Hovering a datapoint shows vales
        XYChart.Data<Double, Double> data = new XYChart.Data<>(dataPointRecord.time(), dataPointRecord.depth());
        Platform.runLater(() -> {
            Node node = data.getNode();
            if (node != null) {
                Tooltip.install(node, new Tooltip("Time: " + data.getXValue() + dataConfigurator.TIME_UNIT + ", " + dataConfigurator.UNIT2_NAME + ": " + data.getYValue() + dataConfigurator.UNIT2_UNIT));
            } else {
                System.out.println("ERROR: DataPointUI is null!!");
            }
        });
        // end


        if (oldPacketNum == dataPointRecord.packetId()) {
            for (XYChart.Series<Double, Double> series : list) {
                if (series.getName().equals(dataConfigurator.getDataGroupName() + dataPointRecord.packetId())) {
                    series.getData().add(data);
                    tableView.getItems().add(dataPointRecord);
                    System.out.println("LOG: Data Group Found > " + dataPointRecord.packetId());
                }
            }
        } else {
            System.out.println("LOG: Data Group Created > " + dataPointRecord.packetId());
            XYChart.Series<Double, Double> xyChartSeries = createDataGroup(dataPointRecord.packetId());
            xyChartSeries.getData().add(data);
            tableView.getItems().add(dataPointRecord);
            scatterChart.getData().add(xyChartSeries);
            oldPacketNum = dataPointRecord.packetId();
            groupCount++;
        }

        if (tableView.getItems().size() > 23) {
            tableView.setPrefHeight((tableView.getFixedCellSize() + 1) * tableView.getItems().size());
        }
        tableView.requestLayout();

        scatterChart.setPrefWidth(initialWidth * groupCount);
        scatterChart.requestLayout();
    }

    @FXML
    public void increaseChartWidth() {
        scatterChart.setPrefWidth(scatterChart.getPrefWidth() * 1.2);
        System.out.println("LOG: Chart Width: " + scatterChart.getPrefWidth());
    }

    @FXML
    public void decreaseChartWidth() {
        scatterChart.setPrefWidth(scatterChart.getPrefWidth() * 0.8);
        System.out.println("LOG: Chart Width: " + scatterChart.getPrefWidth());
    }

    @FXML
    public void increaseTableHeight() {
    }

    @FXML
    public void decreaseTableHeight() {
    }

    public void closeApp() {
        if (!Launcher.isDataReceiverAlive()) {
            Launcher.killDataReceiver();
            Launcher.killDataKeeper();
        }
        System.out.println("LOG: Closing DataRecorder");
    }

    @FXML
    public void writeRawData() {

        File file = null;
        int i = 1;

        do {
            file = new File(DataPathFactory.getFilePathFactory().getFilePath().getCSVPath() + "rawData_" + i + ".csv");
            i++;
        } while (file.exists() && file.isFile());

        DataWriter dw = null;
        try {
            dw = new DataWriter(file.getAbsolutePath());
        } catch (IOException e) {
            DataPathFactory.getFilePathFactory().getFilePath().generateCSVFolder();
        }

//                HashMap<Integer, double[]> data = DataKeeper.getInstance().getAllData();
//                for(int pid : data.keySet()) { // pid -> packetID
//                    System.out.println(data.get(pid));
//                }

        try {
            dw.write("TeamData,Pkt#,Time,Depth");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        ArrayList<DataPointRecord> data = new ArrayList<>(DataKeeper.getInstance().getAllData());
        for (DataPointRecord dpr : data) {
            try {
                dw.writeRaw(dpr);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Unable to write DataPointRecord: " + dpr, "DataWriter Exception", JOptionPane.ERROR_MESSAGE);
            }
        }

        System.out.println("DATA: RAW/CSV Data saved at " + file.getAbsolutePath());
        dw.close();

    }

    @FXML
    public void writeCSVData() {

        File file = null;
        int i = 1;

        do {
            file = new File(DataPathFactory.getFilePathFactory().getFilePath().getCSVPath() + "csvData_" + i + ".csv");
            i++;
        } while (file.exists() && file.isFile());

        DataWriter dw = null;
        try {
            dw = new DataWriter(file.getAbsolutePath());
        } catch (IOException e) {
            DataPathFactory.getFilePathFactory().getFilePath().generateCSVFolder();
        }

//                HashMap<Integer, double[]> data = DataKeeper.getInstance().getAllData();
//                for(int pid : data.keySet()) { // pid -> packetID
//                    System.out.println(data.get(pid));
//                }

        try {
            dw.write("Time,Depth");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        ArrayList<DataPointRecord> data = new ArrayList<>(DataKeeper.getInstance().getAllData());
        for (DataPointRecord dpr : data) {
            try {
                dw.write(dpr);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Unable to write DataPointRecord: " + dpr, "DataWriter Exception", JOptionPane.ERROR_MESSAGE);
            }
        }

        System.out.println("DATA: RAW/CSV Data saved at " + file.getAbsolutePath());
        dw.close();

    }

    public void showCustomCopyJOptionPane(String title, String message) {
        System.out.println(message);

        final Clipboard clipboard = Clipboard.getSystemClipboard();

        Stage stage = new Stage();

        Label label = new Label(message);
        label.setAlignment(Pos.CENTER);
        label.setWrapText(true);

        BorderPane borderPane = new BorderPane();
        borderPane.setPrefSize(750.0, 150.0);
        borderPane.setCenter(label);

        HBox hBox = new HBox();
        hBox.setPrefSize(500.0, 50.0);
        borderPane.setBottom(hBox);
        hBox.setAlignment(Pos.CENTER);
        hBox.setSpacing(10.0);

        Button copyButton = new Button("Copy");
        copyButton.setPrefSize(100.0, 20.0);
        copyButton.setOnAction(event -> {
            final ClipboardContent clipboardContent = new ClipboardContent();
            clipboardContent.putString(message);
            clipboard.setContent(clipboardContent);
        });

        Button closeButton = new Button("Ok");
        closeButton.setPrefSize(100.0, 20.0);
        closeButton.setOnAction(event -> {
            stage.close();
        });

        hBox.getChildren().add(copyButton);
        hBox.getChildren().add(closeButton);

        stage.setScene(new Scene(borderPane));
        stage.setTitle(title);
        stage.show();
    }
}
