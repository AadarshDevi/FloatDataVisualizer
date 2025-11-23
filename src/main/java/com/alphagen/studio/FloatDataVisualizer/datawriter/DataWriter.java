package com.alphagen.studio.FloatDataVisualizer.datawriter;

import com.alphagen.studio.FloatDataVisualizer.data.DataPointRecord;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class DataWriter {

    //    private final File datafile;
    private final BufferedWriter bw;

    public DataWriter(String filepath) throws IOException {
//         datafile = new File(filepath);
        bw = new BufferedWriter(new PrintWriter(new FileWriter(filepath)));
    }

    public void writeRaw(DataPointRecord dpr) throws IOException {
        bw.write(dpr.toString());
        bw.newLine();
    }

    public void write(DataPointRecord dpr) throws IOException {
        bw.write(dpr.time() + ",");
        bw.write(Double.toString(dpr.depth()));
        bw.newLine();
    }

    public void write(String string) throws IOException {
        bw.write(string);
        bw.newLine();
    }


    public boolean close() {
        try {
            bw.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

//    public String getAbsFilePath() {
//        return datafile.getAbsolutePath();
//    }
}
