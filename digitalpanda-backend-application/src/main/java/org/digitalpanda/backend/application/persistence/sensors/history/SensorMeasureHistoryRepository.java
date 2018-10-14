package org.digitalpanda.backend.application.persistence.sensors.history;

import org.digitalpanda.backend.data.SensorMeasureType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.CassandraOperations;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.stream.LongStream;

import static java.util.stream.Collectors.toList;
import static org.digitalpanda.backend.application.persistence.sensors.SensorMeasureDaoHelper.getHistoricalMeasureBlockId;

@Repository
public class SensorMeasureHistoryRepository  {

    private Logger logger =  LoggerFactory.getLogger(SensorMeasureHistoryRepository.class);

    @Autowired
    private CassandraOperations cassandraTemplate; //Used for advanced queries

    @Autowired
    private SensorMeasureHistoryRepositoryCRUD sensorMeasureHistoryRepoCRUD; //Available for CRUD queries

    public SensorMeasureHistoryRepository() {

    }

    public List<SensorMeasureHistoryDao> saveAll(List<SensorMeasureHistoryDao> measuresToSave){
        return sensorMeasureHistoryRepoCRUD.saveAll(measuresToSave);
    }


    public List<SensorMeasureHistoryDao> getMeasuresAtLocationWithInterval(
            String location,
            SensorMeasureType measureType,
            AggregateType aggregateType,
            HistoricalDataStorageSizing targetHistoricalDataSizing,
            Date beginInc,
            Date endIncl) {

        long startBlockId = getHistoricalMeasureBlockId(beginInc, targetHistoricalDataSizing);
        long endBlockId = getHistoricalMeasureBlockId(endIncl, targetHistoricalDataSizing);

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
                                beginInc.getTime(),
                                endIncl.getTime());
                        logger.info(cqlRangeSelect);
                        return cassandraTemplate.select(cqlRangeSelect, SensorMeasureHistoryDao.class);
                    }
                )
                .flatMap(List::stream)
                .collect(toList());
    }


}
