package com.inveno.news.reformat.constant;

import java.util.HashMap;
import java.util.Map;

public class AdSource {
	
	public static class AdSourceValue {
	
		public static final AdSourceValue UNKNOWN = new AdSourceValue(-1);
		
		public static final AdSourceValue SELF_AD = new AdSourceValue(1);
		
		public static final AdSourceValue TAOBAO_AD = new AdSourceValue(2);
		
		public static final AdSourceValue BAIDU_AD = new AdSourceValue(3);
		
		public static final AdSourceValue DUJIN_AD = new AdSourceValue(4);
		
		public static final AdSourceValue JX360_AD = new AdSourceValue(5);
		
		public static final AdSourceValue SOUGOU_AD = new AdSourceValue(6);
		
		public static final AdSourceValue YIYUN_AD = new AdSourceValue(7);
		
		public static final AdSourceValue YUNOS_AD = new AdSourceValue(8);
		
		public static final AdSourceValue KDXF_AD = new AdSourceValue(9);
		
		private int value = -1;
		
		private AdSourceValue(int value) {
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
			
			if (obj instanceof AdSourceValue) {
				ret = (value == ((AdSourceValue)obj).value);
			} else if (obj instanceof Integer) {
				ret = (value == (Integer)obj);
			}
			
			return ret;
		}
	}
	
	public static class AdSourceValueDesc {
		
		public static final AdSourceValueDesc UNKNOWN = new AdSourceValueDesc("unknown");
		
		public static final AdSourceValueDesc SELF_AD = new AdSourceValueDesc("self_ad");
		
		public static final AdSourceValueDesc TAOBAO_AD = new AdSourceValueDesc("taobao_ad");
		
		public static final AdSourceValueDesc BAIDU_AD = new AdSourceValueDesc("baidu_ad");
		
		public static final AdSourceValueDesc DUJIN_AD = new AdSourceValueDesc("dujin_ad");
		
		public static final AdSourceValueDesc JX360_AD = new AdSourceValueDesc("jx360_ad");
		
		public static final AdSourceValueDesc SOUGOU_AD = new AdSourceValueDesc("sougou_ad");
		
		public static final AdSourceValueDesc YIYUN_AD = new AdSourceValueDesc("yiyun_ad");
		
		public static final AdSourceValueDesc YUNOS_AD = new AdSourceValueDesc("yunos_ad");
		
		public static final AdSourceValueDesc KDXF_AD = new AdSourceValueDesc("kdxf_ad");
		
		private String value = null;
		
		private AdSourceValueDesc(String value) {
			this.value = value;
		}
		
		@Override
		public String toString() {
			return value;
		}
		
		
		@Override
		public boolean equals(Object obj) {
			boolean ret = false;
			
			if (obj instanceof AdSourceValueDesc) {
				ret = value.contentEquals(((AdSourceValueDesc)obj).value);
			} else if (obj instanceof String) {
				ret = value.contentEquals((String)obj);
			}
			
			return ret;
		}
	}
	
	public static Map<Integer, String> ad_source_value_desc = new HashMap<Integer, String>();
	
	static {
		ad_source_value_desc.put(AdSourceValue.UNKNOWN.getValue(), AdSourceValueDesc.UNKNOWN.toString());
		ad_source_value_desc.put(AdSourceValue.SELF_AD.getValue(), AdSourceValueDesc.SELF_AD.toString());
		ad_source_value_desc.put(AdSourceValue.BAIDU_AD.getValue(), AdSourceValueDesc.BAIDU_AD.toString());
		ad_source_value_desc.put(AdSourceValue.TAOBAO_AD.getValue(), AdSourceValueDesc.TAOBAO_AD.toString());
		ad_source_value_desc.put(AdSourceValue.DUJIN_AD.getValue(), AdSourceValueDesc.DUJIN_AD.toString());		
		ad_source_value_desc.put(AdSourceValue.JX360_AD.getValue(), AdSourceValueDesc.JX360_AD.toString());
		ad_source_value_desc.put(AdSourceValue.SOUGOU_AD.getValue(), AdSourceValueDesc.SOUGOU_AD.toString());
		ad_source_value_desc.put(AdSourceValue.YIYUN_AD.getValue(), AdSourceValueDesc.YIYUN_AD.toString());
		ad_source_value_desc.put(AdSourceValue.YUNOS_AD.getValue(), AdSourceValueDesc.YUNOS_AD.toString());
		ad_source_value_desc.put(AdSourceValue.KDXF_AD.getValue(), AdSourceValueDesc.KDXF_AD.toString());
	}

	public static String getAdSource(String ad_source) {
		int value = AdSourceValue.UNKNOWN.getValue();
		try {
			value = Integer.valueOf(ad_source);
		} catch (Exception e){
		}
		return getAdSource(value);
	}
	
	public static String getAdSource(int ad_source) {
		String ret = ad_source_value_desc.get(ad_source);
		if (ret == null) {
			ret = AdSourceValueDesc.UNKNOWN.toString();
		}
		return ret;
	}
	
}
