package com.alphagen.studio.float_data_recorder_2.datarecorder;

import com.alphagen.studio.float_data_recorder_2.Launcher;
import com.alphagen.studio.float_data_recorder_2.data.Constants;
import com.alphagen.studio.float_data_recorder_2.data.DataPoint;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.WritableImage;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class DataPlotter {

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
    private String user = System.getProperty("user.name");

    // <Double, Double> : DataTypes of X-Axis and Y-Axis
    @FXML
    private ScatterChart<Double, Double> scatterChart;
    @FXML
    private NumberAxis xAxis;
    @FXML
    private NumberAxis yAxis;
    @FXML
    private TableView<DataPoint> tableView;
    @FXML
    private TableColumn<DataPoint, Double> tc_time;
    @FXML
    private TableColumn<DataPoint, Double> tc_depth;
    @FXML
    private TableColumn<DataPoint, Integer> tc_pkt;

    @FXML
    public void initialize() {

        mi_edit_scatterchart_fit_view.setOnAction(event -> {
            scatterChart.setPrefWidth(824);
        });
        mi_edit_scatterchart_full_view.setOnAction(event -> {
            scatterChart.setPrefWidth(initialWidth * groupCount);
        });
        mi_edit_scatterchart_screenshot.setOnAction(event -> {
            int screenshotcount = 1;
            WritableImage writableImage = scatterChart.snapshot(new SnapshotParameters(), null);
            System.out.println(user);
            File file = new File("C:/Users/" + user + "/OneDrive/Desktop/float_data_graph_screenshot_" + screenshotcount + ".png");
            while (file.exists()) {
                screenshotcount++;
                file = new File("C:/Users/" + user + "/OneDrive/Desktop/float_data_graph_screenshot_" + screenshotcount + ".png");
            }
            try {
                ImageIO.write(SwingFXUtils.fromFXImage(writableImage, null), "png", file);
                System.out.println("DATA: Screenshot saved to " + file.getAbsolutePath());
            } catch (IOException e) {
                System.err.println("ERROR: Input error in Snapshot ScatterChart MenuItem.");
            }

        });
        mi_edit_tableview_screenshot.setOnAction(event -> {
            int screenshotcount = 1;
            WritableImage writableImage = tableView.snapshot(new SnapshotParameters(), null);
            File file = new File("C:/Users/" + user + "/OneDrive/Desktop/float_data_table_screenshot_" + screenshotcount + ".png");
            while (file.exists()) {
                screenshotcount++;
                file = new File("C:/Users/" + user + "/OneDrive/Desktop/float_data_table_screenshot_" + screenshotcount + ".png");
            }
            try {
                ImageIO.write(SwingFXUtils.fromFXImage(writableImage, null), "png", file);
                System.out.println("DATA: Screenshot saved to " + file.getAbsolutePath());
            } catch (IOException e) {
                System.err.println("ERROR: Input error in Snapshot TableView MenuItem.");
            }

        });
        mi_close.setOnAction(event -> {
            if (!Launcher.isDataReceiverAlive()) {
                Launcher.killDataReceiver();
                Launcher.killDataKeeper();
            }
            System.out.println("LOG: Closing DataRecorder");
            System.exit(0);
        });

        xAxis = new NumberAxis();
        xAxis.setAutoRanging(false);
        xAxis.setLowerBound(0);
        xAxis.setUpperBound(5);

        yAxis = new NumberAxis();
        yAxis.setAutoRanging(false);

        tc_time.setCellValueFactory(new PropertyValueFactory<>("time"));
        tc_depth.setCellValueFactory(new PropertyValueFactory<>("depth"));
        tc_pkt.setCellValueFactory(new PropertyValueFactory<>("packetId"));

        list = scatterChart.getData();

//        scatterChart.getData().add(createDataGroup(0));
        scatterChart.setPrefWidth(824);
        scatterChart.requestLayout();

        initialWidth = scatterChart.getPrefWidth();
        tableView.setFixedCellSize(24);
        tableView.setPrefHeight(tableView.getFixedCellSize() * 23);
    }

    public XYChart.Series<Double, Double> createDataGroup(int groupId) {
        XYChart.Series<Double, Double> xyChartSeries = new XYChart.Series<>();
        xyChartSeries.setName(Constants.DATA_GROUP_NAME + groupId);
        return xyChartSeries;
    }

    public void writeDataPoint(DataPoint dataPoint) {

        if (oldPacketNum == dataPoint.getPacketId()) {
            for (XYChart.Series<Double, Double> series : list) {
                if (series.getName().equals(Constants.DATA_GROUP_NAME + dataPoint.getPacketId())) {
                    series.getData().add(new XYChart.Data<>(dataPoint.getTime(), dataPoint.getDepth()));
                    tableView.getItems().add(dataPoint);
                    System.out.println("LOG: Data Group Found > " + dataPoint.getPacketId());
                }
            }
        } else {
            System.out.println("LOG: Data Group Created > " + dataPoint.getPacketId());
            XYChart.Series<Double, Double> xyChartSeries = createDataGroup(dataPoint.getPacketId());
            xyChartSeries.getData().add(new XYChart.Data<>(dataPoint.getTime(), dataPoint.getDepth()));
            tableView.getItems().add(dataPoint);
            scatterChart.getData().add(xyChartSeries);
            oldPacketNum = dataPoint.getPacketId();
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
    public void closeApp() {
        if (!Launcher.isDataReceiverAlive()) {
            Launcher.killDataReceiver();
            Launcher.killDataKeeper();
        }
        System.out.println("LOG: Closing DataRecorder");
    }
}
