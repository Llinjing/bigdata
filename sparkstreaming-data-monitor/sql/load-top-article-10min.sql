USE db_mta;
LOAD DATA LOCAL INFILE '/data/apps/sparkstreaming-data-monitor/data/article-feedback-impression-10min-t_top_article_10min.list' INTO TABLE t_top_article_10min FIELDS TERMINATED BY '\t' LINES TERMINATED BY '\n' IGNORE 0 LINES;
LOAD DATA LOCAL INFILE '/data/apps/sparkstreaming-data-monitor/data/article-feedback-impression-10min-t_top_article_stat_daily.list' INTO TABLE t_top_article_stat_daily FIELDS TERMINATED BY '\t' LINES TERMINATED BY '\n' IGNORE 0 LINES;
