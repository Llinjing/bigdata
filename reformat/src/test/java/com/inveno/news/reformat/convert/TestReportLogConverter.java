package com.inveno.news.reformat.convert;

import java.util.List;
import java.util.Map;

public class TestReportLogConverter {
	
	public static void main(String [] args) {
		ReportLogConverter converter = new ReportLogConverter();
		Map<String, List<String>> ret = null;
		String str = null;
		
		System.out.println("4 ##############################");
		str = "report&10.10.20.122&2017-03-22 10:38:42|&noticias&h5_unknown&01011610270310334801000174219602&&h5_unknown&&h5_unknown&e647e2e5c1b308b8b813fedb08e1848f&1490150322&h5_unknown&6404125503&2&&&&android&other&&h5_unknown&&Spanish&&&&&h5_unknown&&%5B%7B%22event%5Fid%22%3A%222%22%2C%22content%5Fid%22%3A%221043421373%22%2C%22scenario%22%3A%220x070fff%22%2C%22cpack%22%3A%22h5%5Funknown%22%2C%22event%5Ftime%22%3A%221490150323%22%2C%22server%5Ftime%22%3A%221490150317%22%7D%5D";
		ret = converter.convert(str);
		if (ret != null) {
			for (String key : ret.keySet()) {
				System.out.println(key);
				for (String s : ret.get(key)) {
					System.out.println(s);
				}
			}
		}
		
		/**
		System.out.println("1 ##############################");
		str = "report&10.10.20.121&2017-03-06 00:05:33|&noticiasboomchile&h5_unknown&01011611100611255401000046776009&&h5_unknown&&h5_unknown&feb24c5a603989f5ba6166ed3db0bbe1&1488729626575&h5_unknown&.179371094&0&&&&h5_unknown&h5_unknown&&h5_unknown&&Spanish&&&&&h5_unknown&&%5B%7B%22event%5Fid%22%3A%227%22%2C%22action%5Ftype%22%3A%222%22%2C%22action%5Fname%22%3A%22h5%5Fshare%5Fclick%22%2C%22action%5Fvalue%22%3A%7B%22share%5Fto%22%3A%22h5%5Funknown%22%2C%22content%5Ftype%22%3A%22h5%5Funknown%22%2C%22content%5Fid%22%3A%221042544604%22%2C%22scenario%22%3A%220x010100%22%2C%22plateform%22%3A%22h5%5Funknown%22%2C%22fb%5Fuid%22%3A%22h5%5Funknown%22%2C%22fb%5Fgid%22%3A%22h5%5Funknown%22%7D%2C%22event%5Ftime%22%3A%221488729626%22%7D%5D";
		ret = converter.convert(str);
		if (ret != null) {
			for (String key : ret.keySet()) {
				System.out.println(key);
				for (String s : ret.get(key)) {
					System.out.println(s);
				}
			}
		}
		
		System.out.println("2 ##############################-214 215");
		str = "report&192.168.1.103&2017-03-06 00:00:27|&noticiasboomchile&undefined&01011611100611255401000046776009&&undefined&&undefined&260dc9a053adb17eac5ea8cb0a49be87&1488729627&undefined&9632005908&1&&&&android&other&&undefined&&Spanish&&&&&h5_unknown&&%5B%7B%22event%5Fid%22%3A%227%22%2C%22event%5Ftime%22%3A%221488729627%22%2C%22action%5Fname%22%3A%22h5%5Fpage%5Frequest%5Fcall%22%2C%22action%5Ftype%22%3A%222%22%2C%22action%5Fvalue%22%3A%7B%22share%5Fto%22%3A%22h5%5Funknown%22%2C%22content%5Fid%22%3A%221042544604%22%2C%22content%5Ftype%22%3A%22h5%5Funknown%22%2C%22scenario%22%3A%220x010100%22%2C%22plateform%22%3A%22h5%5Funknown%22%2C%22fb%5Fuid%22%3A%22h5%5Funknown%22%2C%22fb%5Fgid%22%3A%22h5%5Funknown%22%7D%7D%5D";
		ret = converter.convert(str);
		if (ret != null) {
			for (String key : ret.keySet()) {
				System.out.println(key);
				for (String s : ret.get(key)) {
					System.out.println(s);
				}
			}
		}
		
		System.out.println("3 ##############################");
		str = "reportus&192.168.1.103&2017-03-06 00:13:46|&noticiasboom&undefined&01011702161112225301000145020406&&undefined&&undefined&938b201c27f686b081cf931dcfc1ebc2&1488730426&undefined&6569516328&2&&&&android&other&&undefined&&Spanish&&&&&h5_unknown&&%5B%7B%22event%5Fid%22%3A%227%22%2C%22event%5Ftime%22%3A%221488730426%22%2C%22action%5Fname%22%3A%22h5%5Fclick%5Fdownload%5Fapp%22%2C%22action%5Ftype%22%3A%222%22%2C%22action%5Fvalue%22%3A%7B%22share%5Fto%22%3A%22h5%5Funknown%22%2C%22content%5Fid%22%3A%221042544604%22%2C%22content%5Ftype%22%3A%22h5%5Funknown%22%2C%22scenario%22%3A%220x010100%22%2C%22plateform%22%3A%22h5%5Funknown%22%2C%22download%5Ffrom%22%3A%22comment%22%2C%22fb%5Fuid%22%3A%22h5%5Funknown%22%2C%22fb%5Fgid%22%3A%22h5%5Funknown%22%7D%7D%5D";
		ret = converter.convert(str);
		if (ret != null) {
			for (String key : ret.keySet()) {
				System.out.println(key);
				for (String s : ret.get(key)) {
					System.out.println(s);
				}
			}
		}
		
		System.out.println("4 ##############################");
		str = "report&192.168.1.103&2017-03-06 00:13:46|&noticiasboom&undefined&01011702161112225301000145020406&&undefined&&undefined&938b201c27f686b081cf931dcfc1ebc2&1488730426&undefined&6569516328&2&&&&android&other&&undefined&&Spanish&&&&&h5_unknown&&%5b%7b%22action_name%22%3a%22deep_link_deferred_h5%22%2c%22action_value%22%3a%221%22%2c%22event_time%22%3a%221488731585%22%2c%22event_id%22%3a%227%22%2c%22action_type%22%3a%220%22%7d%5d";
		ret = converter.convert(str);
		if (ret != null) {
			for (String key : ret.keySet()) {
				System.out.println(key);
				for (String s : ret.get(key)) {
					System.out.println(s);
				}
			}
		}
		
		System.out.println("5 ##############################");
		str = "report&192.168.1.103&2017-03-06 00:13:46|&noticiasboom&undefined&01011702161112225301000145020406&&undefined&&undefined&938b201c27f686b081cf931dcfc1ebc2&1488730426&undefined&6569516328&2&&&&android&other&&undefined&&Spanish&&&&&h5_unknown&&%5b%7b%22event_id%22%3a%227%22%2c%22event_time%22%3a%221488730426%22%2c%22action_name%22%3a%22deep_link_deferred_h5%22%2c%22action_type%22%3a%222%22%2c%22action_value%22%3a%7b%22share_to%22%3a%22h5_unknown%22%2c%22content_id%22%3a%221042544604%22%2c%22content_type%22%3a%22h5_unknown%22%2c%22scenario%22%3a%220x010100%22%2c%22plateform%22%3a%22h5_unknown%22%2c%22download_from%22%3a%22comment%22%2c%22fb_uid%22%3a%22h5_unknown%22%2c%22fb_gid%22%3a%22h5_unknown%22%7d%7d%5d";
		ret = converter.convert(str);
		if (ret != null) {
			for (String key : ret.keySet()) {
				System.out.println(key);
				for (String s : ret.get(key)) {
					System.out.println(s);
				}
			}
		}
		*/
	}
	
}
