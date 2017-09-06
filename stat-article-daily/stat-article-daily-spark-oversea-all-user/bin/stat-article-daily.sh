#!/bin/bash

function prepare()
{
        source ../conf/project.conf
        source ../conf/hadoop.conf
        source ../conf/mysql.conf

        mkdir -p ${LOCAL_DATA_PATH}

        return 0
}

function stat_article_daily()
{
	mysql -h${MYSQL_HOST[1]} -P${MYSQL_PORT[1]} -u${MYSQL_USER[1]} -p${MYSQL_PWD[1]} -e"
	USE db_mta;
    set names utf8;
    SELECT source FROM t_subscription_profile GROUP BY source;
	" >${LOCAL_DATA_PATH}/query-article-source.list

    mysql -h${MYSQL_HOST[2]} -P${MYSQL_PORT[2]} -u${MYSQL_USER[2]} -p${MYSQL_PWD[2]} -e"
    USE dashboard;
    set names utf8;
    SELECT source,article_id,sum(request),sum(impression),sum(click),sum(dwelltime) FROM t_daily_article_operate_${YESTERDAY} WHERE user_type='all' GROUP BY source,article_id;
    " >${LOCAL_DATA_PATH}/query-article-operate.list

    awk 'BEGIN{
        FS = OFS = "\t"
    }{
        if (ARGIND==1) {
            dict[$1]
        } else if (ARGIND==2) {
            if ($1 in dict) {
                all_id[$2]
                request[$2] += $3
                impression[$2] += $4
                click[$2] += $5
                dwelltime[$2] += $6
            }
        }
    } END {
        timestamp = sprintf("%s-%s-%s 00:00:00", 
                        substr("'${YESTERDAY}'", 1, 4),
                        substr("'${YESTERDAY}'", 5, 2),
                        substr("'${YESTERDAY}'", 7, 2))
        for (id in all_id) {
            print "0", timestamp, id, (request[id]+0), (impression[id]+0), (click[id]+0), (dwelltime[id]+0)
        }
    }' ${LOCAL_DATA_PATH}/query-article-source.list \
        ${LOCAL_DATA_PATH}/query-article-operate.list >${LOCAL_DATA_PATH}/query-stat-article-finish.list

	echo "USE cms;" >${LOCAL_TMP_PATH}/load-statistic-article-daily.sql
    echo "LOAD DATA LOCAL INFILE '${LOCAL_DATA_PATH}/query-stat-article-finish.list' INTO TABLE \
                sm_statistic_article_daily FIELDS TERMINATED BY '\t' LINES TERMINATED BY '\n' IGNORE 1 LINES;" \
                >> ${LOCAL_TMP_PATH}/load-statistic-article-daily.sql

    mysql -h${MYSQL_HOST[3]} -P${MYSQL_PORT[3]} -u${MYSQL_USER[3]} -p${MYSQL_PWD[3]} \
                < ${LOCAL_TMP_PATH}/load-statistic-article-daily.sql

	return 0
}

function stat_source_daily()
{
    mysql -h${MYSQL_HOST[1]} -P${MYSQL_PORT[1]} -u${MYSQL_USER[1]} -p${MYSQL_PWD[1]} -e"
    USE db_mta;
    set names utf8;
    SELECT source FROM t_subscription_profile GROUP BY source;
    " >${LOCAL_DATA_PATH}/query-article-source.list

	mysql -h${MYSQL_HOST[2]} -P${MYSQL_PORT[2]} -u${MYSQL_USER[2]} -p${MYSQL_PWD[2]} -e"
    USE dashboard;
	set names utf8;
    SELECT source, sum(request), sum(impression), sum(click), sum(dwelltime) FROM t_daily_article_operate_${YESTERDAY} WHERE user_type='all' GROUP BY source;
     " >${LOCAL_DATA_PATH}/query-article-operate.list

    mysql -h${MYSQL_HOST[2]} -P${MYSQL_PORT[2]} -u${MYSQL_USER[2]} -p${MYSQL_PWD[2]} -e"
    USE dashboard;
    set names utf8;
    SELECT source,sum(article_available_amount) from t_article_publish WHERE timestamp_day='${YESTERDAY_FORMAT}' AND product_id='all' AND language='all' AND content_type='all' AND body_images_count='all' group by source;
    " >${LOCAL_DATA_PATH}/query-source-amount.list

    awk 'BEGIN{
        FS = OFS = "\t"
    }{
        if (ARGIND == 1) {
            dict[$1] = $2
        } else if (ARGIND == 2) {
            if ($1 in dict) {
                print $0, dict[$1]
            } else {
                print $0, 0
            }
        }
    }' ${LOCAL_DATA_PATH}/query-source-amount.list \
    ${LOCAL_DATA_PATH}/query-article-operate.list >${LOCAL_DATA_PATH}/query-stat-article-operate-finish-tmp.list

    awk 'BEGIN{
        FS = OFS = "\t"
    }{
        timestamp = sprintf("%s-%s-%s 00:00:00", 
                        substr("'${YESTERDAY}'", 1, 4),
                        substr("'${YESTERDAY}'", 5, 2),
                        substr("'${YESTERDAY}'", 7, 2))
        if (ARGIND == 1) {
            dict[toupper($1)]
        } else if (ARGIND == 2) {
            if (toupper($1) in dict) {
                print "0", timestamp, $0
            }
        }
    }' ${LOCAL_DATA_PATH}/query-article-source.list \
    ${LOCAL_DATA_PATH}/query-stat-article-operate-finish-tmp.list >${LOCAL_DATA_PATH}/query-stat-article-operate-finish.list

	echo "USE cms;" >${LOCAL_TMP_PATH}/load-statistic-source-daily.sql
    echo "LOAD DATA LOCAL INFILE '${LOCAL_DATA_PATH}/query-stat-article-operate-finish.list' INTO TABLE \
          sm_statistic_source_daily FIELDS TERMINATED BY '\t' LINES TERMINATED BY '\n' IGNORE 1 LINES;" \
          >> ${LOCAL_TMP_PATH}/load-statistic-source-daily.sql

    mysql -h${MYSQL_HOST[3]} -P${MYSQL_PORT[3]} -u${MYSQL_USER[3]} -p${MYSQL_PWD[3]} \
                < ${LOCAL_TMP_PATH}/load-statistic-source-daily.sql

	return 0
}

prepare

stat_article_daily

stat_source_daily
