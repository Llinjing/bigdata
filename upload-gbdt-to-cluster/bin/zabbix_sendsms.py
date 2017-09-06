#!/usr/bin/python
#coding:utf-8 

import os
import sys
import urllib
import urllib2

def send_sms(content,p_num):
    content = content.replace(" ", "_").replace("\t", "_")
    url = 'http://14.17.121.230:8001/moniters/?content=' + content + '&num=' + p_num
    
    print url
    try:
        conn = urllib2.urlopen(url)
        print conn.read()
        return True
    except Exception, e:
        info = sys.exc_info()
        sys.stderr.write(str(info[0]) + ":" + str(info[1]) + "\n")
        return False

if __name__ == "__main__":
    send_sms(sys.argv[1],sys.argv[2])
