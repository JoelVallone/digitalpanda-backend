package org.digitalpanda.backend.application.persistence.sensors;

import org.digitalpanda.backend.application.persistence.sensors.history.HistoricalDataStorageSizing;
import org.digitalpanda.backend.application.persistence.sensors.latest.SensorMeasureLatestDao;
import org.digitalpanda.backend.application.util.Pair;
import org.digitalpanda.backend.data.SensorMeasure;
import org.digitalpanda.backend.data.SensorMeasureMetaData;
import org.digitalpanda.backend.data.SensorMeasureType;
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
        dao.setValue(sensorMeasure.getValue());

        return dao;
    }

    public static Pair<SensorMeasureMetaData, SensorMeasure> toSensorMeasure(SensorMeasureLatestDao sensorMeasureLatestDao){
        return new Pair<>(
                new SensorMeasureMetaData(
                        sensorMeasureLatestDao.getLocation(),
                        sensorMeasureLatestDao.getMeasureType() != null ? SensorMeasureType.valueOf(sensorMeasureLatestDao.getMeasureType()) : null),
                new SensorMeasure(
                        sensorMeasureLatestDao.getTimestamp() != null ? sensorMeasureLatestDao.getTimestamp().getTime() : null,
                        sensorMeasureLatestDao.getValue()
                )
        );
    }

    public static MapId primaryKeyForLatestMeasure(SensorMeasureMetaData measureKey){
        MapId id = new BasicMapId();
        id.put("location", measureKey.getLocation());
        if (measureKey.getType() != null) {
            id.put("measureType", measureKey.getType().name());
        }
        return id;
    }

    public static Date toDate(long timeMillisSinceEpoch){
        return Date.from(Instant.ofEpochMilli(timeMillisSinceEpoch));
    }

    public static Long getHistoricalMeasureBlockId(Date targetTime, HistoricalDataStorageSizing targetHistoricalData) {
        return targetTime.getTime() / 1000L / targetHistoricalData.getTimeBlockPeriodSeconds();
    }
}
