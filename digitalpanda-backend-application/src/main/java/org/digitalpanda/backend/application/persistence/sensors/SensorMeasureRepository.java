package org.digitalpanda.backend.application.persistence.sensors;

import org.digitalpanda.backend.data.SensorMeasure;
import org.digitalpanda.backend.data.SensorMeasureMetaData;
import org.digitalpanda.backend.data.SensorMeasureType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.CassandraOperations;
import org.springframework.data.cassandra.core.mapping.MapId;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.MapIdCassandraRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

/*
 https://docs.spring.io/spring-data/cassandra/docs/2.0.9.RELEASE/reference/html/
 https://www.baeldung.com/spring-data-cassandratemplate-cqltemplate
    See query derivation,
 */

@Repository
public class SensorMeasureRepository {


    //FIXME: Integrate SensorMeasureRepository with Cassandra DB
    @Autowired
    private CassandraOperations cassandraTemplate; //Used for advanced queries
    @Autowired
    private SensorMeasureCassandraRepository sensorMeasureCassandraRepository; //used for CRUD queries

    private HashMap<SensorMeasureMetaData, SensorMeasure> latestMeasures;

    public SensorMeasureRepository() {
        this.latestMeasures = new HashMap<>();
    }

    public synchronized SensorMeasure getMeasure(SensorMeasureMetaData measureKey){

        System.out.print("repository.get : ");
        SensorMeasure sensorMeasure = latestMeasures.get(measureKey);
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
        return new ArrayList<>(latestMeasures.keySet());
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
        latestMeasures.put(measureKey, sensorMeasure);
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
 * CREATE TABLE  sensor_measure (
 *     location text,
 *     day text,
 *     bucket int,
 *     measureType text,
 *     ts timeuuid,
 *     measureValue float,
 *     primary key((location, day, bucket), ts)
 * ) WITH CLUSTERING ORDER BY (ts DESC)
 *          AND COMPACTION = {'class': 'TimeWindowCompactionStrategy',
 *                        'compaction_window_unit': 'DAYS',
 *                        'compaction_window_size': 1};
 *
 * example (query all buckets to scale when input to read is too big per partition => combine DB data in the backend code)
 * CREATE TABLE tweet_stream (
 *     account text,
 *     day text,
 *     bucket int,
 *     ts timeuuid,
 *     message text,
 *     primary key((account, day, bucket), ts)
 * ) WITH CLUSTERING ORDER BY (ts DESC)
 *          AND COMPACTION = {'class': 'TimeWindowCompactionStrategy',
 *                        'compaction_window_unit': 'DAYS',
 *                        'compaction_window_size': 1};
 *   pond.js => time series processing
 *   react-time series => time series plot
 *
 *
 */
