package org.digitalpanda.backend.application.persistence.measure.history;

import org.digitalpanda.backend.data.history.HistoricalDataStorageSizing;
import org.digitalpanda.backend.data.SensorMeasureType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.CassandraOperations;
import org.springframework.stereotype.Repository;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.digitalpanda.backend.data.history.HistoricalDataStorageHelper.getRangeSelectionCqlQueries;

@Repository
public class SensorMeasureHistoryRepository {

    private Logger logger = LoggerFactory.getLogger(SensorMeasureHistoryRepository.class);

    @Autowired
    private CassandraOperations cassandraTemplate; //Used for advanced queries

    @Autowired
    private SensorMeasureHistoryRepositoryCRUD sensorMeasureHistoryRepoCRUD; //Available for CRUD queries

    public SensorMeasureHistoryRepository() {

    }

    public List<SensorMeasureHistorySecondsDao> saveAllSecondPrecisionMeasures(List<SensorMeasureHistorySecondsDao> measuresToSave) {
        return sensorMeasureHistoryRepoCRUD.saveAll(measuresToSave);
    }


    public List<SensorMeasureHistorySecondsDao> getMeasuresAtLocationWithInterval(
            String location,
            SensorMeasureType measureType,
            HistoricalDataStorageSizing targetHistoricalDataSizing,
            long intervalBeginMillisIncl,
            long intervalEndSecondsIncl) {

        return getRangeSelectionCqlQueries(location, measureType, targetHistoricalDataSizing,
                intervalBeginMillisIncl,intervalEndSecondsIncl).stream()
                .map( cqlRangeSelect ->
                    cassandraTemplate.select(cqlRangeSelect, SensorMeasureHistorySecondsDao.class)
                ).flatMap(List::stream)
                .collect(toList());
    }


}
