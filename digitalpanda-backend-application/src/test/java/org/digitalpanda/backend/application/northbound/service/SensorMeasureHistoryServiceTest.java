package org.digitalpanda.backend.application.northbound.service;

import org.digitalpanda.backend.application.northbound.ressource.measure.SensorMeasureUiController;
import org.digitalpanda.backend.application.persistence.measure.history.AggregateType;
import org.digitalpanda.backend.application.persistence.measure.history.HistoricalDataStorageSizing;
import org.digitalpanda.backend.application.persistence.measure.history.SensorMeasureHistoryDao;
import org.digitalpanda.backend.application.persistence.measure.history.SensorMeasureHistoryRepository;
import org.digitalpanda.backend.application.util.Pair;
import org.digitalpanda.backend.data.SensorMeasureType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SensorMeasureHistoryServiceTest {

    private static final String TEST_LOCATION = "testLocation";
    private static final SensorMeasureType TEST_MEASURE_TYPE = SensorMeasureType.TEMPERATURE;
    private static final AggregateType TEST_AGGREGATE_TYPE = AggregateType.VALUE;
    private static final HistoricalDataStorageSizing DEFAULT_STORAGE_SIZING = HistoricalDataStorageSizing.SECOND_PRECISION_RAW;
    private static final long REF_EPOCH_MILLIS = 1540714000000L;

    @Mock
    private SensorMeasureHistoryRepository sensorMeasureHistoryRepositoryMock;
    private SensorMeasureHistoryService sensorMeasureHistoryService;

    @Before
    public void init() {
         this.sensorMeasureHistoryService = new SensorMeasureHistoryService(sensorMeasureHistoryRepositoryMock);
    }

    @Test
    public void getMeasuresWithContinuousEquidistributedSubIntervals_shouldReturnOneContinuousSubInterval() {
        //Given
        long intervalStartMillis = REF_EPOCH_MILLIS;
        long intervalEndMillis = REF_EPOCH_MILLIS + 6L* 1000L;
        List<Pair<Long, Double>> storedTimeValuePairs = buildTimeValuePairs(
                new long[]    {0, 1000, 2000,  3000, 4000, 5000},
                new double [] {1.0, 2.0, 3.0,  3.0, 4.0, 5.0}
        );
        SensorMeasuresEquidistributed expectedSubInterval = new SensorMeasuresEquidistributed(
                intervalStartMillis,
                intervalEndMillis,
                3000L,
                Arrays.asList(2.0, 4.0)
        );
        int expectedDataPointCount = 2;

        when(sensorMeasureHistoryRepositoryMock.getMeasuresAtLocationWithInterval(
                TEST_LOCATION, TEST_MEASURE_TYPE, TEST_AGGREGATE_TYPE, DEFAULT_STORAGE_SIZING, intervalStartMillis, intervalEndMillis))
                    .thenReturn(generateStorageData(storedTimeValuePairs));

        //When
        List<SensorMeasuresEquidistributed> actual = sensorMeasureHistoryService.getMeasuresWithContinuousEquidistributedSubIntervals(TEST_LOCATION, TEST_MEASURE_TYPE, intervalStartMillis, intervalEndMillis, expectedDataPointCount);

        //Then
        assertEquals(1, actual.size());
        assertEquals(expectedSubInterval, actual.get(0));
    }

    //TODO: Add more tests !!!!

    private List<Pair<Long, Double>> buildTimeValuePairs(long [] timestampsDeltaMillis, double [] values){
        if(timestampsDeltaMillis.length != values.length)
            throw new RuntimeException(
                    String.format("timestampsDeltaMillis and value arrays should have the same length : timestampsDeltaMillis.length=%d ; values.length=%d",
                            timestampsDeltaMillis.length, values.length));

        List<Pair<Long, Double>> timeValuePairs = new ArrayList<>(timestampsDeltaMillis.length);
        for (int i = 0; i < timestampsDeltaMillis.length; i++) {
            timeValuePairs.add(new Pair<>(REF_EPOCH_MILLIS + timestampsDeltaMillis[i], values[i]));
        }

        return timeValuePairs;
    }

    private List<SensorMeasureHistoryDao> generateStorageData(List<Pair<Long, Double>> timeValuePairs) {
        return timeValuePairs.stream()
                .map(timeValuePair ->
                        new SensorMeasureHistoryDao(
                                TEST_LOCATION,
                                DEFAULT_STORAGE_SIZING.getTimeBlockPeriodSeconds(),
                                -1L,
                                TEST_MEASURE_TYPE.name(),
                                TEST_AGGREGATE_TYPE.name(),
                                0,
                                Date.from(Instant.ofEpochMilli(timeValuePair.getFirst())),
                                timeValuePair.getSecond()
                        ))
                .collect(toList());
    }

}