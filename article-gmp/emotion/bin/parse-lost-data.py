#!/usr/bin/env python
# coding=utf-8

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
            line_json['decay_click'] = line_json['decay_click']*0.995
            line_json['decay_impression'] = line_json['decay_impression']*0.995
            print json.dumps(line_json)
        except:
            continue
        
