package org.digitalpanda.backend.application.northbound.ressource.measure;


import java.util.List;
import java.util.Objects;

public class SensorMeasuresDTO {

    private Long startTimeMillisIncl;
    private Long endTimeMillisIncl;
    private Long timeMillisBetweenDataPoints;
    private String location;
    private String type;
    private List<Double> values;


    public SensorMeasuresDTO(Long startTimeMillisIncl, Long endTimeMillisIncl, Long timeMillisBetweenDataPoints, String location, String type, List<Double> values) {
        this.startTimeMillisIncl = startTimeMillisIncl;
        this.endTimeMillisIncl = endTimeMillisIncl;
        this.timeMillisBetweenDataPoints = timeMillisBetweenDataPoints;
        this.location = location;
        this.type = type;
        this.values = values;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SensorMeasuresDTO that = (SensorMeasuresDTO) o;
        return Objects.equals(startTimeMillisIncl, that.startTimeMillisIncl) &&
                Objects.equals(endTimeMillisIncl, that.endTimeMillisIncl) &&
                Objects.equals(timeMillisBetweenDataPoints, that.timeMillisBetweenDataPoints) &&
                Objects.equals(location, that.location) &&
                Objects.equals(type, that.type) &&
                Objects.equals(values, that.values);
    }

    @Override
    public int hashCode() {
        return Objects.hash(startTimeMillisIncl, endTimeMillisIncl, timeMillisBetweenDataPoints, location, type, values);
    }

    @Override
    public String toString() {
        return "SensorMeasuresDTO{" +
                "startTimeMillisIncl='" + startTimeMillisIncl + '\'' +
                ", endTimeMillisIncl='" + endTimeMillisIncl + '\'' +
                ", timeMillisBetweenDataPoints=" + timeMillisBetweenDataPoints +
                ", location='" + location + '\'' +
                ", type=" + type +
                ", values=" + values +
                '}';
    }
}
