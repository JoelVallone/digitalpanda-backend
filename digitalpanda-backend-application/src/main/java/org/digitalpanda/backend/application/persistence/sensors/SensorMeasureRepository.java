package org.digitalpanda.backend.application.persistence.sensors;

import org.digitalpanda.backend.application.persistence.sensors.latest.SensorMeasureLatestCassandraRepository;
import org.digitalpanda.backend.application.persistence.sensors.latest.SensorMeasureLatestDao;
import org.digitalpanda.backend.data.SensorMeasure;
import org.digitalpanda.backend.data.SensorMeasureMetaData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.CassandraOperations;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/*
 https://docs.spring.io/spring-data/cassandra/docs/2.0.9.RELEASE/reference/html/
 https://www.baeldung.com/spring-data-cassandratemplate-cqltemplate
    See query derivation,SensorMeasureRepository
 */
//FIXME: Write integration test with embedded cassandra
@Repository
public class SensorMeasureRepository {

    @Autowired
    private CassandraOperations cassandraTemplate; //Used for advanced queries

    @Autowired
    private SensorMeasureLatestCassandraRepository sensorMeasureLatestCassandraRepository; //Available for CRUD queries

    private Map<SensorMeasureMetaData, SensorMeasure> latestMeasures;

    public SensorMeasureRepository() {
        this.latestMeasures = new ConcurrentHashMap<>();
        updateCache();
    }


    //TODO: second-periodic sensor measure cache update => Allows sensor value read scale-out
    public SensorMeasure getLatestMeasure(SensorMeasureMetaData measureKey) {
        System.out.print("repository.get : ");
        SensorMeasure sensorMeasure = latestMeasures.get(measureKey);
        if(sensorMeasure != null){
            System.out.println(measureKey + ", " + sensorMeasure);
        }else{
            System.out.println(measureKey + "=> no data");
        }
        return sensorMeasure;
    }

    public List<SensorMeasureMetaData> getKeys(){
        System.out.println("repository.getKeys : ");
        return new ArrayList<>(latestMeasures.keySet());
    }

    void flushCache() {
        latestMeasures = new ConcurrentHashMap<>();
    }

    public void setMeasure(SensorMeasureMetaData measureKey, SensorMeasure sensorMeasure){
        System.out.println("repository.set : " + measureKey + " => " + sensorMeasure);

        latestMeasures.put(measureKey, sensorMeasure);

        //TODO: Async update to latest measure table and append to history table
        sensorMeasureLatestCassandraRepository.save(
                SensorMeasureDaoHelper.toLatestMeasureDao(measureKey, sensorMeasure));
    }

    //TODO: second-periodic sensor measure cache update => Allows sensor value read scale-out
    public void updateCache(){
        //FIXME : Warm-up in-memory cache by calling:
        // SELECT * FROM sensor_measure_latest WHERE location = ??? AND measureType = ???
    }

    //TODO : getMeasuresAtLocation()
    public List<SensorMeasureLatestDao> getMeasuresAtLocation(String location, Date beginInc, Date endExcl) {
        return Collections.emptyList();
    }
}

/**
 * Measure as time series:  location, type, timestamp, value
 * > ssh pi@192.168.0.211
 * data format (csv):
 * ">,Time[ms],Percentage[%],Hecto-Pascal[hPa],Degree Celcius[°C]"
 * ">,1535718586193,80.75,968.9,17.45"
 *
 * measureType, measureValue, time, location
 *
 *Typical request:
 *  -> Latest values
 *  -> interval of values  at second, minute, hour, day precisions/average
 * Cassandra table layout :
 *
 * //Query all buckets to scale when input to read is too big per partition => combine DB data in the backend code
 * 100 Bytes per record, 1 record each second, n measureType => n*8.25 MB
 * CREATE TABLE  sensor_measure (
 *     location text,
 *     day text,
 *     bucket int,
 *     measureType text,
 *     ts timestamp,
 *     measureValue float,
 *     primary key((location, day, bucket), ts)
 * ) WITH CLUSTERING ORDER BY (ts DESC)
 *          AND COMPACTION = {'class': 'TimeWindowCompactionStrategy',
 *                        'compaction_window_unit': 'DAYS',
 *                        'compaction_window_size': 1};
 *
 *   pond.js => time series processing
 *   react-time series => time series plot
 *
 *   ==> If 5 sensors with 2 measureType each
 *      => partition size (1 bucket) 16.5 MB
 *      => Total data per day: 82.5 MB
 *      => Total data per year: ~30 GB
 */
