package org.digitalpanda.backend.data;

public class SensorMeasure implements Comparable{

    private long timestamp;
    private double measure;

    public SensorMeasure() { this(0L, 0.0); }
    public SensorMeasure(long timestamp, double measure) {
        this.timestamp = timestamp;
        this.measure = measure;
    }

    public long getTimestamp() { return timestamp; }
    public double getMeasure() { return measure; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
    public void setMeasure(double measure) { this.measure =  measure; }

    @Override
    public String toString(){
        return "timestamp=" + timestamp + ", measure=" + measure;
    }

    @Override
    public int compareTo(Object o) {
        if (!(o instanceof SensorMeasure)) return -1;
        SensorMeasure sensorMeasure = (SensorMeasure) o;
        return (int)(this.timestamp - sensorMeasure.timestamp);
    }
}
