#!/bin/bash
#!/bin/bash

function prepare()
{
    source ../conf/project.conf
    source ../conf/warning.conf

    return 0
}

function monitor_service()
{
    for((i=0;i<${TASK_NUM};++i))
    do
        local pid=`ps -ef | grep ${TASK_NAME[$i]}\.properties | grep -v "grep" | awk '{print $2}'`
        if [ -z "${pid}" ]
        then
            ${WARNING_BIN} "${PRODUCT_LINE} : ${TASK_NAME[$i]} is not running, will restart" ${SMS_USER[$ERROR]}
            sh -x ${LOCAL_BIN_PATH}/stop-${TASK_NAME[$i]}.sh
            sh -x ${LOCAL_BIN_PATH}/start-${TASK_NAME[$i]}.sh
        fi
    done

    return 0
}


prepare

monitor_service
