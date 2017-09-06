#!/bin/bash

#!/bash/bin

function start()
{
        source ../conf/project.conf

        java -Djava.ext.dirs=${LOCAL_LIB_PATH}/ \
             -Dlog4j.configuration=file:${LOCAL_CONF_PATH}/log4j-advertisement-info-aggregator.properties \
             com.inveno.news.advertisement.info.aggregator.AdInfoAggregator ${LOCAL_CONF_PATH}/mysql.properties ${LOCAL_CONF_PATH}/redis.properties 15

        return 0
}

start


##crontab task as follow
##*/10 * * * * cd /home/bigdata/apps/advertisement-info-aggregator/bin && sh -x start.sh >> /home/bigdata/apps/advertisement-info-aggregator/log/start.sh.log 2>&1