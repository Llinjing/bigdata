package com.inveno.news.reformat.convert;

import java.util.List;
import java.util.Map;

public class TestAdEtClickConverter {
	
	public static void main(String [] args) {
		AdEtClickConverter converter = new AdEtClickConverter();
		
		System.out.println("1#############");
		String str = "ad_et_click&192.168.1.241&2016-11-19 12:45:50|1.61.4.160|http://www.inveno.com/|http://e.cn.miaozhen.com/r/k=2029687&p=72cM1&dx=__IPDX__&rt=2&ns=__IP__&ni=__IESID__&v=__LOC__&xa=__ADPLATFORM__&ro=sm&o=http://m.chevrolet.com.cn/act/cavalier/cavalier-lp/?utm_source=Baidu-M&utm_medium=HS-201609243_INT80-MDSP1_1&utm_content=SGMMRK2016000200&utm_campaign=SP-CH1600519_1|1153063354885021696|inveno|866598020016024|Mozilla/5.0 (Linux; Android 4.4.2; zh-cn; Coolpad 8690_T00 Build/KOT49H) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/30.0.0.0 Mobile Safari/537.36|";
		Map<String, List<String>> ret = converter.convert(str);
		if (ret != null) {
			for (String key : ret.keySet()) {
				System.out.println(key);
				System.out.println(ret.get(key));
			}
		}
		
		System.out.println("2#############");
		str = "ad_et_click&192.168.1.241&2016-11-19 12:45:50|1.61.4.160|http://www.inveno.com/|http://e.cn.miaozhen.com/r/k=2029687&p=72cM1&dx=__IPDX__&rt=2&ns=__IP__&ni=__IESID__&v=__LOC__&xa=__ADPLATFORM__&ro=sm&o=http://m.chevrolet.com.cn/act/cavalier/cavalier-lp/?utm_source=Baidu-M&utm_medium=HS-201609243_INT80-MDSP1_1&utm_content=SGMMRK2016000200&utm_campaign=SP-CH1600519_1||inveno|866598020016024|Mozilla/5.0 (Linux; Android 4.4.2; zh-cn; Coolpad 8690_T00 Build/KOT49H) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/30.0.0.0 Mobile Safari/537.36|";
		ret = converter.convert(str);
		if (ret != null) {
			for (String key : ret.keySet()) {
				System.out.println(key);
				System.out.println(ret.get(key));
			}
		}
		
		System.out.println("3#############");
		str = "ad_et_click&192.168.1.241&2016-11-19 12:45:50|1.61.4.160|http://www.inveno.com/|http://e.cn.miaozhen.com/r/k=2029687&p=72cM1&dx=__IPDX__&rt=2&ns=__IP__&ni=__IESID__&v=__LOC__&xa=__ADPLATFORM__&ro=sm&o=http://m.chevrolet.com.cn/act/cavalier/cavalier-lp/?utm_source=Baidu-M&utm_medium=HS-201609243_INT80-MDSP1_1&utm_content=SGMMRK2016000200&utm_campaign=SP-CH1600519_1|11541881451714701110000|inveno|866598020016024|Mozilla/5.0 (Linux; Android 4.4.2; zh-cn; Coolpad 8690_T00 Build/KOT49H) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/30.0.0.0 Mobile Safari/537.36|";
		ret = converter.convert(str);
		if (ret != null) {
			for (String key : ret.keySet()) {
				System.out.println(key);
				System.out.println(ret.get(key));
			}
		}
	}

}
