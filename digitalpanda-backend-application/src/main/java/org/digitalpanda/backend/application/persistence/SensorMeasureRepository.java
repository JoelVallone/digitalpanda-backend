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

        System.out.print("repository.get : ");
        SensorMeasure sensorMeasure = measuresMap.get(measureKey);
        if(sensorMeasure != null){
            System.out.println(measureKey + ", " + sensorMeasure);
        }else{
            System.out.println(measureKey + "=> no data");
        }
        return sensorMeasure;
    }

    public synchronized void setMeasure(SensorMeasureMetaData measureKey, SensorMeasure sensorMeasure){
        System.out.println("repository.set : " + measureKey + " => " + sensorMeasure);
        measuresMap.put(measureKey, sensorMeasure);
    }

}
