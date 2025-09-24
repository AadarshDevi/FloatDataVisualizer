module com.alphagen.studio.float_data_recorder_2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fazecast.jSerialComm;
    requires javafx.graphics;
    requires java.desktop;
    requires javafx.swing;

    opens com.alphagen.studio.float_data_recorder_2.datarecorder to javafx.fxml;
    opens com.alphagen.studio.float_data_recorder_2.datareciever to com.fazecast.jSerialComm;
    exports com.alphagen.studio.float_data_recorder_2;
    exports com.alphagen.studio.float_data_recorder_2.datarecorder;
    exports com.alphagen.studio.float_data_recorder_2.data;
}