package org.digitalpanda.backend.data;

import java.util.List;
import java.util.Map;

public class SensorMeasures {

    private Map<SensorMeasureMetaData, List<SensorMeasure>> measures;

    public SensorMeasures(){this(null);}

    public SensorMeasures(Map<SensorMeasureMetaData, List<SensorMeasure>> measures) {
        this.measures = measures;
    }

    public Map<SensorMeasureMetaData, List<SensorMeasure>> getMeasures() { return measures; }
    public void setMeasures(Map<SensorMeasureMetaData, List<SensorMeasure>> measures) { this.measures = measures; }


    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        measures.forEach(
                (sensorMeasureMetaData, sensorMeasureList) -> {
                    sb.append(sensorMeasureMetaData + ":\n");
                    sensorMeasureList.forEach(
                            (sensorMeasure -> sb.append(" >" + sensorMeasure + "\n")));
                });
        sb.append("\n");
        return sb.toString();
    }
}
