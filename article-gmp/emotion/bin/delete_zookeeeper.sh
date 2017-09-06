#!/bin/bash

ips="10.10.20.14:2181,10.10.20.15:2181,10.10.20.16:2181"
path="/test/huanghaifeng/offset/test"

/usr/local/zookeeper/bin/zkCli.sh -server ${ips} rmr ${path}


