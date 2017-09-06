package com.inveno.news.reformat.constant;

public class Protocol {
	
	public static final Protocol HTTP = new Protocol("http");
	
	public static final Protocol HTTPS = new Protocol("https");
	
	public static final String HTTPS_TAG = "_s";
	
	private String protocol = null;
	
	private Protocol(String protocol) {
		this.protocol = protocol;
	}
	
	@Override
	public String toString() {
		return protocol;
	}
	
	@Override
	public boolean equals(Object obj) {
		boolean ret = false;
		
		if (obj instanceof ProductID) {
			ret = (protocol.contentEquals(((Protocol)obj).protocol));
		} else if (obj instanceof String) {
			ret = (protocol.contentEquals((String)obj));
		}
		
		return ret;
	}
	
	public static String getProtocol(String tag) {
		String ret = Protocol.HTTP.toString();
		if (tag != null && tag.endsWith(HTTPS_TAG)) {
			ret = Protocol.HTTPS.toString();
		}
		return ret;
	}

}
