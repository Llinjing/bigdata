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
    applications_arr ={}
    for line_str in sys.stdin.readlines():
        line_str = line_str.strip()
	line_arr = line_str.split('\t')
	tmp_dict = {}
	tmp_dict['application_name'] = line_arr[0]
	tmp_dict['gmp_ge_8_percent_new_article_count'] = line_arr[1]
	tmp_dict['gmp_ge_7_percent_new_article_count'] = line_arr[2]
	##applications_arr.append(tmp_dict)
	applications_arr[line_arr[0]]=tmp_dict
    print json.dumps(applications_arr)
