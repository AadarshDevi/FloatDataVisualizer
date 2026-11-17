package com.alphagen.studio.FloatDataVisualizer.core.data;

/**
 * The name of the data being measured. Used for the frontend
 * <p>
 * Ex: time (s), depth (m)
 *
 * @param name name of the data recorded
 * @param unit units of the data recorded
 */
public record SensorUnit(
        String name,
        String unit
) {
    @Override
    public String toString() {
        return name + " (" + unit + ")";
    }
}
