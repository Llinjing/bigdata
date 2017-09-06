#!/usr/bin/env python
#-*-coding:utf8-*-

'''
@author: haifeng.huang
'''

import sys
import string

try:
    import simplejson as json
except:
    import json

if __name__ == "__main__":
    reload(sys)
    sys.setdefaultencoding('utf-8')
    for line_str in sys.stdin.readlines():
        line_str = line_str.strip()
        line_json = json.loads(line_str)
        print line_json["uid"]
