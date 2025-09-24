module com.alphagen.studio.FloatDataVisualizer {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fazecast.jSerialComm;
    requires javafx.graphics;
    requires java.desktop;
    requires javafx.swing;

    opens com.alphagen.studio.FloatDataVisualizer.datarecorder to javafx.fxml;
    opens com.alphagen.studio.FloatDataVisualizer.datareciever to com.fazecast.jSerialComm;
    exports com.alphagen.studio.FloatDataVisualizer;
    exports com.alphagen.studio.FloatDataVisualizer.datarecorder;
    exports com.alphagen.studio.FloatDataVisualizer.data;
}