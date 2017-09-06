
#!/usr/bin/env python
# coding=utf-8

'''
@author: kdp
'''
import sys
sys.path.append('gen-py')
try:
    import simplejson as json
except:
    import json
import datetime
import time
import traceback

import string
import MySQLdb

if __name__ == "__main__":
    reload(sys)
    sys.setdefaultencoding('utf-8')

    db = MySQLdb.connect("dashboard.cs8e5v1irf4y.ap-southeast-1.rds.amazonaws.com","bigdata","bigdata#2016","dashboard" )
    cursor = db.cursor()
    starttime = datetime.datetime.now()
    try:
	check = 0
    	for line_str in sys.stdin.readlines():
        	line_str = line_str.strip()
		arra = line_str.split('\t')
		if ( (len(arra) < 16) or (arra[2] == 'product_id') ):
			continue
		if ( check == 0 ):
                        select_sql = "select count(*) from t_user_ltv where last_active_time like '%s%%'" % arra[1]
			print select_sql
			cursor.execute(select_sql)
	                data = cursor.fetchone()
			print data[0]
			if ( int(data[0]) >0 ):
				print "today has been stated already!"
				sys.exit(0)
			else:
				check = 1	
		select_sql = "select * from t_user_ltv where product_id = '%s' and app_ver = '%s' and language = '%s' and promotion = '%s' and config_id = '%s' and protocol = '%s' and content_type = '%s' and scenario = '%s' and uid = '%s'" % \
		 (arra[2],arra[3],arra[4],arra[5],arra[6],arra[7],arra[8],arra[9],arra[10])
	    	cursor.execute(select_sql)
    		data = cursor.fetchone()
		if ( data == None):	
			cursor.execute("select date_add(create_time, interval -8 hour) from t_user_profile where uid = '%s'" % arra[10])
			ctimeresult = cursor.fetchone()
			#ctime = (ctimeresult == None) and '' or ctimeresult[0]
			ctime = '' if ctimeresult == None else ctimeresult[0]
			#print ctime
			if (arra[10] == '' or arra[10] == None):
				continue
			insert_sql = "insert into t_user_ltv (last_active_time,product_id,app_ver,language,promotion,config_id,protocol,content_type,scenario,uid,total_request,total_click,total_detail_dwell_time,total_impression,total_impression_all,create_time) \
			values ('%s','%s','%s','%s','%s','%s','%s','%s','%s','%s',%d,%d,%d,%d,%d,'%s')" % (arra[1],arra[2],arra[3],arra[4],arra[5],arra[6],arra[7],arra[8],arra[9],arra[10],int(arra[11]),int(arra[12]),long(arra[13]),int(arra[14]),int(arra[15]),ctime)
			#print insert_sql
			cursor.execute(insert_sql)
		else:
			update_sql = "update t_user_ltv set total_request = total_request+%d,total_click = total_click+%d,total_detail_dwell_time = total_detail_dwell_time+%d,total_impression = total_impression+%d,total_impression_all = total_impression_all+%d \
			, last_active_time = '%s' where product_id = '%s' and app_ver = '%s' and language = '%s' and promotion = '%s' and config_id = '%s' and protocol = '%s' and content_type = '%s' and scenario = '%s' and uid = '%s'" % \
			(int(arra[11]),int(arra[12]),long(arra[13]),int(arra[14]),int(arra[15]),arra[1],arra[2],arra[3],arra[4],arra[5],arra[6],arra[7],arra[8],arra[9],arra[10])
			cursor.execute(update_sql)
    		
    	db.commit()
    except:
	db.rollback()
	traceback.print_exc()
    cursor.close()
    db.close()
    endtime = datetime.datetime.now()
    print (endtime - starttime).seconds
