module com.alphagen.studio.FloatDataVisualizer {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fazecast.jSerialComm;
    requires javafx.graphics;
    requires java.desktop;
    requires javafx.swing;
    requires static lombok;

    opens com.alphagen.studio.FloatDataVisualizer to javafx.fxml, javafx.controls;
    opens com.alphagen.studio.FloatDataVisualizer.buoyui to javafx.fxml, javafx.controls;
    opens com.alphagen.studio.FloatDataVisualizer.buoyui.frontend.pages.connections to javafx.fxml, javafx.controls;
    opens com.alphagen.studio.FloatDataVisualizer.buoyui.frontend.pages.connections.editor to javafx.fxml, javafx.controls;
    opens com.alphagen.studio.FloatDataVisualizer.buoyui.frontend.pages.connections.datacard to javafx.fxml, javafx.controls;
//    opens com.alphagen.studio.FloatDataVisualizer.buoyui.frontend.pages.connections.testing to javafx.fxml, javafx.controls;
    opens com.alphagen.studio.FloatDataVisualizer.buoyui.frontend.managers to javafx.fxml, javafx.controls;
    opens com.alphagen.studio.FloatDataVisualizer.buoyui.frontend.util to javafx.fxml, javafx.controls;
    opens com.alphagen.studio.FloatDataVisualizer.buoyui.lib.fxml to javafx.fxml, javafx.controls;

    exports com.alphagen.studio.FloatDataVisualizer;
    exports com.alphagen.studio.FloatDataVisualizer.buoyui;
    exports com.alphagen.studio.FloatDataVisualizer.buoyui.frontend.pages.connections;
    exports com.alphagen.studio.FloatDataVisualizer.buoyui.frontend.managers;
    exports com.alphagen.studio.FloatDataVisualizer.buoyui.frontend.util;
    exports com.alphagen.studio.FloatDataVisualizer.buoyui.lib.fxml;
    exports com.alphagen.studio.FloatDataVisualizer.buoyui.backend.data;
    exports com.alphagen.studio.FloatDataVisualizer.buoyui.backend.util;
    exports com.alphagen.studio.FloatDataVisualizer.buoyui.backend.constants;
    exports com.alphagen.studio.FloatDataVisualizer.buoyui.backend.processor;
    exports com.alphagen.studio.FloatDataVisualizer.buoyui.backend.app;
    exports com.alphagen.studio.FloatDataVisualizer.buoyui.frontend.pages;
    opens com.alphagen.studio.FloatDataVisualizer.buoyui.frontend.pages to javafx.controls, javafx.fxml;


//    opens com.alphagen.studio.FloatDataVisualizer.datareciever to com.fazecast.jSerialComm;

//    exports com.alphagen.studio.FloatDataVisualizer.datareciever;
//    exports com.alphagen.studio.FloatDataVisualizer.datarecorder;
//    exports com.alphagen.studio.FloatDataVisualizer.datawriter;
}