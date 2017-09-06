package com.inveno.news.reformat.convert;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.inveno.news.reformat.convert.ClickLogConverter;

public class TestClickLogConverter {
	
	public static void main(String [] args) {
		ClickLogConverter converter = new ClickLogConverter();
		
		String str1 = "click&192.168.1.10&2017-01-14 15:59:59&862675028880413&coolpad&Coolpad 7295C&&62139532&0&&0&62102265&&&Coolpad 7295C&&&0&{\"abtest_ver\":\"302\",\"app\":\"coolpad\",\"cpack\":{\"strategy\":\"119\"},\"evnid\":\"13\",\"id\":\"62102265\",\"stime\":\"1484380800\",\"uid\":\"862675028880413\",\"upack\":{\"ad_configid\":\"100001\",\"biz_configid\":\"b1\",\"news_configid\":\"302\"}}";
		String str2 = "clickt&192.168.1.10&2017-01-14 15:59:59&862675028880413&coolpad&Coolpad 7295C&&62112478&0&&0&61956364&&&Coolpad 7295C&&&0&{\"abtest_ver\":\"302\",\"app\":\"coolpad\",\"cpack\":{\"source\":\"萌逗波\",\"strategy\":\"8\"},\"evnid\":\"13\",\"id\":\"61956364\",\"stime\":\"1484380800\",\"uid\":\"862675028880413\",\"upack\":{\"ad_configid\":\"100001\",\"biz_configid\":\"b1\",\"news_configid\":\"302\"}}";
		String str3 = "click&192.168.1.73&2017-01-14 15:59:59&99000558550548&coolpad&Coolpad 5892&&62178165&0&&0&61958936&&&Coolpad 5892&&&0&{\"abtest_ver\":\"259\",\"app\":\"coolpad\",\"cpack\":{\"source\":\"娱乐指南\",\"strategy\":\"8\"},\"evnid\":\"13\",\"id\":\"61958936\",\"stime\":\"1484380800\",\"uid\":\"99000558550548\",\"upack\":{\"ad_configid\":\"100001\",\"biz_configid\":\"b1\",\"news_configid\":\"259\"}}";
		String str4 = "click&192.168.1.73&2017-01-14 15:59:59&99000558550548&coolpad&Coolpad 5892&&62267647&0&&0&62301024&&&Coolpad 5892&&&0&{\"abtest_ver\":\"259\",\"app\":\"coolpad\",\"cpack\":{\"source\":\"三只小猪哥哥\",\"strategy\":\"5\"},\"evnid\":\"13\",\"id\":\"62301024\",\"stime\":\"1484380800\",\"uid\":\"99000558550548\",\"upack\":{\"ad_configid\":\"100001\",\"biz_configid\":\"b1\",\"news_configid\":\"259\"}}";
		String str5 = "click&192.168.1.73&2017-01-14 15:59:59&99000558550548&coolpad&Coolpad 5892&&62267647&0&&0&61925823&&&Coolpad 5892&&&0&{\"abtest_ver\":\"259\",\"app\":\"coolpad\",\"cpack\":{\"source\":\"新浪新闻\",\"strategy\":\"8\"},\"evnid\":\"13\",\"id\":\"61925823\",\"stime\":\"1484380800\",\"uid\":\"99000558550548\",\"upack\":{\"ad_configid\":\"100001\",\"biz_configid\":\"b1\",\"news_configid\":\"259\"}}";
		String str_click_v2 = "click&192.168.1.73&2017-03-07 11:11:34&f6:d0:10:a2:a8:63&coolpad&Coolpad 5200S&&71485413&0&&0&71678865&&9.02.008_VER_2014.07.16_15:58:36&Coolpad 5200S&&&0&{\"abtest_ver\":\"302\",\"app\":\"coolpad\",\"evnid\":\"13\",\"id\":\"71678865\",\"stime\":\"1488856294\",\"uid\":\"f6:d0:10:a2:a8:63\",\"upack\":{\"ad_configid\":\"100001\",\"biz_configid\":\"b9\",\"news_configid\":\"302\"}}";
		String str_click_v2_1 = "click&192.168.1.28&2017-03-07 11:11:31&99000458275467&coolpad&Coolpad 5891Q&&71913120&0&&0&71913120&&&Coolpad 5891Q&&&0&{\"abtest_ver\":\"302\",\"app\":\"coolpad\",\"cpack\":{\"source\":\"每天汽车健康书\",\"strategy\":\"8\"},\"evnid\":\"13\",\"id\":\"71913120\",\"stime\":\"1488856291\",\"uid\":\"99000458275467\",\"upack\":{\"ad_configid\":\"100001\",\"biz_configid\":\"b1\",\"news_configid\":\"302\"}}";
		List<String> list = new ArrayList<String>();
		list.add(str1);
		list.add(str2);
		list.add(str3);
		list.add(str4);
		list.add(str5);
		list.add(str_click_v2);
		list.add(str_click_v2_1);
		for (String str : list) {
			Map<String, List<String>> ret = converter.convert(str);
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
