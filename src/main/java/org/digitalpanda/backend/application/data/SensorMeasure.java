package org.digitalpanda.backend.application.data;

public class SensorMeasure {

    private final long timestamp;
    private final double measure;

    public SensorMeasure(long timestamp, double measure) {
        this.timestamp = timestamp;
        this.measure = measure;
    }

    public long getTimestamp() { return timestamp; }
    public double getMeasure() { return measure; }
}
