package com.alphagen.studio.float_data_recorder_2.data;

public class DataPoint {

    private double depth;
    private double temperature;
    private int packetId;
    private double time;

    public DataPoint(int packetId, double time, double depth) {
        this.packetId = packetId;
        this.depth = depth;
        this.time = time;
    }

    @Override
    public String toString() {
        return Constants.TEAM_DATA + packetId + "," + time + "," + depth;
    }

    public double getDepth() {
        return depth;
    }

    public double getTemperature() {
        return temperature;
    }

    public double getTime() {
        return time;
    }

    public int getPacketId() {
        return packetId;
    }
}
