package com.inveno.news.reformat.schema;

import com.inveno.news.reformat.schema.news.ArticleDwelltimeExtra;

public class TestArticleDwelltimeExtra {
	
	public static void main(String [] args) {
		ArticleDwelltimeExtra extra = new ArticleDwelltimeExtra();
		extra.setContent_id("11");
		extra.setContent_type("dsfds");
		ContentPacket cpack = new ContentPacket();
		extra.setCpack(cpack);
		extra.setDisplay("111");
		extra.setDwelltime("90");
		System.out.println(extra.toString());
	}

}
