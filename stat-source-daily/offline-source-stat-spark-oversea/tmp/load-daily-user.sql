USE dashboard;
LOAD DATA LOCAL INFILE '/data/apps/offline-stat-source-spark/data/stat-user-result.list' INTO TABLE                 t_daily_source FIELDS TERMINATED BY '\t' LINES TERMINATED BY '\n' IGNORE 0 LINES;
