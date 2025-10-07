package com.alphagen.studio.FloatDataVisualizer.data;

public class DataPoint {

    private final double depth;
    private final int packetId;
    private final double time;
    private final Settings settings = Settings.getInstance();
    private double temperature;

    public DataPoint(int packetId, double time, double depth) {
        this.packetId = packetId;
        this.depth = depth;
        this.time = time;
    }

    @Override
    public String toString() {
        return settings.TEAM_DATA + settings.getPacketName() + "-" + packetId + "," + time + "," + depth;
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
