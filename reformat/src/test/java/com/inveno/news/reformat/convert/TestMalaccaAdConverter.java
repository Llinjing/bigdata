package com.inveno.news.reformat.convert;

import java.util.List;
import java.util.Map;

public class TestMalaccaAdConverter {
	
	public static void main(String [] args) {
		MalaccaAdConverter converter = new MalaccaAdConverter();
		String str = null;
		Map<String, List<String>> ret = null;
		
		System.out.println("1###############################-impression");
		str = "malacca_ad_report&192.168.1.243&1|1.0|1|null|1474847999837|1154751095696195413|37|yunos_ad|16050503|163|1|0|140.205.144.149|140.205.144.205|E081B2D1F84DD82AF018286888C1E918|Doov L5P_M||0|0|1474848000001|12|123";
		ret = converter.convert(str);
		if (ret != null) {
			for (String key : ret.keySet()) {
				System.out.println(key);
				for (String str1 : ret.get(key)) {
					System.out.println(str1);
				}
			}
		}
		
		System.out.println("2###############################-click");
		str = "malacca_ad_report&192.168.1.243&1|1.0|2|null|1474847992268|1154751095696195413|37|yunos_ad|16081602|163|1|0|117.136.2.130|140.205.39.73|954AA816BE5836C25DACE0EB03933D46|Doov L5P_M||0|0|1474848000288|coolpad|99999|2016-12-12 13:48:58|8|detail_page||||";
		ret = converter.convert(str);
		if (ret != null) {
			for (String key : ret.keySet()) {
				System.out.println(key);
				for (String str1 : ret.get(key)) {
					System.out.println(str1);
				}
			}
		}
		
		System.out.println("3###############################");
		str = "malacca_ad_report&192.168.1.246&1|1.0|1|1481521738435|1481521737592|1153765933488997278|3|piemediah5|1.0|291|4|0|116.5.87.245|116.5.87.245|867736020089083|||0|0|1481521738362|coolpad|99999|2016-12-12 13:48:58|8|detail_page||||";
		ret = converter.convert(str);
		if (ret != null) {
			for (String key : ret.keySet()) {
				System.out.println(key);
				for (String str1 : ret.get(key)) {
					System.out.println(str1);
				}
			}
		}		
		
		converter.uninit();
	}

}
