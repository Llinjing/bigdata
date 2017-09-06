#!/usr/bin/env python
# coding=utf-8

'''
@author: huanghaifeng
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
	line_arr = line_str.split('\t')
	tmp_dict = {}
	tmp_dict['content_id'] = line_arr[0]
	tmp_dict['product_id'] = line_arr[1]
	tmp_dict['decay_click'] = line_arr[2]
	tmp_dict['decay_impression'] = line_arr[3]
	tmp_dict['update_date'] = line_arr[5]
	tmp_dict['total_impression'] = line_arr[6]
    	print json.dumps(tmp_dict)

## from
## 10104306	coolpad	0.214626	0	-1	20161225	0
## to
## {"decay_impression":79.69663,"content_id":"58555209","decay_click":2.4489408,"product_id":"xiaolajiao","total_impression":155,"update_date":"20161227"}
