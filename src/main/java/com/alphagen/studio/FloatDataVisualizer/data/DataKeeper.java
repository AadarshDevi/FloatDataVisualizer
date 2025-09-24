package com.alphagen.studio.FloatDataVisualizer.data;

import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;

public class DataKeeper implements Runnable {

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
    private final LinkedBlockingQueue<DataPoint> dataPointLinkedBlockingQueue;

    /**
     * Data that is read is then moved to this list. This is the permanent
     * data list
     *
     * @see LinkedBlockingQueue
     */
    private final ArrayList<DataPoint> permanentDataPointArrayList;
    private boolean running = true;


    /**
     * The constructor that creates the 2 import thread-safe lists
     */
    public DataKeeper() {
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
     * Converts a raw data string into a DataPoint obj
     *
     * @param stringData raw string information
     * @return DataPoint obj
     * @see DataPoint
     */
    private DataPoint getDataPoint(String stringData) {
        String[] dataArray = stringData.split(",");
        int packetId = Integer.parseInt(dataArray[1].substring(dataArray[1].indexOf("-") + 1));
        double time = Double.parseDouble(dataArray[2]);
        double depth = Double.parseDouble(dataArray[3]);
        return new DataPoint(packetId, time, depth);
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
            throw new RuntimeException(e);
        }

    }

    public DataPoint getDataPoint() {
        return dataPointLinkedBlockingQueue.poll();
    }

    /**
     * Gets the arraylist that has all the data
     *
     * @return list of all DataPoints
     * @see DataPoint
     */
    public ArrayList<DataPoint> getAllData() {
        return permanentDataPointArrayList;
    }

    public void stop() {
        running = false;
    }

    /**
     * Whenever there is data in raw data list, it takes the data and converts it into DataPoints
     * and puts it in the data list
     *
     * @see DataPoint
     * @see LinkedBlockingQueue
     */
    @Override
    public void run() {
        while (running) {
            if (!rawDataPointLinkedBlockingQueue.isEmpty()) {
                String dataline = rawDataPointLinkedBlockingQueue.poll();
                if (dataline != null) {
                    DataPoint dataPoint = getDataPoint(dataline);
                    try {
                        dataPointLinkedBlockingQueue.put(dataPoint);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    System.out.println("LOG: Data added");
                }
            }
        }
    }
}
