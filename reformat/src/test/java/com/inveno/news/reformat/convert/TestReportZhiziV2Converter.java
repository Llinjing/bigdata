package com.inveno.news.reformat.convert;

import java.util.List;
import java.util.Map;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

public class TestReportZhiziV2Converter {
	
	private static final String CLASS_NAME = TestReportZhiziV2Converter.class.getSimpleName();
	
//	private static final Logger logger = LoggerFactory.getLogger(TestReportZhiziV2Converter.class);

	public static void usage() {
		System.out.println(CLASS_NAME + " <conf_file>");
	}
	
	public static void main(String [] args) {
//		if (args.length != 1) {
//			usage();
//			return;
//		}
//		
//		// config
//		String conf_file = args[0];
//		ReformatConfig config = ReformatConfig.getInstance();
//		if (!config.parse(conf_file)) {
//			logger.warn("parse conf file failed : {}", conf_file);
//			return;
//		}
//		
//		logger.info(config.toString());
//		
//		if (!config.isValid()) {
//			logger.warn("config is invalid");
//			return;
//		}
//
//		ReportZhiziV2Converter converter = new ReportZhiziV2Converter();
//		converter.init();
		
		ReportZhiziV2Converter converter = new ReportZhiziV2Converter();
		Map<String, List<String>> ret = null;
		String str = null;

		System.out.println("0 ##############################    ----    hard adver");
		str = "report&10.10.20.122&2017-05-15 00:00:02|&noticias&gp&01011612011442495201000006967805&&2.5.0.0.0.3&&2.0.0&0bf9fa700d8d7ca4305e65d737d20f28&1494777619&3&72671380&108&867409022130038&75d6a95d885ea1f9&&ZTE&Blade V6&5.0.2&android&es_US&Spanish&MX&03&&&eyJhYl92ZXIiOiIxOTMiLCJhZF9jb25maWdpZCI6IjkzIiwiYml6X2NvbmZpZ2lkIjoiNTAiLCJuZXdzX2NvbmZpZ2lkIjoiMTkzIn0=&{}&%5B%7B%22event%5Fid%22%3A%227%22%2C%22event%5Ftime%22%3A%221494777445%22%2C%22action%5Fname%22%3A%22ad%5Frequest%5Fresponse%22%2C%22action%5Fvalue%22%3A%7B%22adspace%5Fid%22%3A%2215%22%2C%22request%5Fcount%22%3A1%2C%22response%5Fcount%22%3A1%2C%22ad%5Fsource%22%3A%22Facebook%22%2C%22t%5Fadspace%5Fid%22%3A%221266715543393510%5F1510747408990321%22%2C%22duration%5Ftime%22%3A18873%7D%2C%22action%5Ftype%22%3A%222%22%7D%2C%7B%22event%5Fid%22%3A%227%22%2C%22event%5Ftime%22%3A%221494777445%22%2C%22action%5Fname%22%3A%22ad%5Frequest%5Ffill%22%2C%22action%5Fvalue%22%3A%7B%22adspace%5Fid%22%3A%2215%22%2C%22duration%5Ftime%22%3A18874%7D%2C%22action%5Ftype%22%3A%222%22%7D%2C%7B%22event%5Fid%22%3A%227%22%2C%22event%5Ftime%22%3A%221494777454%22%2C%22action%5Fname%22%3A%22ad%5Frequest%5Fresponse%22%2C%22action%5Fvalue%22%3A%7B%22adspace%5Fid%22%3A%2213%22%2C%22request%5Fcount%22%3A1%2C%22response%5Fcount%22%3A1%2C%22ad%5Fsource%22%3A%22Google%22%2C%22t%5Fadspace%5Fid%22%3A%22ca%2Dapp%2Dpub%2D7636229885890988%5C%2F2892937754%22%2C%22duration%5Ftime%22%3A15920%7D%2C%22action%5Ftype%22%3A%222%22%7D%2C%7B%22event%5Fid%22%3A%227%22%2C%22event%5Ftime%22%3A%221494777454%22%2C%22action%5Fname%22%3A%22ad%5Frequest%5Ffill%22%2C%22action%5Fvalue%22%3A%7B%22adspace%5Fid%22%3A%2213%22%2C%22duration%5Ftime%22%3A15947%7D%2C%22action%5Ftype%22%3A%222%22%7D%2C%7B%22event%5Fid%22%3A%227%22%2C%22event%5Ftime%22%3A%221494777454%22%2C%22action%5Fname%22%3A%22ad%5Fdemand%5Ffill%5Fsuccess%22%2C%22action%5Fvalue%22%3A%7B%22adspace%5Fid%22%3A%2213%22%2C%22scenario%22%3A%220x0712ff%22%2C%22duration%5Ftime%22%3A15968%2C%22ad%5Fmd5%22%3A%2224d794dfc756320ffadb905d526299bc%22%7D%2C%22action%5Ftype%22%3A%222%22%7D%2C%7B%22event%5Fid%22%3A%227%22%2C%22event%5Ftime%22%3A%221494777454%22%2C%22action%5Fname%22%3A%22adspace%5Fimpression%22%2C%22action%5Fvalue%22%3A%7B%22adspace%5Fid%22%3A%2213%22%2C%22adspace%5Fposition%22%3A%220%22%7D%2C%22action%5Ftype%22%3A%222%22%7D%2C%7B%22event%5Fid%22%3A%222%22%2C%22event%5Ftime%22%3A%221494777454%22%2C%22extra%5Fmsg%22%3A%7B%22ad%5Fmd5%22%3A%2224d794dfc756320ffadb905d526299bc%22%2C%22t%5Fadspace%5Fid%22%3A%22ca%2Dapp%2Dpub%2D7636229885890988%5C%2F2892937754%22%2C%22adspace%5Fid%22%3A%2213%22%7D%2C%22scenario%22%3A%220x0712ff%22%2C%22content%5Fid%22%3A%2201011612011442495201000006967805%5F1494777454072%22%2C%22cpack%22%3A%22eyJzdHJhdGVneSI6IjExIiwiY29udGVudF90eXBlIjoiMHgwMDAwMDgwMCIsImFkX3NvdXJjZSI6Ikdvb2dsZSJ9%22%2C%22server%5Ftime%22%3A%221494777454%22%2C%22refer%22%3A%7B%22content%5Fid%22%3A%221053762559%22%2C%22content%5Ftype%22%3A%220x00000001%22%7D%7D%2C%7B%22event%5Fid%22%3A%227%22%2C%22event%5Ftime%22%3A%221494777543%22%2C%22action%5Fname%22%3A%22original%5Fad%5Fdemand%22%2C%22action%5Fvalue%22%3A%7B%22adspace%5Fid%22%3A%223%22%2C%22scenario%22%3A%220x070dff%22%7D%2C%22action%5Ftype%22%3A%222%22%7D%2C%7B%22event%5Fid%22%3A%227%22%2C%22event%5Ftime%22%3A%221494777543%22%2C%22action%5Fname%22%3A%22ad%5Frequest%22%2C%22action%5Fvalue%22%3A%7B%22adspace%5Fid%22%3A%223%22%7D%2C%22action%5Ftype%22%3A%222%22%7D%2C%7B%22event%5Fid%22%3A%227%22%2C%22event%5Ftime%22%3A%221494777544%22%2C%22action%5Fname%22%3A%22ad%5Frequest%5Fresponse%22%2C%22action%5Fvalue%22%3A%7B%22adspace%5Fid%22%3A%223%22%2C%22request%5Fcount%22%3A1%2C%22response%5Fcount%22%3A1%2C%22ad%5Fsource%22%3A%22Facebook%22%2C%22t%5Fadspace%5Fid%22%3A%221266715543393510%5F1266716493393415%22%2C%22duration%5Ftime%22%3A1244%7D%2C%22action%5Ftype%22%3A%222%22%7D%2C%7B%22event%5Fid%22%3A%227%22%2C%22event%5Ftime%22%3A%221494777544%22%2C%22action%5Fname%22%3A%22ad%5Frequest%5Ffill%22%2C%22action%5Fvalue%22%3A%7B%22adspace%5Fid%22%3A%223%22%2C%22duration%5Ftime%22%3A1269%7D%2C%22action%5Ftype%22%3A%222%22%7D%2C%7B%22event%5Fid%22%3A%227%22%2C%22event%5Ftime%22%3A%221494777544%22%2C%22action%5Fname%22%3A%22ad%5Fdemand%5Ffill%5Fsuccess%22%2C%22action%5Fvalue%22%3A%7B%22adspace%5Fid%22%3A%223%22%2C%22scenario%22%3A%220x070dff%22%2C%22duration%5Ftime%22%3A1274%2C%22ad%5Ftitle%22%3A%22Disfruta%2030%20d%C3%ADas%20gratis%2E%22%2C%22ad%5Fmd5%22%3A%224b5452967b931fe84e95c54f65c2d967%22%7D%2C%22action%5Ftype%22%3A%222%22%7D%2C%7B%22event%5Fid%22%3A%226%22%2C%22event%5Ftime%22%3A%221494777537%22%2C%22page%5Fname%22%3A%22com%2Einveno%2Exiaozhi%2Edetail%2Eui%2ENewsDetailNativeActivity%22%2C%22stay%5Ftime%22%3A%2281%22%7D%5D";
		ret = converter.convert(str);
		if (ret != null) {
			for (String key : ret.keySet()) {
				System.out.println(key);
				for (String s : ret.get(key)) {
					System.out.println(s);
				}
			}
		}
		
		System.out.println("0 ##############################    ----    thrid part adver");
		str = "report&10.10.20.122&2017-04-07 01:01:03|&noticias&gp&01011704061222435201000011883505&&2.4.7.0.0.4&&2.0.0&be665bc37959fddc5347e2fee8b3911c&1491497161&1&91450127&35&358514050448776&23a008a813c3911c&&lge&LG-D680&4.1.2&android&es_MX&Spanish&MX&020&&&eyJhYl92ZXIiOiIxODUiLCJhZF9jb25maWdpZCI6IjQwIiwiYml6X2NvbmZpZ2lkIjoiNTAiLCJuZXdzX2NvbmZpZ2lkIjoiMTg1In0=&{\"utm_source\":\"(not%20set)\",\"utm_medium\":\"(not%20set)\"}&%5b%7b%22action_name%22%3a%22lockscreen_unlock%22%2c%22action_value%22%3a%221%22%2c%22event_time%22%3a%221491497121%22%2c%22event_id%22%3a%227%22%2c%22action_type%22%3a%220%22%7d%2c%7b%22cpack%22%3a%22eyJzdHJhdGVneSI6IjExIiwiY29udGVudF90eXBlIjoiMHgwMDAwMDgwMCIsImFkX3NvdXJjZSI6IlNlbGZTdXBwb3J0In0%3d%22%2c%22event_time%22%3a%221491497156%22%2c%22event_id%22%3a%222%22%2c%22server_time%22%3a%221491452572%22%2c%22content_id%22%3a%221703270027%22%2c%22scenario%22%3a%220x0711ff%22%7d%2c%7b%22page_name%22%3a%22com.inveno.xiaozhi.main.LoadingActivity%22%2c%22event_time%22%3a%221491497154%22%2c%22event_id%22%3a%226%22%2c%22stay_time%22%3a%226%22%7d%5d";
		ret = converter.convert(str);
		if (ret != null) {
			for (String key : ret.keySet()) {
				System.out.println(key);
				for (String s : ret.get(key)) {
					System.out.println(s);
				}
			}
		}
		
		System.out.println("1 ##############################    ----    soft adver");
		str = "report&10.10.20.122&2017-04-07 01:01:03|&noticias&gp&01011704061222435201000011883505&&2.4.7.0.0.4&&2.0.0&be665bc37959fddc5347e2fee8b3911c&1491497161&1&91450127&35&358514050448776&23a008a813c3911c&&lge&LG-D680&4.1.2&android&es_MX&Spanish&MX&020&&&eyJhYl92ZXIiOiIxODUiLCJhZF9jb25maWdpZCI6IjQwIiwiYml6X2NvbmZpZ2lkIjoiNTAiLCJuZXdzX2NvbmZpZ2lkIjoiMTg1In0=&{\"utm_source\":\"(not%20set)\",\"utm_medium\":\"(not%20set)\"}&%5b%7b%22action_name%22%3a%22lockscreen_unlock%22%2c%22action_value%22%3a%221%22%2c%22event_time%22%3a%221491497121%22%2c%22event_id%22%3a%227%22%2c%22action_type%22%3a%220%22%7d%2c%7b%22cpack%22%3a%22eyJzdHJhdGVneSI6IjExIiwiY29udGVudF90eXBlIjoiMHgwMDAwMDQwMCIsImFkX3NvdXJjZSI6IlNlbGZTdXBwb3J0In0%3d%22%2c%22event_time%22%3a%221491497156%22%2c%22event_id%22%3a%222%22%2c%22server_time%22%3a%221491452572%22%2c%22content_id%22%3a%221703270027%22%2c%22scenario%22%3a%220x0711ff%22%7d%2c%7b%22page_name%22%3a%22com.inveno.xiaozhi.main.LoadingActivity%22%2c%22event_time%22%3a%221491497154%22%2c%22event_id%22%3a%226%22%2c%22stay_time%22%3a%226%22%7d%5d";
		ret = converter.convert(str);
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
