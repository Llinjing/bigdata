package com.inveno.news.reformat.convert;

import java.util.List;
import java.util.Map;

public class TestMalaccaAdRequestConverter {
	
	public static void main(String [] args) {
		MalaccaAdRequestConverter converter = new MalaccaAdRequestConverter();
		
		System.out.println("#############");
		//String str = "malacca_ad_req&192.168.1.241&2016-11-23 17:45:32|1|BBFD7B2F2E33F58A0CFCD040778977B9|yunos_ad|37|yunos_ad|16081602|Doov L5P_M|44984d00153022597cdd9b25a254cf74|119.181.239.138|164|4|312|228|0|1154753354885021696|1|1|";
		String str = "malacca_ad_req&192.168.1.243&2017-03-31 18:15:48|1|290D912993732630835B942672EE0BC5|piemediah5|36|ali|1.0||abcaaf1e6b743c133333910b5d299caa|183.240.38.198|158|1|1080|224|0|1153063443097403392|3|1|";
		Map<String, List<String>> ret = converter.convert(str);
		if (ret != null) {
			for (String key : ret.keySet()) {
				System.out.println(key);
				System.out.println(ret.get(key));
			}
		}
	}

}
