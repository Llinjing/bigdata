##############################
#监控导出任务进程,如挂掉，则拉起
#
##############################

source ../conf/project.conf
source ../conf/warning.conf


for((m=0;m<${APP_NUM};++m))
do
    pid=`ps -ef | grep "${APP_NAME[$m]}"|grep -v grep|grep -v check|awk '{print $2}'`
    if [ -z "${pid}" ]
    then
         ${WARNING_BIN} "${PRODUCT_LINE}:_${APP_NAME[$m]} exporter service is_not_running, service will restart!" "${SMS_USER[$FATAL]}"
         #sh -x stop-${APP_NAME[$m]}.sh
        `nohup sh -x start-${APP_NAME[$m]}.sh 1> ../log/${APP_SCRIPT[$1]}.log 2>&1 &`
    fi
done
