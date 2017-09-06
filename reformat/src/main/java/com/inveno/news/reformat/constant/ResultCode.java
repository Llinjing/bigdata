package com.inveno.news.reformat.constant;

import java.util.HashMap;
import java.util.Map;

public class ResultCode {
	
	public static class ResultCodeValue {
	
		public static final ResultCodeValue UNKNOWN = new ResultCodeValue(-1);
		
		public static final ResultCodeValue REQUEST_SUCCESS = new ResultCodeValue(0);
		
		public static final ResultCodeValue REQUEST_FAILURE = new ResultCodeValue(1);
		
		public static final ResultCodeValue ADR_ERROR = new ResultCodeValue(2);
		
		private int value = -1;
		
		private ResultCodeValue(int value) {
			this.value = value;
		}
		
		public int getValue() {
			return value;
		}
		
		@Override
		public String toString() {
			return String.valueOf(value);
		}
		
		@Override
		public boolean equals(Object obj) {
			boolean ret = false;
			
			if (obj instanceof ResultCodeValue) {
				ret = (value == ((ResultCodeValue)obj).value);
			} else if (obj instanceof Integer) {
				ret = (value == (Integer)obj);
			}
			
			return ret;
		}
	}
	
	public static class ResultCodeValueDesc {
		
		public static final ResultCodeValueDesc UNKNOWN = new ResultCodeValueDesc("unknown");
		
		public static final ResultCodeValueDesc REQUEST_SUCCESS = new ResultCodeValueDesc("request_success");
		
		public static final ResultCodeValueDesc REQUEST_FAILURE = new ResultCodeValueDesc("request_failure");
		
		public static final ResultCodeValueDesc ADR_ERROR = new ResultCodeValueDesc("adr_error");
		
		private String value = null;
		
		private ResultCodeValueDesc(String value) {
			this.value = value;
		}
		
		@Override
		public String toString() {
			return value;
		}
	}
	
	public static Map<Integer, String> ResultCode_value_desc = new HashMap<Integer, String>();
	
	static {
		ResultCode_value_desc.put(ResultCodeValue.REQUEST_SUCCESS.getValue(), ResultCodeValueDesc.REQUEST_SUCCESS.toString());
		ResultCode_value_desc.put(ResultCodeValue.REQUEST_FAILURE.getValue(), ResultCodeValueDesc.REQUEST_FAILURE.toString());
		ResultCode_value_desc.put(ResultCodeValue.ADR_ERROR.getValue(), ResultCodeValueDesc.ADR_ERROR.toString());
	}

	public static String getResultCode(String ResultCode_str) {
		int ResultCode = ResultCodeValue.UNKNOWN.getValue();
		try {
			ResultCode = Integer.valueOf(ResultCode_str);
		} catch (Exception e){
		}
		return getResultCode(ResultCode);
	}
	
	public static String getResultCode(int ResultCode) {
		String ret = ResultCode_value_desc.get(ResultCode);
		if (ret == null) {
			ret = ResultCodeValueDesc.UNKNOWN.toString();
		}
		return ret;
	}
	
}
