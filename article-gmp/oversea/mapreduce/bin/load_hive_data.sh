export HADOOP_HOME="/usr/lib/hadoop/"
/usr/lib/hive/bin/hive -e "set mapred.job.queue.name=realtime; alter table article_gmp add partition (date='20161105', min='0950') location '/inveno-projects/offline/article-gmp/data/article-gmp/history/20161105/0950/'"
