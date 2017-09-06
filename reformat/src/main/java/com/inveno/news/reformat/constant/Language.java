package com.inveno.news.reformat.constant;

public class Language {
	
	public static final Language UNKNOWN = new Language("unknown");
	
	public static final Language CHINESE = new Language("zh_cn");
	
	public static final Language ENGLISH = new Language("english");
	
	public static final Language HINDI = new Language("hindi");
	
	public static final Language INDONESIAN  = new Language("indonesian");
	
	private String lang = null;
	
	private Language(String lang) {
		this.lang = lang;
	}
	
	@Override
	public String toString() {
		return lang;
	}
	
	@Override
	public boolean equals(Object obj) {
		boolean ret = false;
		
		if (obj instanceof Language) {
			ret = lang.contentEquals(((Language)obj).lang);
		} else if (obj instanceof String) {
			ret = lang.contentEquals((String)obj);
		}
		
		return ret;
	}
	
}
