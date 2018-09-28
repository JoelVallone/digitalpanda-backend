package org.digitalpanda.backend.application.persistence.sensors;

import org.digitalpanda.backend.data.SensorMeasure;
import org.digitalpanda.backend.data.SensorMeasureMetaData;
import org.springframework.data.cassandra.core.mapping.BasicMapId;
import org.springframework.data.cassandra.core.mapping.MapId;

import java.time.Instant;
import java.util.Date;
import java.util.Optional;

public class SensorMeasureDaoHelper {

    public static SensorMeasureDao toDao(SensorMeasureMetaData measureKey, SensorMeasure sensorMeasure){
        SensorMeasureDao dao = new SensorMeasureDao();
        Date sampleDate = toDate(sensorMeasure.getTimestamp());

        dao.setLocation(measureKey.getLocation());
        dao.setDay(extractDateDay(sampleDate));
        dao.setBucket(SensorMeasureDao.SENSOR_MEASURE_DEFAULT_BUCKET_ID);

        dao.setTimestamp(sampleDate);

        dao.setMeasureType(measureKey.getType().getUnit());
        dao.setMeasureValue(sensorMeasure.getValue());

        return dao;
    }

    public static MapId buildMap(SensorMeasureMetaData measureKey, Long timestamp){
        MapId id = new BasicMapId();
        id.put("location", measureKey.getLocation());
        id.put("day", extractDateDay(toDate(timestamp)));
        id.put("bucket", 0);
        id.put("timestamp", timestamp);
        return id;
    }

    private static String extractDateDay(Date sampleDate){
        return SensorMeasureDao.DATE_DAY.format(sampleDate);
    }

    private static Date toDate(long timeMillisSinceEpoch){
        return Date.from(Instant.ofEpochMilli(timeMillisSinceEpoch));
    }
}
