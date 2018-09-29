package org.digitalpanda.backend.application.persistence.sensors;

import org.digitalpanda.backend.application.persistence.CassandraWithSpringBaseTest;
import org.digitalpanda.backend.application.persistence.sensors.latest.SensorMeasureLatestDao;
import org.digitalpanda.backend.data.SensorMeasure;
import org.digitalpanda.backend.data.SensorMeasureMetaData;
import org.digitalpanda.backend.data.SensorMeasureType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.CassandraAdminOperations;
import org.springframework.data.cassandra.core.cql.CqlIdentifier;

import java.util.Date;
import java.util.HashMap;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNull;

public class SensorMeasureRepositoryIntegrationTest extends CassandraWithSpringBaseTest {

    //@Rule
    //public CassandraCQLUnit cassandraCQLUnit = new CassandraCQLUnit(new ClassPathCQLDataSet("dataset.cql", CassandraConfig.APP_KEYSPACE));
    private static final SensorMeasureMetaData TEST_PRIMARY_KEY = new SensorMeasureMetaData("locationA", SensorMeasureType.TEMPERATURE);
    private static final SensorMeasure TEST_INITIAL_VALUE = new SensorMeasure(System.currentTimeMillis(), 1.42);

    @Autowired
    CassandraAdminOperations adminTemplate;

    @Autowired
    SensorMeasureRepository repository;

    @Before
    public void createTable() {
        adminTemplate.createTable(
                true, CqlIdentifier.of(SensorMeasureLatestDao.SENSOR_MEASURE_LATEST_TABLE_NAME), SensorMeasureLatestDao.class, new HashMap<>());
        externalUpdate(TEST_PRIMARY_KEY, TEST_INITIAL_VALUE);
    }

    @Test
    public void should_coldReadThenUpdateCacheThenAvailableRead() {
        //Given
        long now = System.currentTimeMillis();

        //When
        SensorMeasure actualCold = repository.getLatestMeasure(TEST_PRIMARY_KEY);
        repository.updateCache();
        SensorMeasure actualHot = repository.getLatestMeasure(TEST_PRIMARY_KEY);

        //Then
        assertNull(actualCold);
        assertEquals(TEST_INITIAL_VALUE.getTimestamp(), actualHot.getTimestamp());
        assertEquals(TEST_INITIAL_VALUE.getValue(), actualHot.getValue());
    }

    @Test
    public void should_coldReadThenLocalSetThenReadHot() {
        //Given
        long now = System.currentTimeMillis();
        SensorMeasureMetaData key = new SensorMeasureMetaData("locationB", SensorMeasureType.TEMPERATURE);
        SensorMeasure expected = new SensorMeasure(now, 2.42);

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
        SensorMeasure expected2 = new SensorMeasure(now, 2.42);

        //When
        repository.updateCache();
        SensorMeasure firstRead = repository.getLatestMeasure(TEST_PRIMARY_KEY);
        repository.setMeasure(TEST_PRIMARY_KEY, expected2);
        SensorMeasure secondRead = repository.getLatestMeasure(TEST_PRIMARY_KEY);

        //Then
        assertEquals(TEST_INITIAL_VALUE.getTimestamp(), firstRead.getTimestamp());
        assertEquals(TEST_INITIAL_VALUE.getValue(), firstRead.getValue());
        assertEquals(expected2.getTimestamp(), secondRead.getTimestamp());
        assertEquals(expected2.getValue(), secondRead.getValue());

    }

    @Test
    public void should_hotReadThenExternalUpdateThenUpToDateRead() {
        //Given
        long now = System.currentTimeMillis();
        SensorMeasure expectedExternalUpdate = new SensorMeasure(now, 2.42);

        //When
        repository.updateCache();
        SensorMeasure firstRead = repository.getLatestMeasure(TEST_PRIMARY_KEY);
        externalUpdate(TEST_PRIMARY_KEY, expectedExternalUpdate);
        repository.updateCache();
        SensorMeasure secondRead = repository.getLatestMeasure(TEST_PRIMARY_KEY);

        //Then
        assertEquals(TEST_INITIAL_VALUE.getTimestamp(), firstRead.getTimestamp());
        assertEquals(TEST_INITIAL_VALUE.getValue(), firstRead.getValue());
        assertEquals(expectedExternalUpdate.getTimestamp(), secondRead.getTimestamp());
        assertEquals(expectedExternalUpdate.getValue(), secondRead.getValue());

    }

    private void externalUpdate(SensorMeasureMetaData key, SensorMeasure measure) {
        SensorMeasureLatestDao dao = new SensorMeasureLatestDao();
        dao.setLocation(key.getLocation());
        dao.setMeasureType(key.getType().name());
        dao.setMeasureValue(measure.getValue());
        dao.setTimestamp(new Date(measure.getTimestamp()));

        adminTemplate.insert(dao);
    }

    @After
    public void dropTable() {
        adminTemplate.dropTable(CqlIdentifier.of(SensorMeasureLatestDao.SENSOR_MEASURE_LATEST_TABLE_NAME));
    }
}
