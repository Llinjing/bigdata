package com.inveno.news.reformat.convert;

import java.util.List;
import java.util.Map;

public class TestOverseaAnonymousAdvertisementConverter {

	public static void main(String [] args) {
		OverseaAnonymousAdConverter converter = new OverseaAnonymousAdConverter();
		String str = "overseas_ad_report&172.31.8.20&2016-09-02 00:00:01|1|null|172.31.26.24|673ba045a6d7af4be8574ecd6fd2d32e|Hotoday|2.1.7.0.0.3|com.inveno.hotoday|0|US|en|1|gp|M5_lite|1.2|batmobi|null|null|null|10806_87077|0|100_3|";
		Map<String, List<String>> ret = converter.convert(str);
		if (ret != null) {
			for (String key : ret.keySet()) {
				System.out.println(key);
				for (String str1 : ret.get(key)) {
					System.out.println(str1);
				}
			}
		}
	}
	
}
