#!/bin/bash

function prepare()
{
        source ../conf/project.conf
        source ../conf/hadoop.conf
        source ../conf/mysql.conf

        mkdir -p ${LOCAL_DATA_PATH}

        return 0
}

function create_table_stat_article()
{
        table_name_fix=${YESTERDAY:0:4}${YESTERDAY:4:2}${YESTERDAY:6:2}

	echo "USE dashboard;" >${LOCAL_TMP_PATH}/create-daily-article.sql
    echo "
	CREATE TABLE t_daily_article_${table_name_fix} (
	  id int(32) unsigned NOT NULL AUTO_INCREMENT,
	  timestamp datetime NOT NULL,
	  article_id varchar(32) NOT NULL,
	  platform varchar(32) NOT NULL DEFAULT 'android',
	  app varchar(32) NOT NULL,
	  abtest_ver varchar(32) NOT NULL DEFAULT 'unknown',
	  scenario varchar(32) NOT NULL DEFAULT 'unknown',
	  scenario_channel varchar(32) DEFAULT 'unknown',
	  strategy varchar(32) NOT NULL DEFAULT 'unknown',
	  app_ver varchar(32) NOT NULL DEFAULT 'unknown',
	  language varchar(32) NOT NULL DEFAULT 'unknown',
	  ad_configid varchar(32) NOT NULL DEFAULT 'unknown',
	  biz_configid varchar(32) NOT NULL DEFAULT 'unknown',
	  view_mode varchar(32) NOT NULL DEFAULT 'unknown',
	  content_type varchar(32) NOT NULL DEFAULT 'unknown',
	  request int(12) DEFAULT NULL,
	  impression int(12) DEFAULT NULL,
	  click int(12) NOT NULL,
	  dwelltime int(12) NOT NULL,
	  user_type varchar(32) NOT NULL DEFAULT 'all',
	  PRIMARY KEY (id),
	  KEY article_id (article_id),
	  KEY platform (platform),
	  KEY app (app),
	  KEY app_abtest (abtest_ver),
	  KEY scenario (scenario),
	  KEY scenario_channel (scenario_channel),
	  KEY strategy (strategy),
	  KEY app_ver (app_ver),
	  KEY language (language),
	  KEY content_type (content_type),
	  KEY request (request)
	) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;
	" >>${LOCAL_TMP_PATH}/create-daily-article.sql
	mysql -h${MYSQL_HOST[2]} -P${MYSQL_PORT[2]} -u${MYSQL_USER[2]} -p${MYSQL_PWD[2]}<${LOCAL_TMP_PATH}/create-daily-article.sql

	return 0
}

function create_table_stat_article_operate()
{
	table_name_fix=${YESTERDAY:0:4}${YESTERDAY:4:2}${YESTERDAY:6:2}

    echo "USE dashboard;" >${LOCAL_TMP_PATH}/create-daily-article-operate.sql
	echo "
	CREATE TABLE t_daily_article_operate_${table_name_fix} (
	  id int(32) unsigned NOT NULL AUTO_INCREMENT,
	  timestamp datetime NOT NULL,
	  article_id varchar(32) NOT NULL,
	  platform varchar(32) NOT NULL DEFAULT 'android',
	  app varchar(32) NOT NULL,
	  scenario varchar(32) NOT NULL DEFAULT 'unknown',
	  scenario_channel varchar(32) NOT NULL DEFAULT 'unknown',
	  strategy varchar(32) NOT NULL DEFAULT 'unknown',
	  language varchar(32) NOT NULL DEFAULT 'unknown',
	  rate int(12) NOT NULL DEFAULT '0',
	  title varchar(256) DEFAULT NULL,
	  source varchar(64) DEFAULT NULL,
	  sourceType varchar(16) DEFAULT NULL,
	  sourceFeeds varchar(32) DEFAULT NULL,
	  publisher varchar(32) DEFAULT NULL,
	  adult_score varchar(64) DEFAULT NULL,
	  channel varchar(32) DEFAULT NULL,
	  discoveryTime varchar(32) DEFAULT NULL,
	  important_level int(12) DEFAULT NULL,
	  url varchar(255) DEFAULT NULL,
	  categories varchar(32) DEFAULT NULL,
	  flag varchar(32) NOT NULL DEFAULT 'unknown',
	  request int(12) DEFAULT NULL,
	  impression int(12) DEFAULT NULL,
	  click int(12) NOT NULL,
	  dwelltime int(12) NOT NULL,
	  user_type varchar(32) NOT NULL DEFAULT 'all',
	  PRIMARY KEY (id),
	  KEY article_id (article_id),
	  KEY platform (platform),
	  KEY app (app),
	  KEY scenario_channel (scenario_channel),
	  KEY strategy (strategy),
	  KEY language (language),
	  KEY source (source),
	  KEY sourceType (sourceType),
	  KEY publisher (publisher),
	  KEY categories (categories)
	) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;
	" >>${LOCAL_TMP_PATH}/create-daily-article-operate.sql
        mysql -h${MYSQL_HOST[2]} -P${MYSQL_PORT[2]} -u${MYSQL_USER[2]} -p${MYSQL_PWD[2]}<${LOCAL_TMP_PATH}/create-daily-article-operate.sql

	return 0
}

function drop_table()
{
	echo "USE dashboard;" >${LOCAL_TMP_PATH}/drop-daily-article.sql
        echo "
		DROP TABLE t_daily_article_${YESTERDAY_30DAY};
		DROP TABLE t_daily_article_operate_${YESTERDAY_30DAY};
	" >>${LOCAL_TMP_PATH}/drop-daily-article.sql
	mysql -h${MYSQL_HOST[2]} -P${MYSQL_PORT[2]} -u${MYSQL_USER[2]} -p${MYSQL_PWD[2]}<${LOCAL_TMP_PATH}/drop-daily-article.sql

	return 0
}

prepare

create_table_stat_article

create_table_stat_article_operate

#drop_table
