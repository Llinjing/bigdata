package com.inveno.bigdata.sync.constant;

public class KafkaTopic {
	
	// old sdk
	public static final KafkaTopic IMPRESSION_STATISTICS = new KafkaTopic("impression-statistics");
	
	private String topic = null;
	
	private KafkaTopic(String topic) {
		this.topic = topic;
	}
	
	@Override
	public String toString() {
		return topic;
	}
	
	@Override
	public boolean equals(Object obj) {
		boolean ret = false;
		
		if (obj instanceof KafkaTopic) {
			ret = topic.contentEquals(((KafkaTopic)obj).topic);
		} else if (obj instanceof String) {
			ret = topic.contentEquals((String)obj);
		}
		
		return ret;
	}
	
}
