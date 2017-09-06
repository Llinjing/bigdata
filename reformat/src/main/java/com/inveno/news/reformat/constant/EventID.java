package com.inveno.news.reformat.constant;

public class EventID {
	
	public static final EventID REQUEST = new EventID(0);
	
	public static final EventID LISTPAGE_DWELLTIME = new EventID(1);
	
	public static final EventID IMPRESSION = new EventID(2);
	
	public static final EventID CLICK = new EventID(3);
	
	public static final EventID DWELLTIME = new EventID(4);
	
	public static final EventID COMPLETENESS = new EventID(5);
	
	public static final EventID ACTIVITY_DWELLTIME = new EventID(6);
	
	public static final EventID EXTEND_EVENT = new EventID(7);
	
	public static final EventID BACKEND_SERVICE = new EventID(8);

	private int event_type = -1;
	
	private EventID(int type) {
		this.event_type = type;
	}
	
	public int getValue() {
		return event_type;
	}
	
	@Override
	public String toString() {
		return String.valueOf(event_type);
	}
	
	@Override
	public boolean equals(Object obj) {
		boolean ret = false;
		
		if (obj instanceof EventID) {
			ret = (event_type == ((EventID)obj).event_type);
		} else if (obj instanceof Integer) {
			ret = (event_type == (Integer)obj);
		}
		
		return ret;
	}
	
}
