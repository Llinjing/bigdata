#!/usr/bin/env python
# coding=utf-8

'''
@author: huanghaifeng
'''

import sys
import string

if __name__ == "__main__":
    reload(sys)
    sys.setdefaultencoding('utf-8')
    for line_str in sys.stdin.readlines():
        try:
            line_str = line_str.strip()
            line_str = line_str.replace("###", "\t")
            print line_str
        except:
            continue
