#!/bin/bash

function prepare()
{
	source ../conf/project.conf
	return 0
}

function history_log_clear()
{
    rm ../log/*${LOG_HISTORY_CLEAR}
}


prepare
history_log_clear

