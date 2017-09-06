#!/bin/bash

ips="172.31.4.53:2181,172.31.4.54:2181,172.31.4.55:2181"
path="/article-gmp/spark-streaming/offset/article-gmp-spark-streaming"

for ((i=0; i<9; i++))  
do  
    /usr/local/zookeeper/bin/zkCli.sh -server ${ips} delete ${path}/impression-reformat_${i}  
    /usr/local/zookeeper/bin/zkCli.sh -server ${ips} delete ${path}/click-reformat_${i}  
    /usr/local/zookeeper/bin/zkCli.sh -server ${ips} delete ${path}/article-gmp_${i}  
done

