package org.digitalpanda.backend.application.data;

import java.util.ArrayList;
import java.util.Map;
import java.util.PriorityQueue;

public class SensorMeasures {

    private final Map<SensorMeasureMetaData, ArrayList<SensorMeasure>> measures;

    public SensorMeasures(Map<SensorMeasureMetaData, ArrayList<SensorMeasure>> measures) {
        this.measures = measures;
    }

    public Map<SensorMeasureMetaData, ArrayList<SensorMeasure>> getMeasures() { return measures; }
}
