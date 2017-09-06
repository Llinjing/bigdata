package com.inveno.news.reformat.constant;

public class KafkaTopic {
	
	// old sdk
	public static final KafkaTopic REQUEST = new KafkaTopic("request");
	
	public static final KafkaTopic REQUEST_NEW_US_EAST = new KafkaTopic("request-new-us-east");
	
	public static final KafkaTopic CLICK = new KafkaTopic("click");
	
	public static final KafkaTopic UCLTM_TOPIC = new KafkaTopic("ucltm_topic");
	
	public static final KafkaTopic UAD = new KafkaTopic("uad");
	
	public static final KafkaTopic PROFILE = new KafkaTopic("profile");
	
	public static final KafkaTopic PROFILE_US_EAST = new KafkaTopic("profile-us-east");
	
	// new sdk
	public static final KafkaTopic REQUEST_NEW = new KafkaTopic("request-new");
	
	public static final KafkaTopic REQUEST_ZHIZI_V2 = new KafkaTopic("request-zhizi-v2");
	
	public static final KafkaTopic REPORT = new KafkaTopic("report-zhizi");
	
	public static final KafkaTopic REPORT_ZHIZI_V2 = new KafkaTopic("report-zhizi-v2");
	
	public static final KafkaTopic REPORT_US_EAST = new KafkaTopic("report-zhizi-us-east");
	
	public static final KafkaTopic REPORT_AD = new KafkaTopic("report-ad");	// oversea
	
	public static final KafkaTopic MALACCA_AD = new KafkaTopic("malacca-ad");	// malacca
	
	public static final KafkaTopic AD_ET_CLICK = new KafkaTopic("ad-et-click");	// advertisement et click
	
	public static final KafkaTopic MALACCA_AD_REQUEST = new KafkaTopic("malacca-ad-request");	// advertisement malacca-ad-request
	
	// reformat topics
	public static final KafkaTopic REQUEST_REFORMAT = new KafkaTopic("request-reformat");
	
	public static final KafkaTopic IMPRESSION_REFORMAT = new KafkaTopic("impression-reformat");
	
	public static final KafkaTopic CLICK_REFORMAT = new KafkaTopic("click-reformat");
	
	public static final KafkaTopic DWELLTIME_REFORMAT = new KafkaTopic("dwelltime-reformat");
	
	public static final KafkaTopic COMPLETENESS_REFORMAT = new KafkaTopic("completeness-reformat");
	
	public static final KafkaTopic PROFILE_REFORMAT = new KafkaTopic("profile-reformat");
	
	public static final KafkaTopic LISTPAGE_DWELLTIME_REFORMAT = new KafkaTopic("listpage-dwelltime-reformat");
	
	public static final KafkaTopic ACTIVITY_DWELLTIME_REFORMAT = new KafkaTopic("activity-dwelltime-reformat");
	
	public static final KafkaTopic BACKEND_SERVICE_REFORMAT = new KafkaTopic("backend-service-reformat");
	
	public static final KafkaTopic EXTEND_EVENT_REFORMAT = new KafkaTopic("extend-event-reformat");
	
	public static final KafkaTopic REPORT_AD_REFORMAT = new KafkaTopic("report-ad-reformat");
	
	// reformat test topics
	public static final KafkaTopic REQUEST_REFORMAT_TEST = new KafkaTopic("request-reformat-test");
	
	public static final KafkaTopic IMPRESSION_REFORMAT_TEST = new KafkaTopic("impression-reformat-test");
	
	public static final KafkaTopic CLICK_REFORMAT_TEST = new KafkaTopic("click-reformat-test");
	
	public static final KafkaTopic DWELLTIME_REFORMAT_TEST = new KafkaTopic("dwelltime-reformat-test");
	
	public static final KafkaTopic COMPLETENESS_REFORMAT_TEST = new KafkaTopic("completeness-reformat-test");
	
	public static final KafkaTopic LISTPAGE_DWELLTIME_REFORMAT_TEST = new KafkaTopic("listpage-dwelltime-reformat-test");
	
	public static final KafkaTopic ACTIVITY_DWELLTIME_REFORMAT_TEST = new KafkaTopic("activity-dwelltime-reformat-test");
	
	public static final KafkaTopic BACKEND_SERVICE_REFORMAT_TEST = new KafkaTopic("backend-service-reformat-test");
	
	public static final KafkaTopic EXTEND_EVENT_REFORMAT_TEST = new KafkaTopic("extend-event-reformat-test");

	//////// advertisement
	// ad from gate-click-reformat/gate-impression-reformat
	public static final KafkaTopic GATE_AD = new KafkaTopic("gate-ad");
	
	// ad from honeybee-click-reformat/honeybee-impression-reformat
	public static final KafkaTopic HONEYBEE_AD = new KafkaTopic("honeybee-ad");
	
	public static final KafkaTopic AD_REQUEST_REFORMAT = new KafkaTopic("ad-request-reformat");
	
	public static final KafkaTopic AD_IMPRESSION_REFORMAT = new KafkaTopic("ad-impression-reformat");
	
	public static final KafkaTopic AD_CLICK_REFORMAT = new KafkaTopic("ad-click-reformat");
	
	public static final KafkaTopic AD_ET_CLICK_REFORMAT = new KafkaTopic("ad-et-click-reformat");// advertisement et click reformat
	
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
