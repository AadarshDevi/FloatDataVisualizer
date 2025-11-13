package com.alphagen.studio.FloatDataVisualizer.data;

import com.alphagen.studio.FloatDataVisualizer.datawriter.DataWriter;
import com.alphagen.studio.FloatDataVisualizer.filepaths.FilePathFactory;
import com.alphagen.studio.FloatDataVisualizer.log.Exitter;

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;

public class DataKeeper implements Runnable, Exitter {

    /**
     * The only instance of the DataKeeper
     */
    private static DataKeeper dk;

    /**
     * All raw string data is stored here before being converted.
     */
    private final LinkedBlockingQueue<String> rawDataPointLinkedBlockingQueue;
    /**
     * All information that is sent to the keeper is stored in this obj. This is
     * the temporary data list.
     *
     * @see LinkedBlockingQueue
     */
    private final LinkedBlockingQueue<DataPointRecord> dataPointLinkedBlockingQueue;

    /**
     * Data that is read is then moved to this list. This is the permanent
     * data list
     *
     * @see LinkedBlockingQueue
     */
    private final ArrayList<DataPointRecord> permanentDataPointArrayList;
    private boolean running = true;
    private final Settings settings;

    private final DataWriter dw;

    /**
     * The constructor that creates the 2 import thread-safe lists
     */
    public DataKeeper() {
        settings = Settings.getInstance();
        if(settings.WRITE_CSV == true) {
            try {
                dw = new DataWriter(FilePathFactory.getFilePathFactory().getFilePath().getCSVPath());
            } catch (IOException e) {
                exit("Unable to create DataWriter.");
                throw new RuntimeException(e);
            }
        } else {
            dw = null;
        }
        rawDataPointLinkedBlockingQueue = new LinkedBlockingQueue<>();
        dataPointLinkedBlockingQueue = new LinkedBlockingQueue<>();
        permanentDataPointArrayList = new ArrayList<>();
    }

    /**
     * the method gives a DataKeeper object and by using this method, there
     * will be only 1 instance of this class.
     *
     * @return DataKeeper
     */
    public static DataKeeper getInstance() {
        if (dk == null) dk = new DataKeeper();
        return dk;
    }

    /**
     * Converts a raw data string into a DataPointRecord obj
     *
     * @param stringData raw string information
     * @return DataPointRecord obj
     * @see DataPointRecord
     */
    private DataPointRecord createDataPointRecord(String stringData) {
        String[] dataArray = stringData.split(",");
        int packetId = Integer.parseInt(dataArray[1].substring(dataArray[1].indexOf("-") + 1));
        double time = Double.parseDouble(dataArray[settings.getTimeIndex()]);
        double unit2 = Double.parseDouble(dataArray[settings.getUNIT2Index()]);
//        double depth = Double.parseDouble(dataArray[settings.getDepthIndex()]);
        return new DataPointRecord(packetId, time, unit2);
//        return new DataPointRecord(packetId, time, depth);
    }

    /**
     * Adds data to temporary data list
     *
     * @param dataline raw string data
     */
    public void writeData(String dataline) {

        try {
            rawDataPointLinkedBlockingQueue.put(dataline);
        } catch (InterruptedException e) {
            JOptionPane.showMessageDialog(null, "Unable to \"put\" Float DataPoint.", "DataKeeper Exception", JOptionPane.ERROR_MESSAGE);
            throw new RuntimeException(e);
        }

    }

    public DataPointRecord getDataPointRecord() {
        // TODO: take DPR, write to file, add to permanentDataPointArrayList, then return.
        DataPointRecord dpr = dataPointLinkedBlockingQueue.poll();
        permanentDataPointArrayList.add(dpr);
        try {
            if(settings.WRITE_CSV) {
                if(settings.WRITE_RAW) {
                    dw.writeRaw(dpr);
                } else {
                    dw.write(dpr);
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Unable to DataPointRecord:\n" + dpr, "DataKeeper Exception", JOptionPane.ERROR_MESSAGE);
            throw new RuntimeException(e);
        }
        return dpr;
    }

    /**
     * Gets the arraylist that has all the data
     *
     * @return list of all DataPoints
     * @see DataPointRecord
     */
    public ArrayList<DataPointRecord> getAllData() {
        return permanentDataPointArrayList;
    }

    public void stop() {
        running = false;
        dw.close();
    }

    /**
     * Whenever there is data in raw data list, it takes the data and converts it into DataPoints
     * and puts it in the data list
     *
     * @see DataPointRecord
     * @see LinkedBlockingQueue
     */
    @Override
    public void run() {
        while (running) {
            if (!rawDataPointLinkedBlockingQueue.isEmpty()) {
                String dataline = rawDataPointLinkedBlockingQueue.poll();
                if (dataline != null) {
                    DataPointRecord dataPointRecord = createDataPointRecord(dataline);
                    try {
                        dataPointLinkedBlockingQueue.put(dataPointRecord);
                    } catch (InterruptedException e) {
                        JOptionPane.showMessageDialog(null, "Unable to \"put\" Float DataPoint.", "DataKeeper Exception", JOptionPane.ERROR_MESSAGE);
                        throw new RuntimeException(e);
                    }
                    System.out.println("LOG: Data added");
                }
            }
        }
    }
}
