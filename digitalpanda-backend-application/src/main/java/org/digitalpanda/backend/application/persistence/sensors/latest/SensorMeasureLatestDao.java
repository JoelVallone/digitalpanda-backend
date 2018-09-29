package org.digitalpanda.backend.application.persistence.sensors.latest;

import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

@Table(SensorMeasureLatestDao.SENSOR_MEASURE_LATEST_TABLE_NAME) //Record max size rough estimation : 100 Bytes
public class SensorMeasureLatestDao {

    public static final String SENSOR_MEASURE_LATEST_TABLE_NAME = "sensor_measure_latest";
    public static final int SENSOR_MEASURE_DEFAULT_BUCKET_ID = 0;
    public static final SimpleDateFormat DATE_DAY = new SimpleDateFormat("yyyy-MM-dd");

    @PrimaryKeyColumn(name = "location", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
    private String location; //text

    @PrimaryKeyColumn(name = "day", ordinal = 1, type = PrimaryKeyType.PARTITIONED)
    private String measureType; //text

    @Column
    private Date timestamp; //time

    @Column
    private double measureValue; //text

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
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
        SensorMeasureLatestDao that = (SensorMeasureLatestDao) o;
        return Double.compare(that.measureValue, measureValue) == 0 &&
                Objects.equals(location, that.location) &&
                Objects.equals(timestamp, that.timestamp) &&
                Objects.equals(measureType, that.measureType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(location, timestamp, measureType, measureValue);
    }
}
