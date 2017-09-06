
# add by panhongan
*/10 * * * * offline cd /data/apps/reformat/bin && sh -x monitor.sh 1>/dev/null 2>&1

05 01 * * * offline cd /data/apps/reformat/bin && sh -x history-keeper.sh 1>../log/history-keeper.err 2>&1
