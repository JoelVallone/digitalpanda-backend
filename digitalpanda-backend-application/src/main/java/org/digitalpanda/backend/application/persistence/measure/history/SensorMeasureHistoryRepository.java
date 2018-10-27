package org.digitalpanda.backend.application.persistence.measure.history;

import org.digitalpanda.backend.data.SensorMeasureType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.CassandraOperations;
import org.springframework.stereotype.Repository;

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

    public List<SensorMeasureHistoryDao> saveAll(List<SensorMeasureHistoryDao> measuresToSave) {
        return sensorMeasureHistoryRepoCRUD.saveAll(measuresToSave);
    }


    public List<SensorMeasureHistoryDao> getMeasuresAtLocationWithInterval(
            String location,
            SensorMeasureType measureType,
            AggregateType aggregateType,
            HistoricalDataStorageSizing targetHistoricalDataSizing,
            long intervalBeginMillisIncl,
            long intervalEndSecondsIncl) {

        long startBlockId = getHistoricalMeasureBlockId(intervalBeginMillisIncl, targetHistoricalDataSizing);
        long endBlockId = getHistoricalMeasureBlockId(intervalEndSecondsIncl, targetHistoricalDataSizing);

        return LongStream.rangeClosed(startBlockId, endBlockId).boxed()
                .map(blockId -> {
                        String cqlRangeSelect = String.format(
                                "SELECT * FROM iot.sensor_measure_history WHERE " +
                                        "location = '%s' AND " +
                                        "time_block_period_seconds = %d AND " +
                                        "time_block_id = %d AND " +
                                        "measure_type = '%s' AND " +
                                        "aggregate_type = '%s' AND " +
                                        "bucket = %d AND " +
                                        "timestamp >= %d AND timestamp <= %d",
                                location,
                                targetHistoricalDataSizing.getTimeBlockPeriodSeconds(),
                                blockId,
                                measureType.name(),
                                aggregateType.name(),
                                SensorMeasureHistoryDao.SENSOR_MEASURE_DEFAULT_BUCKET_ID,
                                intervalBeginMillisIncl,
                                intervalEndSecondsIncl);
                        logger.debug(cqlRangeSelect);
                        return cassandraTemplate.select(cqlRangeSelect, SensorMeasureHistoryDao.class);
                    }
                )
                .flatMap(List::stream)
                .collect(toList());
    }


}
