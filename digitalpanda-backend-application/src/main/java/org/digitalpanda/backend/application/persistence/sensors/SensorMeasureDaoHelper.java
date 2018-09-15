package org.digitalpanda.backend.application.persistence.sensors;

import org.digitalpanda.backend.data.SensorMeasure;
import org.digitalpanda.backend.data.SensorMeasureMetaData;

import java.time.Instant;
import java.util.Date;

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

    private static String extractDateDay(Date sampleDate){
        return SensorMeasureDao.DATE_DAY.format(sampleDate);
    }

    private static Date toDate(long timeMillisSinceEpoch){
        return Date.from(Instant.ofEpochMilli(timeMillisSinceEpoch));
    }
}
