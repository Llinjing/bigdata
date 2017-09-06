package com.inveno.news.reformat.convert;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

public class TestProfileLogConverter {
	
	public static void main(String [] args) {
		ProfileLogConverter converter = new ProfileLogConverter();
		
		System.out.println("1#############");
		String str = "profile&172.31.12.139&2016-11-30 13:56:16&noticias&01011611301356165201000000524307&&&&&720&1280&1.7791901G&1.0.9.0.0.3&1&op&&android&&pm&&1.0.9.0.0.3_gp&74cea6842e0bdef1&&gp&"; 
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
		str = "profile&172.31.12.139&2016-11-30 13:56:25&noticias&01011611301356255201000000525003&352155076242052&6182088297&9c:df:b1:04:54:68&&320&480&0.46047592G&1.0.9.0.0.3&1&op&&android&&pm&&1.0.9.0.0.3_gp&33c4e0d933d0669&%7B%22af%5Ftranid%22%3A%22Kft2JHuWIaxnp2KEsmcwEg%22%2C%22pid%22%3A%22yeahmobi%5Fint%22%2C%22af%5Fclick%5Flookback%22%3A%227d%22%2C%22c%22%3A%22MX%22%2C%22clickid%22%3A%2216784a780%2Dcff5%2Dd4a4%2Dc113174fa75ee63dba1a0953412ac7bb8ea0322302e0027%22%2C%22af%5Fsiteid%22%3A%227482%22%2C%22af%5Fsub1%22%3A%2297673%22%7D&gp&";
		ret = converter.convert(str);
		if (ret != null) {
			for (String key : ret.keySet()) {
				System.out.println(key);
				for (String s : ret.get(key)) {
					System.out.println(s);
				}
			}
		}
		
		System.out.println("3#############");
		str = "profile&172.31.12.139&2016-11-30 00:30:01&hotoday&01011611300030014801000000426601&911363959887274&&90:21:81:1b:d4:5d&&480&854&0.94781494G&2.3.2.0.0.3&1&op&&android&&pm&&2.3.2.0.0.3_gp&4779f80901fcc0b5&%7B%22referrer%22%3A%22af%5Ftranid%253Dv6aQrNdvFKiZ%5FnZfdEFjrQ%2526pid%253Dyeahmobi%5Fint%2526af%5Fclick%5Flookback%253D7d%2526c%253D%5FIN%2526clickid%253D%257Btransaction%5Fid%257D%2526af%5Fsiteid%253D%257Baffiliate%5Fid%257D%2526af%5Fsub1%253D%257Baff%5Fsub8%257D%22%7D&gp&";
		ret = converter.convert(str);
		if (ret != null) {
			for (String key : ret.keySet()) {
				System.out.println(key);
				for (String s : ret.get(key)) {
					System.out.println(s);
				}
			}
		}
		
		System.out.println("4#############");
		str = "profile&172.31.12.139&2016-12-05 19:01:42&hotoday&01011612051901434801000031847708&911351100178073&&&&720&1280&0.94376755G&2.3.3.0.0.4&1&op&&android&&pm&&2.3.3.0.0.4_gp&7e61add48f66f354&%7B%22utm%5Fsource%22%3A%22%28not%2520set%29%22%2C%22utm%5Fmedium%22%3A%22%28not%2520set%29%22%7D&gp&";
		ret = converter.convert(str);
		if (ret != null) {
			for (String key : ret.keySet()) {
				System.out.println(key);
				for (String s : ret.get(key)) {
					System.out.println(s);
				}
			}
		}
		
		System.out.println("5#############json");
		str = "profile&172.31.12.139&2016-11-30 00:02:30&noticias&01011611300002315201000000279705&358991038226161&&00:d3:df:f0:60:98&&540&888&0.44844818G&1.0.9.0.0.3&1&op&&anDR&&pm&&1.0.9.0.0.3_gp&c600d32520e44cbd&%7B%22af%5Ftranid%22%3A%22GvTpslYUN2R37g8Lw%5FETIw%22%2C%22pid%22%3A%22yeahmobi%5Fint%22%2C%22af%5Fclick%5Flookback%22%3A%227d%22%2C%22c%22%3A%22MX%22%2C%22clickid%22%3A%2258785c192%2D389d%2D54cc%2D521303909123da770628a6e8b3b9fb0823e29236df30033%22%2C%22af%5Fsiteid%22%3A%227482%22%2C%22af%5Fsub1%22%3A%2297673%22%7D&gp&";
		ret = converter.convert(str);
		if (ret != null) {
			for (String key : ret.keySet()) {
				System.out.println(key);
				for (String s : ret.get(key)) {
					System.out.println(s);
				}
			}
		}
		
		System.out.println("6#############json");
		str = "profile&2016-09-02 05:22:37&hotoday&010116090202016-09-02 06:09:44&mata&01011609020609445101000006821607&358542065314352&&B4:EF:39:06:D8:2B&&480&800&0.44235992G&1.0.3.0.0.3&1&op&&android&&pm&&1.0.3.0.0.3_gp&300d51a91cd4fff8&";
		ret = converter.convert(str);
		if (ret != null) {
			for (String key : ret.keySet()) {
				System.out.println(key);
				for (String s : ret.get(key)) {
					System.out.println(s);
				}
			}
		}
		
		System.out.println("7#############json");
		str = "profile&2016-09-03 20:48:02&hotoday&010116090320480348012016-09-03 20:49:43&mata&01011609032049435101000011504207&356876056426136&&00:E3:B2:3B:CE:EF&&480&800&0.8226929G&1.0.4.0.0.3&1&op&&android&&pm&&1.0.4.0.0.3_gp&799d710f048e38f2&";
		ret = converter.convert(str);
		if (ret != null) {
			for (String key : ret.keySet()) {
				System.out.println(key);
				for (String s : ret.get(key)) {
					System.out.println(s);
				}
			}
		}
		
		//converterFile(converter);
	}

	public static void converterFile(ProfileLogConverter converter){
		System.out.println("converter profile start");
		String inputFilePath = "D:\\tmp\\Profile_meidong_newUser_utc_1201.log";
		String outputFilePath = "D:\\tmp\\profile_20161201.log";
		
		InputStream is = null;
		FileWriter writer = null;
		try {
		    is = new FileInputStream(inputFilePath);
		    @SuppressWarnings("resource")
			BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"), 512);
	        writer = new FileWriter(outputFilePath, true);   
		    
		    for (String line = reader.readLine(); line != null; line = reader.readLine()) {
		        line = line.trim();
		        //System.out.println(line);
		        Map<String, List<String>> ret = converter.convert("profile&172.31.12.139&"+line);
				if (ret != null) {
					for (String key : ret.keySet()) {
						//System.out.println(key);
						for (String s : ret.get(key)) {
							//System.out.println(s);
							writer.write(s+"\n");
						}
					}
				}
		    }
		}catch (FileNotFoundException fnfe){
		    fnfe.printStackTrace();
		}catch (IOException ioe){
		    ioe.printStackTrace();
		} finally {
		    try {
		        if (is != null) {
		            is.close();
		            is = null;
		        }
		        if (writer != null) {
		        	writer.close();
		        	writer = null;
		        }
		    } catch (IOException e) {
		        e.printStackTrace();
		    }
		}
		System.out.println("converter profile finish");
	}
}
