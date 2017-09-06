#!/usr/bin/env python
# coding=utf-8

'''
@author: zhangrujing
'''

import sys
import string

if __name__ == "__main__":
    reload(sys)
    sys.setdefaultencoding('utf-8')
    for line_str in sys.stdin.readlines():
        line_str = line_str.strip()
        try:
		line_str = line_str.replace("(", "").replace(")", "").replace(",", "\t").replace("##", "\t")
		print line_str
        except:
		continue
