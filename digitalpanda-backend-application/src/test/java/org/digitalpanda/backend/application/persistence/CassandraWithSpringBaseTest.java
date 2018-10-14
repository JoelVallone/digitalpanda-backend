package org.digitalpanda.backend.application.persistence;

import org.cassandraunit.utils.EmbeddedCassandraServerHelper;
import org.digitalpanda.backend.application.config.CassandraConfig;
import org.digitalpanda.backend.application.persistence.sensors.history.SensorMeasureHistoryRepository;
import org.digitalpanda.backend.application.persistence.sensors.latest.SensorMeasureLatestRepository;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { CassandraConfig.class, SensorMeasureLatestRepository.class, SensorMeasureHistoryRepository.class} )
public abstract class CassandraWithSpringBaseTest {

    @BeforeClass
    public static void startCassandraEmbedded() throws Exception{
        //https://github.com/jsevellec/cassandra-unit/wiki/How-to-use-it-in-your-code
        EmbeddedCassandraServerHelper.startEmbeddedCassandra("embedded-cassandra.yaml");
    }
}
