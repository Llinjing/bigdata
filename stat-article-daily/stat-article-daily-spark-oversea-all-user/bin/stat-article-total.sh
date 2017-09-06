#!/bin/bash

function prepare()
{
        source ../conf/project.conf
        source ../conf/hadoop.conf
        source ../conf/mysql.conf

        mkdir -p ${LOCAL_DATA_PATH}

        return 0
}

function stat_article_total()
{
	mysql -h${MYSQL_HOST[3]} -P${MYSQL_PORT[3]} -u${MYSQL_USER[3]} -p${MYSQL_PWD[3]} -e"
	USE cms;
	SELECT content_id, request, impression, click, dwelltime from sm_statistic_article_total where timestamp='${BEFORE_YESTERDAY_FORMAT}';
	" >${LOCAL_DATA_PATH}/article-total-stat.list

	mysql -h${MYSQL_HOST[3]} -P${MYSQL_PORT[3]} -u${MYSQL_USER[3]} -p${MYSQL_PWD[3]} -e"
        USE cms;
        SELECT content_id, request, impression, click, dwelltime from sm_statistic_article_daily where timestamp='${YESTERDAY_FORMAT}';
        " >${LOCAL_DATA_PATH}/article-daily-stat.list

	awk 'BEGIN{
		FS = "\t"
		OFS = "\t"
	}{
		if (ARGIND == 1){
			if (NR > 1){
				request[$1] += $2
                impression[$1] += $3
                click[$1] += $4
                dwelltime[$1] += $5
			}
		} 
		else if (ARGIND == 2) {
			if (NR > 1){
				request[$1] += $2
                impression[$1] += $3
                click[$1] += $4
                dwelltime[$1] += $5
			}
		}
	}END{
		timestamp = sprintf("%s-%s-%s 00:00:00", 
                        substr("'${YESTERDAY}'", 1, 4),
                        substr("'${YESTERDAY}'", 5, 2),
                        substr("'${YESTERDAY}'", 7, 2))

		for (id in request) {
			print "0", timestamp, id, (request[id]+0), (impression[id]+0), (click[id]+0), (dwelltime[id]+0)
		}
	}' ${LOCAL_DATA_PATH}/article-total-stat.list ${LOCAL_DATA_PATH}/article-daily-stat.list >${LOCAL_DATA_PATH}/article-total-stat-finish.list

	echo "USE cms;" >${LOCAL_TMP_PATH}/load-statistic-article-total.sql
    echo "LOAD DATA LOCAL INFILE '${LOCAL_DATA_PATH}/article-total-stat-finish.list' INTO TABLE \
                sm_statistic_article_total FIELDS TERMINATED BY '\t' LINES TERMINATED BY '\n' IGNORE 0 LINES;" \
                >> ${LOCAL_TMP_PATH}/load-statistic-article-total.sql

    mysql -h${MYSQL_HOST[3]} -P${MYSQL_PORT[3]} -u${MYSQL_USER[3]} -p${MYSQL_PWD[3]} \
                < ${LOCAL_TMP_PATH}/load-statistic-article-total.sql

	return 0
}

function stat_source_total()
{
	mysql -h${MYSQL_HOST[3]} -P${MYSQL_PORT[3]} -u${MYSQL_USER[3]} -p${MYSQL_PWD[3]} -e"
        USE cms;
	    set names 'utf8';
        SELECT source, request, impression, click, dwelltime, article_count from sm_statistic_source_total where timestamp='${BEFORE_YESTERDAY_FORMAT}';
        " >${LOCAL_DATA_PATH}/article-operate-total-stat.list

        mysql -h${MYSQL_HOST[3]} -P${MYSQL_PORT[3]} -u${MYSQL_USER[3]} -p${MYSQL_PWD[3]} -e"
        USE cms;
	    set names 'utf8';
        SELECT source, request, impression, click, dwelltime, article_count from sm_statistic_source_daily where timestamp='${YESTERDAY_FORMAT}';
        " >${LOCAL_DATA_PATH}/article-operate-daily-stat.list

	awk 'BEGIN{
		FS = "\t"
		OFS = "\t"
	}{
		if (ARGIND == 1){
                if (NR > 1){
                      request[$1] += $2
                      impression[$1] += $3
                      click[$1] += $4
                      dwelltime[$1] += $5
				      article_count[$1] += $6
                }
        } 
        else if (ARGIND == 2) {
                if (NR > 1){
                      request[$1] += $2
                      impression[$1] += $3
                      click[$1] += $4
                      dwelltime[$1] += $5
				      article_count[$1] += $6
                }
        }
	}END{
		timestamp = sprintf("%s-%s-%s 00:00:00", 
                        substr("'${YESTERDAY}'", 1, 4),
                        substr("'${YESTERDAY}'", 5, 2),
                        substr("'${YESTERDAY}'", 7, 2))

                for (id in request) {
                        print "0", timestamp, id, (request[id]+0), (impression[id]+0), (click[id]+0), (dwelltime[id]+0), (article_count[id]+0)
                }
	}' ${LOCAL_DATA_PATH}/article-operate-total-stat.list \
		${LOCAL_DATA_PATH}/article-operate-daily-stat.list \
	>${LOCAL_DATA_PATH}/article-operate-total-stat-finish.list

	echo "USE cms;" >${LOCAL_TMP_PATH}/load-statistic-source-total.sql
    echo "LOAD DATA LOCAL INFILE '${LOCAL_DATA_PATH}/article-operate-total-stat-finish.list' INTO TABLE \
                sm_statistic_source_total FIELDS TERMINATED BY '\t' LINES TERMINATED BY '\n' IGNORE 0 LINES;" \
                >> ${LOCAL_TMP_PATH}/load-statistic-source-total.sql

    mysql -h${MYSQL_HOST[3]} -P${MYSQL_PORT[3]} -u${MYSQL_USER[3]} -p${MYSQL_PWD[3]} \
                < ${LOCAL_TMP_PATH}/load-statistic-source-total.sql

	return 0
}

prepare

stat_article_total

stat_source_total
