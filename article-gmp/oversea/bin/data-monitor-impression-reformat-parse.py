#!/usr/bin/env python
# coding=utf-8

'''
@author: huanghaifeng
'''

import sys
import string
import time
try:
    import simplejson as json
except:
    import json

if __name__ == "__main__":
    reload(sys)
    sys.setdefaultencoding('utf-8')
    timestamp = time.strftime('%Y%m%d',time.localtime(time.time()))
    for line_str in sys.stdin.readlines():
        line_str = line_str.strip()
        try:
            line_json = json.loads(line_str)
            product_id = line_json['product_id']
            content_id = line_json['article_request_extra']['content_id']
            language = line_json['app_lan']
            content_type = line_json['article_request_extra']['content_type']
            if 'advertisement' not in content_type:
                print content_id +'\t'+ language +'\t'+ content_type +'\t'+ timestamp
        except:
            sys.stderr.write("invalid data, failed get json data: " + line_str + "\n")
            continue
