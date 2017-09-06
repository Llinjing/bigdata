package com.inveno.news.reformat.schema;

import com.inveno.news.reformat.schema.news.ProfileSchema;

public class TestNewUserSchema {
	
	public static void main(String [] args) {
		ProfileSchema schema = new ProfileSchema();
		schema.setGate_ip("1.1.1.1");
		schema.setUid("abcd");
		schema.setApp_ver("sadf.ff");
		schema.setApp("hotoday");
		schema.setApp_lan("chinese");
		System.out.println(schema.toString());
	}

}
