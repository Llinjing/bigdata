package com.inveno.news.reformat.convert;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessageValidator {
	
	private static final Logger logger = LoggerFactory.getLogger(MessageValidator.class);
	
	public static final String APP_REGEX = "^[0-9a-zA-Z-_+:]+$";
	
	public static final String UID_REGEX = "^[0-9a-zA-Z-_*#,+:]+$";
	
	public static final String CONTENT_ID_REGEX = "^[0-9]+$";
	
	public static boolean validate(String uid, String app, String msg) {
		boolean ret = false;
		
		if (!app.matches(APP_REGEX)) {
			logger.warn("app is invalid : {}", msg);
			return ret;
		}
		if (!uid.matches(UID_REGEX)) {
			logger.warn("uid is invalid : {}", msg);
			return ret;
		}
		
		return true;
	}
	
	public static boolean validate(String uid, String app, String content_id, String msg) {
		boolean ret = false;
		
		if (!app.matches(APP_REGEX)) {
			logger.warn("app is invalid : {}", msg);
			return ret;
		}
		if (!uid.matches(UID_REGEX)) {
			logger.warn("uid is invalid : {}", msg);
			return ret;
		}
		
		return true;
	}

}
