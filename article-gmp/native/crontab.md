 ## article-gmp-spark-streaming
 05,15,25,35,45,55 * * * * bigdata cd /home/bigdata/apps/article-gmp-sparkstreaming/bin && sh -x data-monitor.sh 1>../log/data-monitor.log 2>&1
 */10 * * * * bigdata cd /home/bigdata/apps/article-gmp-sparkstreaming/bin && sh -x job-monitor.sh 1>../log/job-monitor.log 2>&1