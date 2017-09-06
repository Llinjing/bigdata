
TOMCAT_HOME=/data/software/apacche-tomcat-8.0.39

cp bin/catalina.sh ${TOMCAT_HOME}/bin
cp conf/server.xml ${TOMCAT_HOME}/conf
cp target/hubble-web.war ${TOMCAT_HOME}/webapps

import sql/t_history_task.sql

sh -x shutdown.sh
sh -x startup.sh
