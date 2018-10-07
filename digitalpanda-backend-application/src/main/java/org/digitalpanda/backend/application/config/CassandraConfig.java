package org.digitalpanda.backend.application.config;

import com.google.common.io.ByteStreams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.cassandra.config.AbstractCassandraConfiguration;
import org.springframework.data.cassandra.config.CassandraClusterFactoryBean;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Configuration
@EnableCassandraRepositories("org.digitalpanda.backend.application.persistence.sensors")
@PropertySource("classpath:application.properties")
public class CassandraConfig extends AbstractCassandraConfiguration {


    public static final String APP_KEYSPACE = "iot";

    @Override
    protected String getKeyspaceName() {
        return APP_KEYSPACE;
    }

    public String[] getEntityBasePackages() {
        return new String[] { "org.digitalpanda.backend.application.persistence.sensors" };
    }

    @Override
    protected List<String> getStartupScripts(){
        /*
        String keyspace = "CREATE KEYSPACE IF NOT EXISTS " +  this.getKeyspaceName()
                + " WITH durable_writes = true "
                + " AND replication = { 'replication_factor' : 1, 'class' : 'SimpleStrategy' };";
        return Collections.singletonList(keyspace);*/

        try {
            String initScript = new String(
                    ByteStreams.toByteArray(
                            (new ClassPathResource("init.cql")).getInputStream()));
            return Arrays.stream(initScript.split(";"))
                    .map(s -> s + ";")
                    .collect(toList());
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
    @Override
    protected List<String> getShutdownScripts() {
        return Collections.emptyList();
    }

    @Value("${cassandra.port}")
    String cassandraPort;
    @Override
    protected int getPort() {
        return cassandraPort != null ? Integer.valueOf(cassandraPort) : CassandraClusterFactoryBean.DEFAULT_PORT;
    }
}