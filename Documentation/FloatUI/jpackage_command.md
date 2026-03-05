# Commands to Build Excecutable for Platforms

## Windows 11
```cmd
jpackage --input target --name FloatDataVisualizer_2_1_0 --main-jar FloatDataVisualizer-2.1.0.jar --main-class com.alphagen.studio.FloatDataVisualizer.Launcher --type app-image --icon D:\College\Clubs\Miramar_Engineering_Club\float_data_recorder_2_icon.ico --dest installer --app-version 2.0.0 --vendor MiramarWaterJets --verbose --module-path "C:\development\java\lib\javafx-sdk-23.0.1\lib;C:\development\java\lib\jSerialComm_v_2_11_2" --add-modules javafx.controls,javafx.fxml,javafx.graphics,java.desktop,javafx.swing,com.fazecast.jSerialComm --win-console --description "MateROV Float Data Visualizer" --java-options "-Dprism.order=sw -Dprism.verbose=true -Djava.library.path=C:\development\java\lib\javafx-sdk-23.0.1\bin"
```

## MacOS 11.7
```cmd
jpackage --input target --name FloatDataVisualizer_1_1_2 --main-jar FloatDataVisualizer-2.1.1.2.jar --main-class c
om.alphagen.studio.FloatDataVisualizer.Launcher --type dmg --dest installer --app-version 1.1.2 --vendor MiramarWaterJets --verbose --module-path "/Users/veerabhadram/Desktop/Aadarsh/Development/Java/java_libs/javafx-sdk-23.0.1/lib:/Users/veerabhadram/Desktop/Aadarsh/Development/Java/java_libs/jSerialComm/2_11_2" --add-modules javafx.controls,javafx.fxml,javafx.graphics,java.desktop,javafx.swing,com.fazecast.jSerialComm --description "MateROV Float Data Visualizer" --java-options "-Dprism.order=sw -Dprism.verbose=true -Djava.library.path=/Users/veerabhadram/Desktop/Aadarsh/Development/Java/java_libs/javafx-sdk-23.0.1/lib" --icon /Users/veerabhadram/IdeaProjects/FloatDataVisualizer/src/main/resources/com/alphagen/studio/FloatDataVisualizer/float_data_recorder_2_png_icon_2.icns 
```
