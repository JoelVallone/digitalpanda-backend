package org.digitalpanda.backend.application.persistence.measure.history;

/*
Web user : < 200 ms responsiveness
    -> api call :   metadata = (start millis (9 Bytes), end millis (9 Bytes), aggregate type (20 Bytes), aggregate interval millis (9 Bytes), measure type (20 Bytes));
                    data = List of [0-9]{5}.[0-9]{4} (10 Bytes) values
    With 3G basic connectivity 300 KiB/s => 60 KiB payload
        => Max 6 K measures for any interval
        => Throttle request size on client side,
            If needed, recompute aggregates in backend service,
            Historical data in DB can be pre/batch-computed but obeys Cassandra constraints :
                -> Evenly sized partitions, target partition size, time-ordered insertions, dynamic TTL ...

Cassandra: Maximum partition size : 50 MiB,
    -> sensor_measure_history row max size : 100 Bytes
    => 500 k records
*/
public enum HistoricalDataStorageSizing {
    SECOND_PRECISION_RAW(1L, AggregateType.VAL); //Raw data provided by sensor network
    //MINUTE_PRECISION_AGGREGATE(60L, AggregateType.AVG); //TODO: Possible DB-stored output of batch processing aggregate on sensor network raw data

    public static final long MAX_TABLE_PARTITION_SIZE_BYTES = 50 * (1L << 20);
    public static final long MEASURE_HISTORY_ROW_SIZE_BYTES = 100L;
    public static final long RECORDS_PER_TABLE_PARTITION  = (MAX_TABLE_PARTITION_SIZE_BYTES / MEASURE_HISTORY_ROW_SIZE_BYTES);

    private Long timeBlockPeriodSeconds;
    private Long aggregateIntervalSeconds;
    private AggregateType aggregateType;


    HistoricalDataStorageSizing(Long aggregateIntervalSeconds, AggregateType aggregateType){
        this.aggregateIntervalSeconds = aggregateIntervalSeconds;
        this.timeBlockPeriodSeconds = aggregateIntervalSeconds * RECORDS_PER_TABLE_PARTITION;
        this.aggregateType = aggregateType;
    }

    public Long getTimeBlockPeriodSeconds() {
        return timeBlockPeriodSeconds;
    }

    public Long getAggregateIntervalSeconds() {
        return aggregateIntervalSeconds;
    }

    public AggregateType getAggregateType() {
        return aggregateType;
    }
}
