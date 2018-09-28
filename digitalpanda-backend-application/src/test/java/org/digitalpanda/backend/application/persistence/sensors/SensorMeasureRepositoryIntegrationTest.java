package org.digitalpanda.backend.application.persistence.sensors;

import org.digitalpanda.backend.application.persistence.CassandraWithSpringBaseTest;
import org.digitalpanda.backend.data.SensorMeasure;
import org.digitalpanda.backend.data.SensorMeasureMetaData;
import org.digitalpanda.backend.data.SensorMeasureType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.CassandraAdminOperations;
import org.springframework.data.cassandra.core.cql.CqlIdentifier;

import java.util.HashMap;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNull;

public class SensorMeasureRepositoryIntegrationTest extends CassandraWithSpringBaseTest {

    //@Rule
    //public CassandraCQLUnit cassandraCQLUnit = new CassandraCQLUnit(new ClassPathCQLDataSet("dataset.cql", CassandraConfig.APP_KEYSPACE));


    @Autowired
    CassandraAdminOperations adminTemplate;

    @Autowired
    SensorMeasureRepository repository;

    @Before
    public void createTable() {
        adminTemplate.createTable(
                true, CqlIdentifier.of(SensorMeasureDao.SENSOR_MEASURE_LATEST_TABLE_NAME), SensorMeasureDao.class, new HashMap<>());
    }

    @Test
    public void should_coldReadThenUpdateCacheThenAvailableRead() {
        //Given
        long now = System.currentTimeMillis();
        SensorMeasureMetaData key = new SensorMeasureMetaData("locationA", SensorMeasureType.TEMPERATURE);
        SensorMeasure expected = new SensorMeasure(now, 03.400;
        externalUpdate(key, expected);

        //When
        SensorMeasure actualCold = repository.getLatestMeasure(key);
        repository.updateCache();
        SensorMeasure actualHot = repository.getLatestMeasure(key);

        //Then
        assertNull(actualCold);
        assertEquals(expected.getTimestamp(), actualHot.getTimestamp());
        assertEquals(expected.getValue(), actualHot.getValue());
    }

    @Test
    public void should_coldReadThenLocalSetThenReadHot() {
        //Given
        long now = System.currentTimeMillis();
        SensorMeasureMetaData key = new SensorMeasureMetaData("locationA", SensorMeasureType.TEMPERATURE);
        SensorMeasure expected = new SensorMeasure(now, 13.400);

        //When
        SensorMeasure actualCold = repository.getLatestMeasure(key);
        repository.setMeasure(key, expected);
        SensorMeasure actualHot = repository.getLatestMeasure(key);

        //Then
        assertNull(actualCold);
        assertEquals(expected.getTimestamp(), actualHot.getTimestamp());
        assertEquals(expected.getValue(), actualHot.getValue());
    }


    @Test
    public void should_hotReadThenSetThenUpToDateRead() {
        //Given
        long now = System.currentTimeMillis();
        SensorMeasureMetaData key = new SensorMeasureMetaData("locationA", SensorMeasureType.TEMPERATURE);
        SensorMeasure expected1 = new SensorMeasure(now, 13.400);
        SensorMeasure expected2 = new SensorMeasure(now, 23.400);

        //When
        repository.setMeasure(key, expected1);
        SensorMeasure firstRead = repository.getLatestMeasure(key);
        repository.setMeasure(key, expected2);
        SensorMeasure secondRead = repository.getLatestMeasure(key);

        //Then
        assertEquals(expected1.getTimestamp(), firstRead.getTimestamp());
        assertEquals(expected1.getValue(), firstRead.getValue());
        assertEquals(expected2.getTimestamp(), secondRead.getTimestamp());
        assertEquals(expected2.getValue(), secondRead.getValue());

    }

    //TODO: Add more test cases with external updates

    private void externalUpdate(SensorMeasureMetaData key, SensorMeasure expected) {
        ???
    }

    @After
    public void dropTable() {
        adminTemplate.dropTable(CqlIdentifier.of(SensorMeasureDao.SENSOR_MEASURE_LATEST_TABLE_NAME));
    }
}
