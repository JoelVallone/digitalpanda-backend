package org.digitalpanda.backend.application.persistence.sensors;

import org.digitalpanda.backend.application.persistence.sensors.latest.SensorMeasureLatestDao;
import org.digitalpanda.backend.data.SensorMeasure;
import org.digitalpanda.backend.data.SensorMeasureMetaData;
import org.springframework.data.cassandra.core.mapping.BasicMapId;
import org.springframework.data.cassandra.core.mapping.MapId;

import java.time.Instant;
import java.util.Date;

public class SensorMeasureDaoHelper {


    public static SensorMeasureLatestDao toLatestMeasureDao(SensorMeasureMetaData measureKey, SensorMeasure sensorMeasure){
        SensorMeasureLatestDao dao = new SensorMeasureLatestDao();
        Date sampleDate = toDate(sensorMeasure.getTimestamp());

        dao.setLocation(measureKey.getLocation());

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
        return SensorMeasureLatestDao.DATE_DAY.format(sampleDate);
    }

    private static Date toDate(long timeMillisSinceEpoch){
        return Date.from(Instant.ofEpochMilli(timeMillisSinceEpoch));
    }
}
