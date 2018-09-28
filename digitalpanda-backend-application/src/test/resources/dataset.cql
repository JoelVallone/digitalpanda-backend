CREATE TABLE  sensor_measure_history (
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
                       'compaction_window_size': 1};

CREATE TABLE  sensor_measure_latest (
   location text,
   measureType text,
   ts timestamp,
   measureValue float,
   primary key((location, measureType))
) WITH COMPACTION = {'class': 'TimeWindowCompactionStrategy',
                      'compaction_window_unit': 'SECONDS',
                      'compaction_window_size': 3600};

INSERT INTO sensor_measure(location, day, bucket, measureType, ts, measureValue) values
                    ('locationA', '2018-09-21', 0, 'TEMPERATURE', 1537531133378, 42.200 );