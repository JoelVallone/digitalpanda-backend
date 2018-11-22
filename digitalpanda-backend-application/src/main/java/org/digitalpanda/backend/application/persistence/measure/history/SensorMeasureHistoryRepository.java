package org.digitalpanda.backend.application.persistence.measure.history;

import org.digitalpanda.backend.data.SensorMeasureType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.CassandraOperations;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
import java.util.stream.LongStream;

import static java.util.stream.Collectors.toList;
import static org.digitalpanda.backend.application.persistence.measure.SensorMeasureDaoHelper.getHistoricalMeasureBlockId;

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

        long startBlockId = getHistoricalMeasureBlockId(intervalBeginMillisIncl, targetHistoricalDataSizing);
        long endBlockId = getHistoricalMeasureBlockId(intervalEndSecondsIncl, targetHistoricalDataSizing);

        //Only second granularity measure data are available at the moment
        if (targetHistoricalDataSizing.getAggregateIntervalSeconds() > 5L ) {
            return Collections.emptyList();
        }

        return LongStream.rangeClosed(startBlockId, endBlockId).boxed()
                .map(blockId -> {
                        String cqlRangeSelect = String.format(
                                "SELECT * FROM iot.sensor_measure_history_seconds WHERE " +
                                        "location = '%s' AND " +
                                        "time_block_id = %d AND " +
                                        "measure_type = '%s' AND " +
                                        "bucket = %d AND " +
                                        "timestamp >= %d AND timestamp <= %d",
                                location,
                                blockId,
                                measureType.name(),
                                SensorMeasureHistorySecondsDao.SENSOR_MEASURE_DEFAULT_BUCKET_ID,
                                intervalBeginMillisIncl,
                                intervalEndSecondsIncl);
                        logger.debug(cqlRangeSelect);
                        return cassandraTemplate.select(cqlRangeSelect, SensorMeasureHistorySecondsDao.class);
                    }
                )
                .flatMap(List::stream)
                .collect(toList());
    }


}
