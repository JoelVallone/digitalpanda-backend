package org.digitalpanda.backend.application.persistence.sensors;

import org.digitalpanda.backend.data.SensorMeasure;
import org.digitalpanda.backend.data.SensorMeasureMetaData;
import org.digitalpanda.backend.data.SensorMeasureType;
import org.junit.Test;

import static org.junit.Assert.*;

public class SensorMeasureMapperTest {

    @Test
    public void shouldMapToDao(){
        //Given
        SensorMeasureMetaData measureKey = new SensorMeasureMetaData("aLocation", SensorMeasureType.TEMPERATURE);
        SensorMeasure measureValue = new SensorMeasure( 1535718586193L, 42.404);

        //When
        SensorMeasureDao actual = SensorMeasureDaoHelper.toDao(measureKey, measureValue);

        //Then
        assertEquals("aLocation", actual.getLocation());
        assertEquals("2018-08-31", actual.getDay());
        assertEquals(0, actual.getBucket());
        assertEquals(1535718586193L, actual.getTimestamp().toInstant().toEpochMilli());
        assertEquals(SensorMeasureType.TEMPERATURE.getUnit(), actual.getMeasureType());
        assertEquals(42.404, actual.getMeasureValue(), 0.001);
    }

}