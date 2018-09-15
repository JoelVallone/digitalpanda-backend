package org.digitalpanda.backend.application.persistence.sensors;

import org.springframework.data.cassandra.repository.MapIdCassandraRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SensorMeasureCassandraRepository extends MapIdCassandraRepository<SensorMeasureDao> {
    //Auto-manged by Spring
}
