package com.inveno.news.reformat.convert;

import java.util.List;
import java.util.Map;

import com.inveno.news.reformat.convert.UcltmLogConverter;

public class TestUcltmLogConverter {
	
	public static void main(String [] args) {
		UcltmLogConverter converter = new UcltmLogConverter();
		
		System.out.println("0#############");
		String str = "ucltm&192.168.1.73&2016-08-25 23:43:05&meizu1&99000562058585&0&36540914&0&1152&0&0&{\"abtest_ver\":\"108\",\"app\":\"coolpad\",\"cpack\":{\"strategy\":\"8\"},\"duration\":1152,\"id\":\"36540914\",\"idtype\":\"0\",\"stime\":\"1472139785\",\"tm\":1472139786082,\"uid\":\"99000562058585\",\"upack\":{\"ad_configid\":\"160\",\"news_configid\":\"108\"}}";
		Map<String, List<String>> ret = converter.convert(str);
		if (ret != null) {
			for (String key : ret.keySet()) {
				System.out.println(key + " : " + ret.get(key));
			}
		}
		
		System.out.println("1#############");
		str = "ucltm&192.168.1.109&2017-01-14 16:16:01&coolpad&99000559220633&0&61924063&0&26441&0&0&{\"abtest_ver\":\"302\",\"app\":\"coolpad\",\"cpack\":{\"source\":\"风继续吹\",\"strategy\":\"8\"},\"duration\":26441,\"id\":\"61924063\",\"idtype\":\"0\",\"stime\":\"1484381761\",\"tm\":1484381762695,\"uid\":\"99000559220633\",\"upack\":{\"ad_configid\":\"100001\",\"biz_configid\":\"b10\",\"news_configid\":\"302\"}}";
		ret = converter.convert(str);
		if (ret != null) {
			for (String key : ret.keySet()) {
				System.out.println(key + " : " + ret.get(key));
			}
		}
		
		System.out.println("2#############");
		str = "ucltm&192.168.1.109&2017-01-14 16:16:01&coolpad&867365021175020&0&57974919&0&3505&0&0&{\"abtest_ver\":\"319\",\"app\":\"coolpad\",\"cpack\":{\"strategy\":\"15\"},\"duration\":3505,\"id\":\"57974919\",\"idtype\":\"0\",\"stime\":\"1484381761\",\"tm\":1484381761554,\"uid\":\"867365021175020\",\"upack\":{\"ad_configid\":\"100001\",\"biz_configid\":\"b10\",\"news_configid\":\"319\"}}";
		ret = converter.convert(str);
		if (ret != null) {
			for (String key : ret.keySet()) {
				System.out.println(key + " : " + ret.get(key));
			}
		}
		
		System.out.println("3#############");
		str = "ucltm&192.168.1.109&2017-01-14 16:16:01&coolpad&99000561766750&0&62275780&0&25677&0&0&{\"abtest_ver\":\"302\",\"app\":\"coolpad\",\"cpack\":{\"source\":\"萌宠乐事\",\"strategy\":\"8\"},\"duration\":25677,\"id\":\"62275780\",\"idtype\":\"0\",\"stime\":\"1484381762\",\"tm\":1484381763630,\"uid\":\"99000561766750\",\"upack\":{\"ad_configid\":\"100001\",\"biz_configid\":\"b10\",\"news_configid\":\"302\"}}";
		ret = converter.convert(str);
		if (ret != null) {
			for (String key : ret.keySet()) {
				System.out.println(key + " : " + ret.get(key));
			}
		}
	}

}
