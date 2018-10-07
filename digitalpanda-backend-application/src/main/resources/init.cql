CREATE KEYSPACE IF NOT EXISTS iot
WITH durable_writes = true
AND replication = { 'replication_factor' : 1, 'class' : 'SimpleStrategy' };

/*
    Goal : Get all the ordered measures within sub-day intervals for a location
        > TWCS allows to compact rows into the same SS table with insertion time locality
        > Only appropriate for strictly ordered time series insertion
*/
CREATE TABLE IF NOT EXISTS iot.sensor_measure_history_second_precision_by_day (
    location text,
    day timestamp,
    bucket int,
    measureType text,
    timestamp timestamp,
    measureValue double,
    primary key((location, day, bucket), timestamp)
) WITH CLUSTERING ORDER BY (timestamp DESC)
         AND COMPACTION = {'class': 'TimeWindowCompactionStrategy',
                       'compaction_window_unit': 'DAYS',
                       'compaction_window_size': 1}
         AND default_time_to_live = 31536000; -- 365 * 24 * 3600

/*
    Goal: Get the all the latest measure of any measure identified by location and type
    + Scale with the number of locations
        > LCS is read optimized and guarantees efficient data compaction
        > High SStable compaction background cost.
*/
CREATE TABLE IF NOT EXISTS iot.sensor_measure_latest (
   location text,
   measureType text,
   timestamp timestamp,
   measureValue double,
   primary key((location), measureType)
) WITH COMPACTION = {'class': 'LeveledCompactionStrategy',
                      'tombstone_compaction_interval': '1800'};