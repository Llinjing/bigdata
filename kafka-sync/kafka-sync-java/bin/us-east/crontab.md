*/5 * * * * cd /data/apps/kafka-sync-service/bin && sh -x monitor.sh 1>../log/monitor.err 2>&1
00 01 * * * cd /data/apps/kafka-sync-service/bin && sh -x history-keeper.sh 1>../log/history-keeper.err 2>&1
