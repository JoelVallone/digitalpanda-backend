package org.digitalpanda.backend.application.persistence;

import org.digitalpanda.backend.data.SensorMeasure;
import org.digitalpanda.backend.data.SensorMeasureMetaData;
import org.digitalpanda.backend.data.SensorMeasureType;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Repository
public class SensorMeasureRepository {
    private HashMap<SensorMeasureMetaData, SensorMeasure> measuresMap;

    public SensorMeasureRepository() {
        this.measuresMap = new HashMap<>();
    }

    public synchronized SensorMeasure getMeasure(SensorMeasureMetaData measureKey){

        System.out.print("repository.get : ");
        SensorMeasure sensorMeasure = measuresMap.get(measureKey);
        //SensorMeasure sensorMeasure = generateDummyMeasure(measureKey);
        if(sensorMeasure != null){
            System.out.println(measureKey + ", " + sensorMeasure);
        }else{
            System.out.println(measureKey + "=> no data");
        }
        return sensorMeasure;
    }

    public synchronized List<SensorMeasureMetaData> getKeys(){
        System.out.println("repository.getKeys : ");
        return new ArrayList<>(measuresMap.keySet());
        //return getDummyMeasureKeys();
    }

    private List<SensorMeasureMetaData> getDummyMeasureKeys(){
        List<SensorMeasureMetaData> keyList = new ArrayList<>();
        keyList.add(new SensorMeasureMetaData("indoor", SensorMeasureType.TEMPERATURE));
        keyList.add(new SensorMeasureMetaData("indoor", SensorMeasureType.PRESSURE));
        keyList.add(new SensorMeasureMetaData("outdoor", SensorMeasureType.TEMPERATURE));
        return keyList;
    }
    private SensorMeasure generateDummyMeasure(SensorMeasureMetaData measureKey) {
        return new SensorMeasure(System.currentTimeMillis(), Math.random());
    }

    public synchronized void setMeasure(SensorMeasureMetaData measureKey, SensorMeasure sensorMeasure){
        System.out.println("repository.set : " + measureKey + " => " + sensorMeasure);
        measuresMap.put(measureKey, sensorMeasure);
    }

}
