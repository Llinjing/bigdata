package com.inveno.news.reformat.convert;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TestUadLogConverter {
	
	public static void main(String [] args) {
		UadLogConverter converter = new UadLogConverter();
		
		System.out.println("1#############");
		String str = "uad&192.168.1.27&2016-08-25 23:45:18&866288023480456&0&36418873&1&5&1472139918895&&0&0&meizu&0&0&{\"abtest_ver\":\"116\",\"adid\":null,\"adtype\":null,\"app\":\"coolpad\",\"cpack\":{\"strategy\":\"8\"},\"id\":\"36418873\",\"stime\":\"1472139919\",\"uid\":\"866288023480456\",\"upack\":{\"ad_configid\":\"160\",\"news_configid\":\"116\"}}";
		Map<String, List<String>> ret = converter.convert(str);
		if (ret != null) {
			for (String key : ret.keySet()) {
				System.out.println(key);
				for (String s : ret.get(key)) {
					System.out.println(s);
				}
			}
		}

		System.out.println("2#############");
		String str1 = "uad&192.168.1.10&2017-01-14 16:09:23&99000555072598&0&62296808&1&5&1484381364344&&0&0&coolpad&0&0&{\"abtest_ver\":\"302\",\"adid\":null,\"adtype\":null,\"app\":\"coolpad\",\"app_ver\":\"9.03.008\",\"cpack\":{\"strategy\":\"8\"},\"id\":\"62296808\",\"stime\":\"1484381363\",\"uid\":\"99000555072598\",\"upack\":{\"ad_configid\":\"100001\",\"biz_configid\":\"b9\",\"news_configid\":\"302\"}}";
		String str2 = "uad&192.168.1.10&2017-01-14 16:09:23&99000558782519&0&61941823&1&5&1484381366925&61941823&100&0&coolpad&0&0&{\"abtest_ver\":\"302\",\"adid\":\"61941823\",\"adtype\":null,\"app\":\"coolpad\",\"app_ver\":\"9.03.012\",\"cpack\":{\"source\":\"一生相阅\",\"strategy\":\"8\"},\"id\":\"61941823\",\"stime\":\"1484381363\",\"uid\":\"99000558782519\",\"upack\":{\"ad_configid\":\"100001\",\"biz_configid\":\"b9\",\"news_configid\":\"302\"}}";
		String str3 = "uad&192.168.1.10&2017-01-14 16:09:23&99000558906386&0&62152791&1&5&1484381363396&62152791&100&0&coolpad&0&0&{\"abtest_ver\":\"343\",\"adid\":\"62152791\",\"adtype\":null,\"app\":\"coolpad\",\"app_ver\":\"9.03.012\",\"cpack\":{\"source\":\"趣头条\",\"strategy\":\"8\"},\"id\":\"62152791\",\"stime\":\"1484381363\",\"uid\":\"99000558906386\",\"upack\":{\"ad_configid\":\"100001\",\"biz_configid\":\"b9\",\"news_configid\":\"343\"}}";
		String str4 = "uad&192.168.1.10&2017-01-14 16:09:23&867112028625189&0&61976406&1&5&1484381362742&&0&0&coolpad&0&0&{\"abtest_ver\":\"302\",\"adid\":null,\"adtype\":null,\"app\":\"coolpad\",\"app_ver\":\"9.03.010\",\"cpack\":{\"source\":\"乐唯\",\"strategy\":\"8\"},\"id\":\"61976406\",\"stime\":\"1484381363\",\"uid\":\"867112028625189\",\"upack\":{\"ad_configid\":\"100001\",\"biz_configid\":\"b9\",\"news_configid\":\"302\"}}";
		String str5 = "uad&192.168.1.10&2017-01-14 16:09:23&99000557558833&0&62287328&1&5&1484381364517&&0&0&coolpad&0&0&{\"abtest_ver\":\"302\",\"adid\":null,\"adtype\":null,\"app\":\"coolpad\",\"app_ver\":\"9.03.010\",\"cpack\":{\"source\":\"男人窝\",\"strategy\":\"8\"},\"id\":\"62287328\",\"stime\":\"1484381363\",\"uid\":\"99000557558833\",\"upack\":{\"ad_configid\":\"100001\",\"biz_configid\":\"b10\",\"news_configid\":\"302\"}}";
		String str6 = "uad&192.168.1.10&2017-01-14 16:09:23&866288028639346&0&61969056&1&5&1484381364478&&0&0&coolpad&0&0&{\"abtest_ver\":\"302\",\"adid\":null,\"adtype\":null,\"app\":\"coolpad\",\"app_ver\":\"9.03.008\",\"cpack\":{\"source\":\"星闻扒客\",\"strategy\":\"8\"},\"id\":\"61969056\",\"stime\":\"1484381364\",\"uid\":\"866288028639346\",\"upack\":{\"ad_configid\":\"100001\",\"biz_configid\":\"b9\",\"news_configid\":\"302\"}}";
		String str7 = "uad&192.168.1.10&2017-01-14 16:09:23&99000560685949&0&62146552&1&5&1484381365141&62146552&100&0&coolpad&0&0&{\"abtest_ver\":\"302\",\"adid\":\"62146552\",\"adtype\":null,\"app\":\"coolpad\",\"app_ver\":\"9.03.012\",\"cpack\":{\"source\":\"扒皮娱\",\"strategy\":\"113\"},\"id\":\"62146552\",\"stime\":\"1484381364\",\"uid\":\"99000560685949\",\"upack\":{\"ad_configid\":\"100001\",\"biz_configid\":\"b10\",\"news_configid\":\"302\"}}";
		List<String> list = new ArrayList<String>();
		list.add(str1);
		list.add(str2);
		list.add(str3);
		list.add(str4);
		list.add(str5);
		list.add(str6);
		list.add(str7);
		for (String tmp : list) {
			ret = converter.convert(tmp);
			if (ret != null) {
				for (String key : ret.keySet()) {
					System.out.println(key);
					for (String s : ret.get(key)) {
						System.out.println(s);
					}
				}
			}
		}
	}

}
