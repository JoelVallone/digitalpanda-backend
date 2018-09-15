package org.digitalpanda.backend.application.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.config.AbstractCassandraConfiguration;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Configuration
//@EnableCassandraRepositories("org.digitalpanda.backend.application.persistence.sensors")
public class CassandraConfig extends AbstractCassandraConfiguration {

    /*@Bean
    public CassandraClusterFactoryBean cluster() {
        CassandraClusterFactoryBean cluster =
                new CassandraClusterFactoryBean();
        cluster.setContactPoints("127.0.0.1");
        cluster.setPort(9042);
        return cluster;
    }*/

    @Override
    protected String getKeyspaceName() {
        return "iot";
    }

    /*
    @Bean
    public CassandraMappingContext cassandraMapping()
            throws ClassNotFoundException {
        return new BasicCassandraMappingContext();
    }
    */

    public String[] getEntityBasePackages() {
        return new String[] { "org.digitalpanda.backend.application.persistence.sensors" };
    }

    /*

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

    @Override
    protected List<String> getStartupScripts() {

        String script = "CREATE KEYSPACE IF NOT EXISTS " +  this.getKeyspaceName()
                + " WITH durable_writes = true "
                + " AND replication = { 'replication_factor' : 1, 'class' : 'SimpleStrategy' };";

        return Arrays.asList(script);
    }

    @Override
    protected List<String> getShutdownScripts() {
        return Collections.emptyList();
    }
}