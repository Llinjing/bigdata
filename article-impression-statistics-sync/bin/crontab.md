## 
*/10 * * * * cd /data1/apps/impression-stat-sync/bin && sh -x monitor.sh 1>../log/monitor.err 2>&1
05 01 * * * cd /data1/apps/impression-stat-sync/bin && sh -x history-keeper.sh 1>../log/history-keeper.err 2>&1
