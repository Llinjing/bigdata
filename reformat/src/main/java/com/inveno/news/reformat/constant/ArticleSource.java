package com.inveno.news.reformat.constant;

public class ArticleSource {
	
	public static final ArticleSource UNKNOWN = new ArticleSource("unknown");
	
	private String source = null;
	
	private ArticleSource(String source) {
		this.source = source;
	}
	
	@Override
	public String toString() {
		return source;
	}
	
	@Override
	public boolean equals(Object obj) {
		boolean ret = false;
		
		if (obj instanceof ArticleSource) {
			ret = source.contentEquals(((ArticleSource)obj).source);
		} else if (obj instanceof String) {
			ret = source.contentEquals((String)obj);
		}
		
		return ret;
	}
	
}
