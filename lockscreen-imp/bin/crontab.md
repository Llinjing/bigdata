*/2 * * * * cd /data/apps/lockscreen-imp/bin && sh -x monitor-task.sh 1>../log/monitor-task.err 2>&1
*/1 * * * * cd /data/apps/lockscreen-imp/bin && sh -x monitor-acs-failed-data.sh 1>../log/monitor-acs-failed-data.err 2>&1
10 02 * * * cd /data/apps/lockscreen-imp/bin && sh -x history-keeper.sh 1>../log/history-keeper.err 2>&1
