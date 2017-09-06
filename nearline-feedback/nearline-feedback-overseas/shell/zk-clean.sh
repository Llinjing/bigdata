#!/bin/bash
source ../conf/project.conf

if [ $# -eq 1 ]
then
    GROUP=$1
    echo "Group Name : ${GROUP}"
    ${ZK_CLIENT} -server ${ZK_HOSTS} rmr ${ZK_BASE}/${GROUP}
fi
