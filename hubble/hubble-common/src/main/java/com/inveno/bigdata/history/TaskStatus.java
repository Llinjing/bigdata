package com.inveno.bigdata.history;

public class TaskStatus {
	
	public static final TaskStatus ACCEPTED = new TaskStatus("ACCEPTED");
	
	public static final TaskStatus RUNNING = new TaskStatus("RUNNING");
	
	public static final TaskStatus SUCCEED = new TaskStatus("SUCCEED");
	
	public static final TaskStatus FAILED = new TaskStatus("FAILED");

	private String value = null;
	
	private TaskStatus(String lang) {
		this.value = lang;
	}
	
	@Override
	public String toString() {
		return value;
	}
	
	@Override
	public boolean equals(Object obj) {
		boolean ret = false;
		
		if (obj instanceof TaskStatus) {
			ret = value.contentEquals(((TaskStatus)obj).value);
		} else if (obj instanceof String) {
			ret = value.contentEquals((String)obj);
		}
		
		return ret;
	}
	
}
