USE dashboard;
LOAD DATA LOCAL INFILE '/data/apps/offline-advertisement-stat/data/advertiser-finish.list' INTO TABLE 		t_daily_ad_advertiser FIELDS TERMINATED BY '\t' LINES TERMINATED BY '\n' IGNORE 1 LINES;
