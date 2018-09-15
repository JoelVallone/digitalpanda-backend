package org.digitalpanda.backend.application.persistence.sensors;

import org.digitalpanda.backend.application.persistence.CassandraWithSpringBaseTest;
import org.digitalpanda.backend.data.SensorMeasureType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.CassandraAdminOperations;
import org.springframework.data.cassandra.core.cql.CqlIdentifier;
import org.springframework.data.cassandra.core.mapping.BasicMapId;
import org.springframework.data.cassandra.core.mapping.MapId;

import java.util.HashMap;
import java.util.Optional;

import static junit.framework.TestCase.assertEquals;

public class SensorMeasureRepositoryIntegrationTest extends CassandraWithSpringBaseTest {

    @Autowired
    CassandraAdminOperations adminTemplate;

    @Autowired
    SensorMeasureCassandraRepository repository;

    @Before
    public void createTable() {
        adminTemplate.createTable(
                true, CqlIdentifier.of(SensorMeasureDao.SENSOR_MEASURE_TABLE_NAME), SensorMeasureDao.class, new HashMap<>());
    }

    @Test
    public void shouldInsertAndFindEntity() {
        SensorMeasureDao sensorMeasureDao = new SensorMeasureDao();
        sensorMeasureDao.setLocation("SomewhereNearMyComputer");
        sensorMeasureDao.setDay("2018-09-08");
        sensorMeasureDao.setTimestamp("1536391282793");
        sensorMeasureDao.setMeasureType(SensorMeasureType.HUMIDITY.name());
        sensorMeasureDao.setMeasureValue(42.3);

        repository.save(sensorMeasureDao);

        MapId id = new BasicMapId();
        id.put("location", sensorMeasureDao.getLocation());
        id.put("day", sensorMeasureDao.getDay());
        id.put("bucket", 0);
        id.put("timestamp", sensorMeasureDao.getTimestamp());
        Optional<SensorMeasureDao> actual = repository.findById(id);

        assertEquals(sensorMeasureDao, actual.get());
    }


    @After
    public void dropTable() {
        adminTemplate.dropTable(CqlIdentifier.of(SensorMeasureDao.SENSOR_MEASURE_TABLE_NAME));
    }
}
