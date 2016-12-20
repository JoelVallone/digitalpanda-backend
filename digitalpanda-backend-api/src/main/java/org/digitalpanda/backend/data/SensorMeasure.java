package org.digitalpanda.backend.data;

public class SensorMeasure implements Comparable{

    private final long timestamp;
    private final double measure;

    public SensorMeasure(long timestamp, double measure) {
        this.timestamp = timestamp;
        this.measure = measure;
    }

    public long getTimestamp() { return timestamp; }
    public double getMeasure() { return measure; }

    @Override
    public int compareTo(Object o) {
        if (!(o instanceof SensorMeasure)) return -1;
        SensorMeasure sensorMeasure = (SensorMeasure) o;
        return (int)(this.timestamp - sensorMeasure.timestamp);
    }
}
