package org.digitalpanda.backend.application.northbound.service;

import org.digitalpanda.backend.application.persistence.measure.history.HistoricalDataStorageSizing;
import org.digitalpanda.backend.application.persistence.measure.history.SensorMeasureHistoryDao;
import org.digitalpanda.backend.application.persistence.measure.history.SensorMeasureHistoryRepository;
import org.digitalpanda.backend.data.SensorMeasureType;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;
import java.util.*;

import static java.lang.Math.toIntExact;
import static java.util.stream.Collectors.toList;
import static org.digitalpanda.backend.application.persistence.measure.history.SensorMeasureHistoryDao.ROW_SIZE_BYTES;

public class SensorMeasureHistoryService {

    public static final double MAX_OUTPUT_MEASURES_JITTER = 1.2;
    public static final int MAX_ROW_COUNT = 55*1000*1000 / ROW_SIZE_BYTES; //~55 MiB of data ~= 581'395 data points with second period ~= 7.4 days sample size !

    private SensorMeasureHistoryRepository sensorMeasureHistoryRepository;

    @Autowired
    public SensorMeasureHistoryService(SensorMeasureHistoryRepository sensorMeasureHistoryRepository) {
        this.sensorMeasureHistoryRepository = sensorMeasureHistoryRepository;
    }

    public List<SensorMeasuresEquidistributed> getMeasuresWithContinuousEquidistributedSubIntervals(
            String location, SensorMeasureType sensorMeasureType, long startTimeMillisIncl, long endTimeMillisExcl, int dataPointCount) {
        HistoricalDataStorageSizing storageSizingWithNearestLowerPeriod = findHistoricalDataStorageSizingWithNearestLowerPeriod(startTimeMillisIncl, endTimeMillisExcl, dataPointCount);

        long trimmedEndTimeMillisIncl = endTimeMillisExcl;
        if((endTimeMillisExcl - startTimeMillisIncl) / (storageSizingWithNearestLowerPeriod.getTimeBlockPeriodSeconds()*1000)  > MAX_ROW_COUNT) {
            trimmedEndTimeMillisIncl = startTimeMillisIncl + (storageSizingWithNearestLowerPeriod.getTimeBlockPeriodSeconds() * 1000 * MAX_ROW_COUNT);
        }
        List<SensorMeasureHistoryDao> storageValuesTimeIncreasing = loadMeasuresIncreasingOrder(location, sensorMeasureType, startTimeMillisIncl, trimmedEndTimeMillisIncl, storageSizingWithNearestLowerPeriod);

        return resizeSample(startTimeMillisIncl, endTimeMillisExcl, dataPointCount, storageValuesTimeIncreasing, storageSizingWithNearestLowerPeriod);
    }

    private List<SensorMeasuresEquidistributed> resizeSample(long startTimeMillisIncl, long endTimeMillisExcl, int targetDataPointCount, List<SensorMeasureHistoryDao> storedMeasuresTimeIncreasing, HistoricalDataStorageSizing historicalDataStorageSizing) {
        if(storedMeasuresTimeIncreasing.size() == 0)
            return Collections.emptyList();

        final long targetPeriodMillis = (endTimeMillisExcl - startTimeMillisIncl) / targetDataPointCount;
        if (targetPeriodMillis < 1000)
            throw new RuntimeException("Historical data sample bellow second period is not supported");

        long storageDataSampleUnitPeriodMillis = historicalDataStorageSizing.getAggregateIntervalSeconds() * 1000;
        List<SensorMeasuresEquidistributed> sensorMeasuresResizedEquidistributedSubSamples = new ArrayList<>();
        List<SensorMeasureHistoryDao> currentSubIntervalMeasures = new ArrayList<>();
        long curentSubSampleStartIntervalMillis = storedMeasuresTimeIncreasing.get(0).getTimestamp().getTime();

        SensorMeasureHistoryDao previousMeasure = storedMeasuresTimeIncreasing.get(0);

        for (SensorMeasureHistoryDao currentMeasure : storedMeasuresTimeIncreasing) {
            //If the next measure creates a discontinuity:
            // 1) Perform resizing with the accumulated sub sample of data points
            // 2) Start a new sub sample interval
            if (currentMeasure.getTimestamp().getTime() > previousMeasure.getTimestamp().getTime() + (long) (targetPeriodMillis * MAX_OUTPUT_MEASURES_JITTER)) {
                sensorMeasuresResizedEquidistributedSubSamples.add(
                        resizeSubSampleToTargetDataPointPeriodWithAverage(
                            curentSubSampleStartIntervalMillis,
                            previousMeasure.getTimestamp().getTime(), // + storageDataSampleUnitPeriodMillis,
                            targetPeriodMillis,
                            currentSubIntervalMeasures
                ));

                curentSubSampleStartIntervalMillis = currentMeasure.getTimestamp().getTime();
                currentSubIntervalMeasures = new ArrayList<>();
            }

            currentSubIntervalMeasures.add(currentMeasure);
            previousMeasure = currentMeasure;
        }

        if(sensorMeasuresResizedEquidistributedSubSamples.size() == 0 ||
                sensorMeasuresResizedEquidistributedSubSamples.get(sensorMeasuresResizedEquidistributedSubSamples.size()-1).getStartTimeMillisIncl() != curentSubSampleStartIntervalMillis){
            sensorMeasuresResizedEquidistributedSubSamples.add(
                    resizeSubSampleToTargetDataPointPeriodWithAverage(
                            curentSubSampleStartIntervalMillis,
                            previousMeasure.getTimestamp().getTime(), // + storageDataSampleUnitPeriodMillis,
                            targetPeriodMillis,
                            currentSubIntervalMeasures
                    ));
        }

        return sensorMeasuresResizedEquidistributedSubSamples;
    }

    private SensorMeasuresEquidistributed resizeSubSampleToTargetDataPointPeriodWithAverage(long startTimeMillisIncl, long endTimeMillisExcl, long targetPeriodMillis, List<SensorMeasureHistoryDao> continuousStorageValuesTimeIncreasing){
        int targetDataPointCount = toIntExact((endTimeMillisExcl - startTimeMillisIncl) / targetPeriodMillis) + 1;
        List<Double> resizedSample = new ArrayList<>(targetDataPointCount);
        long nextPeriodStartTimeMillisIncl = startTimeMillisIncl + targetPeriodMillis;
        double accumulator = 0.0;
        int accCount = 0;
        for (SensorMeasureHistoryDao sensorMeasureDao : continuousStorageValuesTimeIncreasing) {
            if (sensorMeasureDao.getTimestamp().getTime() >= nextPeriodStartTimeMillisIncl) {
                nextPeriodStartTimeMillisIncl += targetPeriodMillis;
                if (accCount != 0) {
                    resizedSample.add(accumulator / accCount);
                } else {
                    resizedSample.add(0.0);
                }
                accCount = 0;
                accumulator = 0.0;
            }

            accumulator += sensorMeasureDao.getValue();
            accCount++;
        }
        if (accCount != 0) {
            resizedSample.add(accumulator / accCount);
        }

        return new SensorMeasuresEquidistributed(startTimeMillisIncl, startTimeMillisIncl + resizedSample.size() * targetPeriodMillis, targetPeriodMillis, resizedSample);
    }

    private HistoricalDataStorageSizing findHistoricalDataStorageSizingWithNearestLowerPeriod(long intervalBeginSecondsIncl, long intervalEndSecondsIncl, int dataPointCount) {
        //TODO: Refine computation once multiple HistoricalDataStorageSizing are available
        return HistoricalDataStorageSizing.SECOND_PRECISION_RAW;
    }

    private List<SensorMeasureHistoryDao> loadMeasuresIncreasingOrder(String location, SensorMeasureType sensorMeasureType, long startTimeMillisIncl, long endTimeMillisIncl, HistoricalDataStorageSizing storageSizingWithNearestPeriod) {
        //TODO: consider cassandra storage layout change : split table by aggregate granularity
        return sensorMeasureHistoryRepository
                .getMeasuresAtLocationWithInterval(
                        location,
                        sensorMeasureType,
                        storageSizingWithNearestPeriod.getAggregateType(),
                        storageSizingWithNearestPeriod,
                        startTimeMillisIncl,
                        endTimeMillisIncl).stream()
                .sorted(Comparator.comparingLong(sensorMeasureHistoryDao -> sensorMeasureHistoryDao.getTimestamp().getTime()))
                .collect(toList());
    }
}
