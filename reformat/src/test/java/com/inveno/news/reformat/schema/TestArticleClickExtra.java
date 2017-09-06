package com.inveno.news.reformat.schema;

import com.inveno.news.reformat.schema.news.ArticleClickExtra;

public class TestArticleClickExtra {
	
	public static void main(String [] args) {
		ArticleClickExtra extra = new ArticleClickExtra();
		extra.setContent_id("11");
		extra.setContent_type("dsfds");
		
		ContentPacket cpack = new ContentPacket();
		extra.setCpack(cpack);
		extra.setDisplay("111");
		System.out.println(extra.toString());
	}

}
