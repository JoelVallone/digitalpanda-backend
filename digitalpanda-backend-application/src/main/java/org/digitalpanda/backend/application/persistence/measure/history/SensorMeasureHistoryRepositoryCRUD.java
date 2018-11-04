package org.digitalpanda.backend.application.persistence.measure.history;

import org.springframework.data.cassandra.repository.MapIdCassandraRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SensorMeasureHistoryRepositoryCRUD extends MapIdCassandraRepository<SensorMeasureHistorySecondsDao> {
    //Auto-manged by Spring
}
