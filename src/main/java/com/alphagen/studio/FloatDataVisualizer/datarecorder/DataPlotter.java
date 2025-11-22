package com.alphagen.studio.FloatDataVisualizer.datarecorder;

import com.alphagen.studio.FloatDataVisualizer.Launcher;
import com.alphagen.studio.FloatDataVisualizer.data.DataKeeper;
import com.alphagen.studio.FloatDataVisualizer.data.DataPointRecord;
import com.alphagen.studio.FloatDataVisualizer.data.Settings;
import com.alphagen.studio.FloatDataVisualizer.datawriter.DataWriter;
import com.alphagen.studio.FloatDataVisualizer.datawriter.DataWriterFactory;
import com.alphagen.studio.FloatDataVisualizer.filepaths.FilePath;
import com.alphagen.studio.FloatDataVisualizer.filepaths.FilePathFactory;
import com.alphagen.studio.FloatDataVisualizer.log.Exitter;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.WritableImage;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class DataPlotter implements Exitter {

    // mi_edit = mi_export

    private final int SCATTER_CHART_PREF_WIDTH = 824;
    @FXML private MenuItem mi_close;
    @FXML private MenuItem mi_edit_scatterchart_fit_view;
    @FXML private MenuItem mi_edit_scatterchart_full_view;
    @FXML private MenuItem mi_edit_scatterchart_screenshot;
    @FXML private MenuItem mi_edit_tableview_screenshot;

    private int groupCount = 1;
    private double initialWidth;
    private ObservableList<XYChart.Series<Double, Double>> list;
    private int oldPacketNum = -1;
    // <Double, Double> : DataTypes of X-Axis and Y-Axis

    @FXML private ScatterChart<Double, Double> scatterChart;
    @FXML private NumberAxis xAxis;
    @FXML private NumberAxis yAxis;

    @FXML private TableView<DataPointRecord> tableView;
    @FXML private TableColumn<DataPointRecord, Double> tc_time;
    @FXML private TableColumn<DataPointRecord, Double> tc_depth;
    @FXML private TableColumn<DataPointRecord, Integer> tc_pkt;
    private Settings settings;

    @FXML private MenuItem mi_edit_scatterchart_increase_width;
    @FXML private MenuItem mi_edit_scatterchart_decrease_width;
    @FXML private MenuItem mi_edit_tableview_increase_width;
    @FXML private MenuItem mi_edit_tableview_decrease_width;

    @FXML private MenuItem mi_export_data_raw_data;
    @FXML private MenuItem mi_export_data_csv_data;

    @FXML
    public void initialize() {

        FilePath filePath = FilePathFactory.getFilePathFactory().getFilePath();

        settings = Settings.getInstance();

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
                file = new File(filePath.getScatterPath() + "float_data_graph_screenshot_" + screenshotcount + ".png");
                screenshotcount++;
            } while (file.exists());
            try {
                ImageIO.write(SwingFXUtils.fromFXImage(writableImage, null), "png", file);
                System.out.println("DATA: Screenshot saved to " + file.getAbsolutePath());
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
                file = new File(filePath.getTablePath() + "float_data_table_screenshot_" + screenshotcount + ".png");
                screenshotcount++;
            } while (file.exists());

            try {
                ImageIO.write(SwingFXUtils.fromFXImage(writableImage, null), "png", file);
                System.out.println("DATA: Screenshot saved to " + file.getAbsolutePath());
            } catch (IOException e) {
                System.err.println("ERROR: Input error in Snapshot TableView MenuItem.");
                JOptionPane.showMessageDialog(null, "Input error in Snapshot TableView MenuItem.", "DataPlotter Exception", JOptionPane.ERROR_MESSAGE);
            }

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
        tableView.setPrefHeight(tableView.getFixedCellSize() * 23);

        mi_edit_tableview_increase_width.setDisable(true);
        mi_edit_tableview_decrease_width.setDisable(true);

        mi_edit_tableview_increase_width.setVisible(false);
        mi_edit_tableview_decrease_width.setVisible(false);

        tc_time.setText("Time (" + settings.TIME_UNIT + ")");
        tc_depth.setText(settings.UNIT2_NAME + " (" + settings.UNIT2_UNIT + ")");
    }

    public XYChart.Series<Double, Double> createDataGroup(int groupId) {
        XYChart.Series<Double, Double> xyChartSeries = new XYChart.Series<>();
        xyChartSeries.setName(settings.getDataGroupName() + groupId);
        return xyChartSeries;
    }

    public void writeDataPoint(DataPointRecord dataPointRecord) {

        if (oldPacketNum == dataPointRecord.packetId()) {
            for (XYChart.Series<Double, Double> series : list) {
                if (series.getName().equals(settings.getDataGroupName() + dataPointRecord.packetId())) {
                    series.getData().add(new XYChart.Data<>(dataPointRecord.time(), dataPointRecord.depth()));
                    tableView.getItems().add(dataPointRecord);
                    System.out.println("LOG: Data Group Found > " + dataPointRecord.packetId());
                }
            }
        } else {
            System.out.println("LOG: Data Group Created > " + dataPointRecord.packetId());
            XYChart.Series<Double, Double> xyChartSeries = createDataGroup(dataPointRecord.packetId());
            xyChartSeries.getData().add(new XYChart.Data<>(dataPointRecord.time(), dataPointRecord.depth()));
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

    public void writeRawData() {



            Thread dataWriter_raw = new Thread(() -> {

                File file = null;
                int i = 1;

                do {
                    file = new File(FilePathFactory.getFilePathFactory().getFilePath().getCSVPath() + "rawData_"+i+".csv");
                    i++;
                } while(file.exists() && file.isFile());

                DataWriter dw = null;
                try {
                    dw = new DataWriter(file.getAbsolutePath());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                HashMap<Integer, double[]> data = DataKeeper.getInstance().getAllData();
                for(int pid : data.keySet()) { // pid -> packetID
                    System.out.println(data.get(pid));
                }

    //            ArrayList<DataPointRecord> data = new ArrayList<>(DataKeeper.getInstance().getAllData());
    //            for (DataPointRecord dpr : data) {
    //                try {
    //                    dw.writeRaw(dpr);
    //                } catch (IOException e) {
    //                    JOptionPane.showMessageDialog(null, "Unable to write DataPointRecord: " + dpr.toString(), "DataWriter Exception", JOptionPane.ERROR_MESSAGE);
    //                }
    //            }

            });
            dataWriter_raw.setDaemon(true);
            dataWriter_raw.setName("DataWriter_RAW");
            dataWriter_raw.start();
    }

    public void writeCSVData() {
        Thread dataWriter_csv = new Thread(()-> {
            // code
        });
        dataWriter_csv.setName("DataWriter_CSV");
    }
}
