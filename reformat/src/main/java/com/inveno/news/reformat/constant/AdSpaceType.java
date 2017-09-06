package com.inveno.news.reformat.constant;

import java.util.HashMap;
import java.util.Map;

public class AdSpaceType {

	public static class AdSpaceTypeValue {

		public static final AdSpaceTypeValue UNKNOWN = new AdSpaceTypeValue(-1);

		public static final AdSpaceTypeValue BANNER = new AdSpaceTypeValue(1);

		public static final AdSpaceTypeValue OPENNING = new AdSpaceTypeValue(2);

		public static final AdSpaceTypeValue INTERSTITIAL = new AdSpaceTypeValue(3);

		public static final AdSpaceTypeValue NATIVE = new AdSpaceTypeValue(4);

		public static final AdSpaceTypeValue TEXT = new AdSpaceTypeValue(5);

		private int value = -1;

		private AdSpaceTypeValue(int value) {
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

			if (obj instanceof AdSpaceTypeValue) {
				ret = (value == ((AdSpaceTypeValue) obj).value);
			} else if (obj instanceof Integer) {
				ret = (value == (Integer) obj);
			}

			return ret;
		}

	}
	
	
	public static class AdSpaceTypeValueDesc {
		
		public static final AdSpaceTypeValueDesc UNKNOWN = new AdSpaceTypeValueDesc("unknown");
		
		public static final AdSpaceTypeValueDesc BANNER = new AdSpaceTypeValueDesc("banner");

		public static final AdSpaceTypeValueDesc OPENNING = new AdSpaceTypeValueDesc("openning");

		public static final AdSpaceTypeValueDesc INTERSTITIAL = new AdSpaceTypeValueDesc("interstitial");

		public static final AdSpaceTypeValueDesc NATIVE = new AdSpaceTypeValueDesc("native");

		public static final AdSpaceTypeValueDesc TEXT = new AdSpaceTypeValueDesc("text");
		
		private String value = null;
		
		private AdSpaceTypeValueDesc(String value) {
			this.value = value;
		}
		
		@Override
		public String toString() {
			return value;
		}
		
	}
	
	public static Map<Integer, String> ad_space_type_desc = new HashMap<Integer, String>();
	
	static {
		ad_space_type_desc.put(AdSpaceTypeValue.UNKNOWN.getValue(), AdSpaceTypeValueDesc.UNKNOWN.toString());
		ad_space_type_desc.put(AdSpaceTypeValue.BANNER.getValue(), AdSpaceTypeValueDesc.BANNER.toString());
		ad_space_type_desc.put(AdSpaceTypeValue.OPENNING.getValue(), AdSpaceTypeValueDesc.OPENNING.toString());
		ad_space_type_desc.put(AdSpaceTypeValue.INTERSTITIAL.getValue(), AdSpaceTypeValueDesc.INTERSTITIAL.toString());
		ad_space_type_desc.put(AdSpaceTypeValue.NATIVE.getValue(), AdSpaceTypeValueDesc.NATIVE.toString());
		ad_space_type_desc.put(AdSpaceTypeValue.TEXT.getValue(), AdSpaceTypeValueDesc.TEXT.toString());
	}
	
	public static String getAdSpaceType(String ad_space_type) {
		int value = AdSpaceTypeValue.UNKNOWN.getValue();
		try {
			value = Integer.valueOf(ad_space_type).intValue();
		} catch (Exception e) {
		}
		return getAdSpaceType(value);
	}

	public static String getAdSpaceType(int ad_space_type) {
		String ret = ad_space_type_desc.get(ad_space_type);
		if (ret == null) {
			ret = AdSpaceTypeValueDesc.UNKNOWN.toString();
		}
		return ret;
	}
	
}
