package org.digitalpanda.backend.application.persistence.measure.history;

import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import java.util.Date;
import java.util.Objects;

@Table(SensorMeasureHistoryDao.SENSOR_MEASURE_HISTORY_TABLE_NAME) //Record max size rough estimation : 20 + 8 + 8 + 4 + 8 + 20 + 10 + 8 (86) Bytes
public class SensorMeasureHistoryDao {

    public static final String SENSOR_MEASURE_HISTORY_TABLE_NAME = "sensor_measure_history";
    public static final int SENSOR_MEASURE_DEFAULT_BUCKET_ID = 0;
    public static final int ROW_SIZE_BYTES = 86;

    @PrimaryKeyColumn(name = "location", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
    private String location;

    @PrimaryKeyColumn(name = "time_block_period_seconds", ordinal = 1, type = PrimaryKeyType.PARTITIONED)
    private Long timeBlockPeriodSeconds;

    @PrimaryKeyColumn(name = "time_block_id", ordinal = 2, type = PrimaryKeyType.PARTITIONED)
    private Long timeBlockId;

    @PrimaryKeyColumn(name = "measure_type", ordinal = 3, type = PrimaryKeyType.PARTITIONED)
    private String measureType;

    @PrimaryKeyColumn(name = "aggregate_type", ordinal = 4, type = PrimaryKeyType.PARTITIONED)
    private String aggregateType;

    @PrimaryKeyColumn(name = "bucket", ordinal = 5, type = PrimaryKeyType.PARTITIONED)
    private Integer bucket;

    @PrimaryKeyColumn(name = "timestamp", ordinal = 6, type = PrimaryKeyType.CLUSTERED)
    private Date timestamp;

    @Column
    private double value;

    public SensorMeasureHistoryDao() {
    }

    public SensorMeasureHistoryDao(String location, Long timeBlockPeriodSeconds, Long timeBlockId, String measureType, String aggregateType, Integer bucket, Date timestamp, double value) {
        this.location = location;
        this.timeBlockPeriodSeconds = timeBlockPeriodSeconds;
        this.timeBlockId = timeBlockId;
        this.measureType = measureType;
        this.aggregateType = aggregateType;
        this.bucket = bucket;
        this.timestamp = timestamp;
        this.value = value;
    }

    @Override
    public String toString() {
        return "SensorMeasureHistoryDao{" +
                "location='" + location + '\'' +
                ", timeBlockPeriodSeconds=" + timeBlockPeriodSeconds +
                ", timeBlockId=" + timeBlockId +
                ", measureType='" + measureType + '\'' +
                ", aggregateType='" + aggregateType + '\'' +
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

    public Long getTimeBlockPeriodSeconds() {
        return timeBlockPeriodSeconds;
    }

    public void setTimeBlockPeriodSeconds(Long timeBlockPeriodSeconds) {
        this.timeBlockPeriodSeconds = timeBlockPeriodSeconds;
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

    public String getAggregateType() {
        return aggregateType;
    }

    public void setAggregateType(String aggregateType) {
        this.aggregateType = aggregateType;
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
        SensorMeasureHistoryDao that = (SensorMeasureHistoryDao) o;
        return Double.compare(that.value, value) == 0 &&
                Objects.equals(location, that.location) &&
                Objects.equals(timeBlockPeriodSeconds, that.timeBlockPeriodSeconds) &&
                Objects.equals(timeBlockId, that.timeBlockId) &&
                Objects.equals(bucket, that.bucket) &&
                Objects.equals(timestamp, that.timestamp) &&
                Objects.equals(measureType, that.measureType) &&
                Objects.equals(aggregateType, that.aggregateType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(location, timeBlockPeriodSeconds, timeBlockId, bucket, timestamp, measureType, aggregateType, value);
    }

}
