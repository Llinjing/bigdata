package com.inveno.news.reformat.constant;

import java.util.HashMap;
import java.util.Map;

public class Network {
	
	public static class NetworkValue {
	
		public static final NetworkValue UNKNOWN = new NetworkValue(-1);
		
		public static final NetworkValue WIFI = new NetworkValue(1);
		
		public static final NetworkValue NETWORK_2G = new NetworkValue(2);
		
		public static final NetworkValue NETWORK_3G = new NetworkValue(3);
		
		public static final NetworkValue NETWORK_4G = new NetworkValue(4);
		
		public static final NetworkValue NETWORK_5G = new NetworkValue(5);
		
		public static final NetworkValue OTHER_MOBILE_NETWORK = new NetworkValue(6);
		
		private int value = -1;
		
		private NetworkValue(int value) {
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
			
			if (obj instanceof NetworkValue) {
				ret = (value == ((NetworkValue)obj).value);
			} else if (obj instanceof Integer) {
				ret = (value == (Integer)obj);
			}
			
			return ret;
		}
	}
	
	public static class NetworkValueDesc {
		
		public static final NetworkValueDesc UNKNOWN = new NetworkValueDesc("unknown");
		
		public static final NetworkValueDesc WIFI = new NetworkValueDesc("wifi");
		
		public static final NetworkValueDesc NETWORK_2G = new NetworkValueDesc("2g");
		
		public static final NetworkValueDesc NETWORK_3G = new NetworkValueDesc("3g");
		
		public static final NetworkValueDesc NETWORK_4G = new NetworkValueDesc("4g");
		
		public static final NetworkValueDesc NETWORK_5G = new NetworkValueDesc("5g");
		
		public static final NetworkValueDesc OTHER_MOBILE_NETWORK = new NetworkValueDesc("other");
		
		private String value = null;
		
		private NetworkValueDesc(String value) {
			this.value = value;
		}
		
		@Override
		public String toString() {
			return value;
		}
	}
	
	public static Map<Integer, String> network_value_desc = new HashMap<Integer, String>();
	
	static {
		network_value_desc.put(NetworkValue.UNKNOWN.getValue(), NetworkValueDesc.UNKNOWN.toString());
		network_value_desc.put(NetworkValue.WIFI.getValue(), NetworkValueDesc.WIFI.toString());
		network_value_desc.put(NetworkValue.NETWORK_2G.getValue(), NetworkValueDesc.NETWORK_2G.toString());
		network_value_desc.put(NetworkValue.NETWORK_3G.getValue(), NetworkValueDesc.NETWORK_3G.toString());
		network_value_desc.put(NetworkValue.NETWORK_4G.getValue(), NetworkValueDesc.NETWORK_4G.toString());
		network_value_desc.put(NetworkValue.NETWORK_5G.getValue(), NetworkValueDesc.NETWORK_5G.toString());
		network_value_desc.put(NetworkValue.OTHER_MOBILE_NETWORK.getValue(), NetworkValueDesc.OTHER_MOBILE_NETWORK.toString());
	}

	public static String getNetwork(String network_str) {
		int network = NetworkValue.UNKNOWN.getValue();
		try {
			network = Integer.valueOf(network_str);
		} catch (Exception e){
		}
		return getNetwork(network);
	}
	
	public static String getNetwork(int network) {
		String ret = network_value_desc.get(network);
		if (ret == null) {
			ret = NetworkValueDesc.UNKNOWN.toString();
		}
		return ret;
	}
	
}
