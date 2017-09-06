package com.inveno.news.reformat.constant;

public class LogType {
	
	public static final LogType GATE_AD = new LogType("gate-ad");
	
	public static final LogType MALACCA_AD = new LogType("malacca-ad");
	
	public static final LogType THIRD_MALACCA_AD = new LogType("third-malacca-ad");
	
	public static final LogType HONEYBEE_AD = new LogType("honeybee-ad");
	
	public static final LogType ET_AD = new LogType("et-ad");
	
	private String value = null;
	
	private LogType(String value) {
		this.value = value;
	}
	
	@Override
	public String toString() {
		return value;
	}
	
	@Override
	public boolean equals(Object obj) {
		boolean ret = false;
		
		if (obj instanceof LogType) {
			ret = (value.contentEquals(((LogType)obj).value));
		} else if (obj instanceof String) {
			ret = (value.contentEquals((String)obj));
		}
		
		return ret;
	}

}
