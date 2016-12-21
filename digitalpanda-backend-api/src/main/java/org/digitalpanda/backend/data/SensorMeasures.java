package org.digitalpanda.backend.data;

import java.util.List;
import java.util.Map;

public class SensorMeasures {

    private final Map<SensorMeasureMetaData, List<SensorMeasure>> measures;

    public SensorMeasures(Map<SensorMeasureMetaData, List<SensorMeasure>> measures) {
        this.measures = measures;
    }

    public Map<SensorMeasureMetaData, List<SensorMeasure>> getMeasures() { return measures; }
}
