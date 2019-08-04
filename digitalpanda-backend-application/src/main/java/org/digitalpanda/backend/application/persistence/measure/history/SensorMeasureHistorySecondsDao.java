package org.digitalpanda.backend.application.persistence.measure.history;

import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import java.util.Date;
import java.util.Objects;

@Table(SensorMeasureHistorySecondsDao.SENSOR_MEASURE_HISTORY_TABLE_NAME) //Record max size rough estimation : 20 + 8 +  4 + 8 + 20 + 10 + 8 (78) Bytes
public class SensorMeasureHistorySecondsDao {

    public static final String SENSOR_MEASURE_HISTORY_TABLE_NAME = "sensor_measure_history_seconds";
    public static final int ROW_SIZE_BYTES = 86;

    @PrimaryKeyColumn(name = "location", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
    private String location;

    @PrimaryKeyColumn(name = "time_block_id", ordinal = 1, type = PrimaryKeyType.PARTITIONED)
    private Long timeBlockId;

    @PrimaryKeyColumn(name = "measure_type", ordinal = 2, type = PrimaryKeyType.PARTITIONED)
    private String measureType;

    @PrimaryKeyColumn(name = "bucket", ordinal = 3, type = PrimaryKeyType.PARTITIONED)
    private Integer bucket;

    @PrimaryKeyColumn(name = "timestamp", ordinal = 4, type = PrimaryKeyType.CLUSTERED)
    private Date timestamp;

    @Column
    private double value;

    public SensorMeasureHistorySecondsDao() {
    }

    public SensorMeasureHistorySecondsDao(String location, Long timeBlockId, String measureType, Integer bucket, Date timestamp, double value) {
        this.location = location;
        this.timeBlockId = timeBlockId;
        this.measureType = measureType;
        this.bucket = bucket;
        this.timestamp = timestamp;
        this.value = value;
    }

    @Override
    public String toString() {
        return "SensorMeasureHistorySecondsDao{" +
                "location='" + location + '\'' +
                ", timeBlockId=" + timeBlockId +
                ", measureType='" + measureType + '\'' +
                ", bucket=" + bucket +
                ", timestamp=" + timestamp +
                ", value=" + value +
                '}';
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Long getTimeBlockId() {
        return timeBlockId;
    }

    public void setTimeBlockId(Long timeBlockId) {
        this.timeBlockId = timeBlockId;
    }

    public Integer getBucket() {
        return bucket;
    }

    public void setBucket(Integer bucket) {
        this.bucket = bucket;
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

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SensorMeasureHistorySecondsDao that = (SensorMeasureHistorySecondsDao) o;
        return Double.compare(that.value, value) == 0 &&
                Objects.equals(location, that.location) &&
                Objects.equals(timeBlockId, that.timeBlockId) &&
                Objects.equals(bucket, that.bucket) &&
                Objects.equals(timestamp, that.timestamp) &&
                Objects.equals(measureType, that.measureType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(location, timeBlockId, bucket, timestamp, measureType, value);
    }

}
