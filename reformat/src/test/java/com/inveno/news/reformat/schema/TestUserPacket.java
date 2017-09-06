package com.inveno.news.reformat.schema;

import com.inveno.news.reformat.schema.UserPacket;

public class TestUserPacket {
	
	public static void main(String [] args) {
		UserPacket upack = new UserPacket();
		System.out.println(upack.toString());
		
		upack.setNews_configid("108");
		upack.setAd_configid("108");
		upack.setBiz_configid("99");
		System.out.println(upack.toString());
	}

}
