package com.inveno.bigdata.common;

import java.util.Calendar;

public class DateTimeUtil {
	
	public static Long getBeginTime() {
		Calendar beginTime = Calendar.getInstance();
		int day = beginTime.get(Calendar.DATE);
		beginTime.set(Calendar.DATE, day-1);
		beginTime.set(Calendar.HOUR_OF_DAY, 0);  
		beginTime.set(Calendar.MINUTE, 0);  
		beginTime.set(Calendar.SECOND, 0);  
		beginTime.set(Calendar.MILLISECOND, 0);  
        return beginTime.getTime().getTime()/1000;  
	}
	
	public static Long getEndTime() {
		Calendar endTime = Calendar.getInstance();
		endTime.set(Calendar.HOUR_OF_DAY, 0);  
		endTime.set(Calendar.MINUTE, 0);  
		endTime.set(Calendar.SECOND, 0);  
		endTime.set(Calendar.MILLISECOND, 0);  
        return endTime.getTime().getTime()/1000;
	}

}
