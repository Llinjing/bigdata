package com.inveno.news.reformat.schema;

import com.inveno.news.reformat.schema.news.Refer;

public class TestRefer {
	
	public static void main(String [] args) {
		Refer refer = new Refer();
		refer.setContent_id("12345");
		refer.setContent_type("news");
		System.out.println(refer.toString());
	}

}
