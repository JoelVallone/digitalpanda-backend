package org.digitalpanda.backend.application.ressource;

import org.digitalpanda.backend.application.persistence.sensors.SensorMeasureRepository;
import org.digitalpanda.backend.data.SensorMeasure;
import org.digitalpanda.backend.data.SensorMeasureType;
import org.digitalpanda.backend.data.SensorMeasureMetaData;
import org.digitalpanda.backend.data.SensorMeasures;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SensorMeasureControllerTest {

    @Mock
    private SensorMeasureRepository sensorMeasureRepositoryMock;
    private SensorMeasureController sensorMeasureController;

    @Before
    public void init() {
        this.sensorMeasureController = new SensorMeasureController(sensorMeasureRepositoryMock);
    }

    @Test
    public void should_get_latest_sensor_measure() {
        //Given
        final SensorMeasureMetaData sensorMeasureMetaData = new SensorMeasureMetaData("home", SensorMeasureType.HUMIDITY);
        final SensorMeasure sensorMeasure = new SensorMeasure(33L,42.0);
        when(sensorMeasureRepositoryMock.getMeasure(sensorMeasureMetaData)).thenReturn(sensorMeasure);

        //When
        SensorMeasure actual = sensorMeasureController.getLatestMeasure(sensorMeasureMetaData);

        //Then
        verify(sensorMeasureRepositoryMock, times(1))
                .getMeasure(Matchers.eq(sensorMeasureMetaData));
        assertEquals(actual,sensorMeasure);
    }

    @Test
    public void should_set_latest_sensor_measure() {
        //Given
        final SensorMeasure measure1 = new SensorMeasure(32L,42.0);
        final SensorMeasure measure2 = new SensorMeasure(01L,41.0);
        final List<SensorMeasure> measures = Arrays.asList(new SensorMeasure [] {measure1, measure2});
        final SensorMeasureMetaData sensorMeasureMetaData = new SensorMeasureMetaData("home", SensorMeasureType.HUMIDITY);
        final SensorMeasures sensorMeasures  = new SensorMeasures(sensorMeasureMetaData, measures);
        final List<SensorMeasures> sensorMeasuresList = new ArrayList<>();
        sensorMeasuresList.add(sensorMeasures);

        //Then
        sensorMeasureController.setLatestMeasure(sensorMeasuresList);

        //When
        verify(sensorMeasureRepositoryMock, times(1))
                .setMeasure(Matchers.eq(sensorMeasureMetaData), Matchers.eq(measure1));
    }
}
