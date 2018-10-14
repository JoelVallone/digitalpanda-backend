package org.digitalpanda.backend.application.persistence.sensors.history;

import org.springframework.data.cassandra.repository.MapIdCassandraRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SensorMeasureHistoryRepositoryCRUD extends MapIdCassandraRepository<SensorMeasureHistoryDao> {
    //Auto-manged by Spring
}
