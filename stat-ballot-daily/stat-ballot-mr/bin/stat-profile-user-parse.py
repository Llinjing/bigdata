#!/usr/bin/env python
# coding=utf-8

'''
@author: zhangrujing
'''

import sys
import string

reload(sys)
sys.setdefaultencoding('utf-8')

try:
    import simplejson as json
except:
    import json

if __name__ == "__main__":
    for line_str in sys.stdin.readlines():
        line_str = line_str.strip()
        try:
            line_json = json.loads(line_str)
            uid = line_json['uid']
        except:
            continue

        print uid
