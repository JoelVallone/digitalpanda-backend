package org.digitalpanda.backend.application.persistence;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import org.cassandraunit.utils.EmbeddedCassandraServerHelper;
import org.digitalpanda.backend.application.config.CassandraConfig;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { CassandraConfig.class} ) //, SensorMeasureCassandraRepository.class
public abstract class CassandraWithSpringBaseTest {

    @BeforeClass
    public static void startCassandraEmbedded() throws Exception{
        //https://github.com/jsevellec/cassandra-unit/wiki/How-to-use-it-in-your-code
        System.setProperty("log4j.configuration", "file:log4j-embedded-cassandra-panda.properties");
        EmbeddedCassandraServerHelper.startEmbeddedCassandra();
        Cluster cluster = Cluster.builder()
                .addContactPoints("127.0.0.1").withPort(9142).build();
        Session session = cluster.connect();
        String initScript = "CREATE KEYSPACE IF NOT EXISTS iot "
                + "WITH durable_writes = true "
                + "AND replication = { 'replication_factor' : 1, 'class' : 'SimpleStrategy' };";

        session.execute(initScript);
        session.execute("use iot;");
    }
}
