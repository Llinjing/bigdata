package com.inveno.bigdata.history;

public class DataSource {
	
	public static final DataSource DAILY_USER = new DataSource("t_daily_user");
	
	public static final DataSource DAILY_ARTICLE = new DataSource("t_daily_article");
	
	public static final DataSource DAILY_ARTICLE_OPERATE = new DataSource("t_daily_article_operate");
	
	public static final DataSource DAILY_SOURCE = new DataSource("t_daily_source");
	
	public static final DataSource DAILY_USER_RETENTION = new DataSource("t_user_retention");

	private String value = null;
	
	private DataSource(String lang) {
		this.value = lang;
	}
	
	@Override
	public String toString() {
		return value;
	}
	
	@Override
	public boolean equals(Object obj) {
		boolean ret = false;
		
		if (obj instanceof DataSource) {
			ret = value.contentEquals(((DataSource)obj).value);
		} else if (obj instanceof String) {
			ret = value.contentEquals((String)obj);
		}
		
		return ret;
	}
	
	public static boolean isValid(String data_source) {
		return (DataSource.DAILY_USER.equals(data_source) ||
				DataSource.DAILY_ARTICLE.equals(data_source) ||
				DataSource.DAILY_ARTICLE_OPERATE.equals(data_source) ||
				DataSource.DAILY_SOURCE.equals(data_source) ||
				DataSource.DAILY_USER_RETENTION.equals(data_source));
	}
	
}
