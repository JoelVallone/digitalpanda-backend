package org.digitalpanda.backend.application.persistence;

import org.digitalpanda.backend.data.SensorMeasure;
import org.digitalpanda.backend.data.SensorMeasureMetaData;
import org.springframework.stereotype.Repository;

import java.util.HashMap;

@Repository
public class SensorMeasureRepository {
    private HashMap<SensorMeasureMetaData, SensorMeasure> measuresMap;

    public SensorMeasureRepository() {
        this.measuresMap = new HashMap<>();
    }

    public synchronized SensorMeasure getMeasure(SensorMeasureMetaData measureKey){
        return measuresMap.get(measureKey);
    }

    public synchronized void setMeasure(SensorMeasureMetaData measureKey, SensorMeasure sensorMeasure){
        measuresMap.put(measureKey, sensorMeasure);
    }

}
