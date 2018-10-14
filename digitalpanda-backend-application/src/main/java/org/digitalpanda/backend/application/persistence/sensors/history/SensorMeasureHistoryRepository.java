package org.digitalpanda.backend.application.persistence.sensors.history;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.CassandraOperations;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.Date;
import java.util.List;

@Repository
public class SensorMeasureHistoryRepository  {

    @Autowired
    private CassandraOperations cassandraTemplate; //Used for advanced queries

    @Autowired
    private SensorMeasureHistoryRepositoryCRUD sensorMeasureHistoryRepoCRUD; //Available for CRUD queries

    public SensorMeasureHistoryRepository() {

    }

    public List<SensorMeasureHistoryDao> saveAll(List<SensorMeasureHistoryDao> measuresToSave){
        return sensorMeasureHistoryRepoCRUD.saveAll(measuresToSave);
    }


    //TODO : getMeasuresAtLocation()
    public List<SensorMeasureHistoryDao> getMeasuresAtLocationWithInterval(String location, Date beginInc, Date endIncl) {
        return Collections.emptyList();
    }


}
