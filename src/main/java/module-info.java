module com.alphagen.studio.FloatDataVisualizer {
	requires javafx.controls;
	requires javafx.fxml;
	requires com.fazecast.jSerialComm;
	requires javafx.graphics;
	requires java.desktop;
	requires javafx.swing;
	requires static lombok;

	opens com.alphagen.studio.FloatDataVisualizer to javafx.controls, javafx.fxml;
	opens com.alphagen.studio.FloatDataVisualizer.buoyui.connections to javafx.controls, javafx.fxml;
//    opens com.alphagen.studio.FloatDataVisualizer.datarecorder to javafx.fxml;
//    opens com.alphagen.studio.FloatDataVisualizer.datareciever to com.fazecast.jSerialComm;

	exports com.alphagen.studio.FloatDataVisualizer;
	exports com.alphagen.studio.FloatDataVisualizer.buoyui.connections;
	//    exports com.alphagen.studio.FloatDataVisualizer.data;
//    exports com.alphagen.studio.FloatDataVisualizer.data.util;
//    exports com.alphagen.studio.FloatDataVisualizer.datareciever;
//    exports com.alphagen.studio.FloatDataVisualizer.datarecorder;
//    exports com.alphagen.studio.FloatDataVisualizer.datawriter;
//    exports com.alphagen.studio.FloatDataVisualizer.filepaths;
//    exports com.alphagen.studio.FloatDataVisualizer.log;
}