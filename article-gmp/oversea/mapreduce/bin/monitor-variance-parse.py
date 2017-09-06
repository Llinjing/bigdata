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
    applications_arr = {}
    for line_str in sys.stdin.readlines():
        line_str = line_str.strip()
	line_arr = line_str.split('\t')
	tmp_dict = {}
        tmp_dict["application_name"] = line_arr[0]
        tmp_dict["[0,0.0001)"] = line_arr[1]
        tmp_dict["[0.0001,0.0003)"] = line_arr[2]
        tmp_dict["[0.0003,0.0005)"] = line_arr[3]
        tmp_dict["[0.0005,0.0007)"] = line_arr[4]
        tmp_dict["[0.0007,0.0009)"] = line_arr[5]
        tmp_dict["[0.0009,)"] = line_arr[6]
	##applications_arr.append(tmp_dict)
	applications_arr[line_arr[0]]=tmp_dict
    print json.dumps(applications_arr)
