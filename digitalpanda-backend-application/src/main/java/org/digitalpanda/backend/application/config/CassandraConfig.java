package org.digitalpanda.backend.application.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.config.AbstractCassandraConfiguration;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Configuration
@EnableCassandraRepositories("org.digitalpanda.backend.application.persistence.sensors")
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
    protected List<String> getStartupScripts() {
        //FIXME: Externalize in a config script
        String script = "CREATE KEYSPACE IF NOT EXISTS " +  this.getKeyspaceName()
                + " WITH durable_writes = true "
                + " AND replication = { 'replication_factor' : 1, 'class' : 'SimpleStrategy' };";

        return Arrays.asList(script);
    }

    @Override
    protected List<String> getShutdownScripts() {
        return Collections.emptyList();
    }

    /*

    /*@Bean
    public CassandraClusterFactoryBean cluster() {
        CassandraClusterFactoryBean cluster =
                new CassandraClusterFactoryBean();
        cluster.setContactPoints("127.0.0.1");
        cluster.setPort(9042);
        return cluster;
    }

    @Override
    protected List<CreateKeyspaceSpecification> getKeyspaceCreations() {

        CreateKeyspaceSpecification specification = CreateKeyspaceSpecification
                .createKeyspace(this.getKeyspaceName())
                .withSimpleReplication()
                .ifNotExists(true);

        return Arrays.asList(specification);
    }

    @Override
    public SchemaAction getSchemaAction() {
        return SchemaAction.CREATE_IF_NOT_EXISTS;
    }

     */
    /**/
}