package org.digitalpanda.backend.application.northbound.service;

import java.util.List;
import java.util.Objects;

public class SensorMeasuresEquidistributed {

    private long startTimeMillisIncl;
    private long endTimeMillisIncl;
    private long targetPeriodMillis;
    private List<Double> equidistributedValues;

    public SensorMeasuresEquidistributed() {
    }

    public SensorMeasuresEquidistributed(long startTimeMillisIncl, long endTimeMillisIncl, long targetPeriodMillis, List<Double> equidistributedValues) {
        this.startTimeMillisIncl = startTimeMillisIncl;
        this.endTimeMillisIncl = endTimeMillisIncl;
        this.targetPeriodMillis = targetPeriodMillis;
        this.equidistributedValues = equidistributedValues;
    }

    public long getStartTimeMillisIncl() {
        return startTimeMillisIncl;
    }

    public void setStartTimeMillisIncl(long startTimeMillisIncl) {
        this.startTimeMillisIncl = startTimeMillisIncl;
    }

    public long getEndTimeMillisIncl() {
        return endTimeMillisIncl;
    }

    public void setEndTimeMillisIncl(long endTimeMillisIncl) {
        this.endTimeMillisIncl = endTimeMillisIncl;
    }

    public long getTargetPeriodMillis() {
        return targetPeriodMillis;
    }

    public void setTargetPeriodMillis(long targetPeriodMillis) {
        this.targetPeriodMillis = targetPeriodMillis;
    }

    public List<Double> getEquidistributedValues() {
        return equidistributedValues;
    }

    public void setEquidistributedValues(List<Double> equidistributedValues) {
        this.equidistributedValues = equidistributedValues;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SensorMeasuresEquidistributed that = (SensorMeasuresEquidistributed) o;
        return startTimeMillisIncl == that.startTimeMillisIncl &&
                endTimeMillisIncl == that.endTimeMillisIncl &&
                targetPeriodMillis == that.targetPeriodMillis &&
                Objects.equals(equidistributedValues, that.equidistributedValues);
    }

    @Override
    public int hashCode() {
        return Objects.hash(startTimeMillisIncl, endTimeMillisIncl, targetPeriodMillis, equidistributedValues);
    }

    @Override
    public String toString() {
        return "SensorMeasuresEquidistributed{" +
                "startTimeMillisIncl=" + startTimeMillisIncl +
                ", endTimeMillisIncl=" + endTimeMillisIncl +
                ", targetPeriodMillis=" + targetPeriodMillis +
                ", equidistributedValues=" + equidistributedValues +
                '}';
    }
}
