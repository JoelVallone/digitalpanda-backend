package org.digitalpanda.backend.application.persistence.sensors;

import org.springframework.data.cassandra.core.cql.Ordering;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import java.util.Objects;

@Table(SensorMeasureDao.SENSOR_MEASURE_TABLE_NAME)
public class SensorMeasureDao {

    public static final String SENSOR_MEASURE_TABLE_NAME = "sensor_measure";

    @PrimaryKeyColumn(name = "location", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
    private String location; //text

    @PrimaryKeyColumn(name = "day", ordinal = 1, type = PrimaryKeyType.PARTITIONED)
    private String day; //text

    @PrimaryKeyColumn(name = "bucket", ordinal = 2, type = PrimaryKeyType.PARTITIONED)
    private int bucket = 0; //int

    @PrimaryKeyColumn(name = "timestamp", ordinal = 2, type = PrimaryKeyType.CLUSTERED, ordering = Ordering.ASCENDING)
    private String timestamp; //timeuuid

    @Column
    private String measureType; //text

    @Column
    private double measureValue; //text

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public int getBucket() {
        return bucket;
    }

    public void setBucket(int bucket) {
        this.bucket = bucket;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getMeasureType() {
        return measureType;
    }

    public void setMeasureType(String measureType) {
        this.measureType = measureType;
    }

    public double getMeasureValue() {
        return measureValue;
    }

    public void setMeasureValue(double measureValue) {
        this.measureValue = measureValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SensorMeasureDao that = (SensorMeasureDao) o;
        return bucket == that.bucket &&
                Double.compare(that.measureValue, measureValue) == 0 &&
                Objects.equals(location, that.location) &&
                Objects.equals(day, that.day) &&
                Objects.equals(timestamp, that.timestamp) &&
                Objects.equals(measureType, that.measureType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(location, day, bucket, timestamp, measureType, measureValue);
    }
}
