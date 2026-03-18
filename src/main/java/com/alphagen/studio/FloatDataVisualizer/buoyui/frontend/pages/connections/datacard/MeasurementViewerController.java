package com.alphagen.studio.FloatDataVisualizer.buoyui.frontend.pages.connections.datacard;

import com.alphagen.studio.FloatDataVisualizer.buoyui.backend.data.ConnectionConfig;
import com.alphagen.studio.FloatDataVisualizer.buoyui.backend.data.MeasurementConfig;
import com.alphagen.studio.FloatDataVisualizer.buoyui.frontend.managers.StageManager;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

public class MeasurementViewerController {
	@FXML public TableView<MeasurementConfig> measurementTable;
	@FXML public TableColumn<MeasurementConfig, String> table_col_name;
	@FXML public TableColumn<MeasurementConfig, String> table_col_unit;

	public void setData(ConnectionConfig connectionConfig) {
		measurementTable.getItems().addAll(connectionConfig.measurementConfigs());
	}

	@FXML
	public void initialize() {
		table_col_name.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().name()));
		table_col_unit.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().unit()));
	}

	public void closeMeasurementViewer() {
		Stage stage = StageManager.getConnectionCreatorStage();
		stage.close();
	}
}