package org.digitalpanda.backend.application.persistence.measure.history;

import org.digitalpanda.backend.application.persistence.CassandraWithSpringBaseTest;
import org.digitalpanda.backend.application.persistence.measure.latest.SensorMeasureLatestDao;
import org.digitalpanda.backend.application.util.Pair;
import org.digitalpanda.backend.data.SensorMeasureType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.CassandraAdminOperations;
import org.springframework.data.cassandra.core.cql.CqlIdentifier;

import java.text.SimpleDateFormat;
import java.util.*;

import static java.util.stream.Collectors.toList;
import static junit.framework.TestCase.assertEquals;
import static org.digitalpanda.backend.application.persistence.measure.SensorMeasureDaoHelper.getHistoricalMeasureBlockId;

public class SensorMeasureHistoryRepositoryIntegrationTest extends CassandraWithSpringBaseTest {

    private static final SensorMeasureType TARGET_MEASURE_TYPE = SensorMeasureType.TEMPERATURE;
    private static final AggregateType TARGET_AGGREGATE_TYPE = AggregateType.AVG;
    private static final HistoricalDataStorageSizing TARGET_HISTORICAL_DATA_SIZING = HistoricalDataStorageSizing.SECOND_PRECISION_RAW;
    private static final String TEST_LOCATION = "SomewhereNearMyComputer";

    @Autowired
    CassandraAdminOperations adminTemplate;

    @Autowired
    SensorMeasureHistoryRepository repository;

    private static final SimpleDateFormat DATE_FORMAT =new SimpleDateFormat("MMM dd yyyy HH:mm:ss");

    @Before
    public void createTable() {
        adminTemplate.createTable(
                true, CqlIdentifier.of(SensorMeasureHistoryDao.SENSOR_MEASURE_HISTORY_TABLE_NAME), SensorMeasureLatestDao.class, new HashMap<>());
    }

    @Test
    public void shouldInsertAndFindEntity() throws Exception {
        //Given
        //  => Data in table partition 1
        Date dateFirstElementFirstPartition = DATE_FORMAT.parse("Dec 24 2018 23:59:59");
        List<SensorMeasureHistoryDao> firstPartition = measureSequence(Collections.singletonList(
                new Pair<>(dateFirstElementFirstPartition, 1.14159)));
        //  => Data in table partition 2
        Date dateFirstElementSecondPartition = new Date(
                (getHistoricalMeasureBlockId(dateFirstElementFirstPartition, TARGET_HISTORICAL_DATA_SIZING) + 1)
                        * TARGET_HISTORICAL_DATA_SIZING.getTimeBlockPeriodSeconds()
                        * 1000L);
        Date dateSecondElementSecondPartition = new Date(
                dateFirstElementSecondPartition.getTime() + 1000L);
        List<SensorMeasureHistoryDao> secondPartition = measureSequence(Arrays.asList(
            new Pair<>(dateFirstElementSecondPartition, 2.14159),
            new Pair<>(dateSecondElementSecondPartition, 3.14159)));

        List<SensorMeasureHistoryDao> dataToStore = new ArrayList<>(firstPartition);
        dataToStore.addAll(secondPartition);

        //When
        repository.saveAll(dataToStore);
        List<SensorMeasureHistoryDao> actual = repository
                .getMeasuresAtLocationWithInterval(
                    TEST_LOCATION,
                    TARGET_MEASURE_TYPE,
                    TARGET_AGGREGATE_TYPE,
                    TARGET_HISTORICAL_DATA_SIZING,
                    dateFirstElementFirstPartition,
                    dateFirstElementSecondPartition);

        //Then
        assertEquals(2, actual.size());
        assertEquals(firstPartition.get(0), actual.get(0));
        assertEquals(secondPartition.get(0), actual.get(1));
    }

    private List<SensorMeasureHistoryDao> measureSequence(List<Pair<Date, Double>> timestampedMeasures){
        return timestampedMeasures.stream()
                .map(timestampedMeasure -> {
                    SensorMeasureHistoryDao dao = new SensorMeasureHistoryDao();
                    dao.setLocation(TEST_LOCATION); //Partition field
                    Date measureDate = timestampedMeasure.getFirst();
                    dao.setTimeBlockPeriodSeconds(TARGET_HISTORICAL_DATA_SIZING.getTimeBlockPeriodSeconds()); //Partition field
                    dao.setTimeBlockId(getHistoricalMeasureBlockId(measureDate, TARGET_HISTORICAL_DATA_SIZING)); //Partition field
                    dao.setMeasureType(TARGET_MEASURE_TYPE.name()); //Partition field
                    dao.setAggregateType(TARGET_AGGREGATE_TYPE.name()); //Partition field
                    dao.setBucket(SensorMeasureHistoryDao.SENSOR_MEASURE_DEFAULT_BUCKET_ID); //Partition field
                    dao.setTimestamp(measureDate);//Clustering field
                    dao.setValue(timestampedMeasure.getSecond());
                    return dao;
                }).collect(toList());
    }


    @After
    public void dropTable() {
        adminTemplate.dropTable(CqlIdentifier.of(SensorMeasureLatestDao.SENSOR_MEASURE_LATEST_TABLE_NAME));
    }
}