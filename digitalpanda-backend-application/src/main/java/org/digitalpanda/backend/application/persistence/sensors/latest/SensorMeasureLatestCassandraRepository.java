package org.digitalpanda.backend.application.persistence.sensors.latest;

import org.springframework.data.cassandra.repository.MapIdCassandraRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SensorMeasureLatestCassandraRepository extends MapIdCassandraRepository<SensorMeasureLatestDao> {
    //Auto-manged by Spring
}
