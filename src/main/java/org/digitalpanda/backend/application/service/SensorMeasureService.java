package org.digitalpanda.backend.application.service;

import org.digitalpanda.backend.application.data.SensorMeasure;
import org.digitalpanda.backend.application.data.SensorMeasureMetaData;

import java.util.HashMap;
import java.util.LinkedList;

public class SensorMeasureService {
    private HashMap<SensorMeasureMetaData, LinkedList<SensorMeasure>> measuresMap;

    public SensorMeasureService() {
        this.measuresMap = new HashMap<>();
    }

    public SensorMeasure geatLatestMeasure(SensorMeasureMetaData measureKey){
        if (measuresMap.containsKey(measureKey)) {
            measuresMap.get(measureKey).peek();
        } else {

        }
        return null;
    }



}
