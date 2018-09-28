

/*
    Goal : Get all the ordered measures within sub-day intervals for a location
*/
CREATE TABLE IF NOT EXIST sensor_measure_history_second_precision_by_day (
    location text,
    day timestamp,
    bucket int,
    measureType text,
    ts timestamp,
    measureValue float,
    primary key((location, day, bucket), ts)
) WITH CLUSTERING ORDER BY (ts DESC)
         AND COMPACTION = {'class': 'TimeWindowCompactionStrategy',
                       'compaction_window_unit': 'DAYS',
                       'compaction_window_size': 1}
         AND default_time_to_live = 365 * 24 * 3600;
/*
    > TWCS allows to compact rows into the same SS table with insertion time locality
    > Only appropriate for strictly ordered time series insertion
*/


/*
    Goal: Get the all the latest measure of any measure identified by location and type
    + Scale with the number of locations
*/
CREATE TABLE IF NOT EXIST sensor_measure_latest (
   location text,
   measureType text,
   ts timestamp,
   measureValue float,
   primary key((location), measureType)
) WITH COMPACTION = {'class': 'LeveledCompactionStrategy',
                      'tombstone_compaction_interval': '1800'};

/*
    > LCS is read optimized and guarantees efficient data compaction
    > High SStable compaction background cost.
*/