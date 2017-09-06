package com.inveno.news.reformat.schema;

import com.inveno.news.reformat.schema.news.ArticleCompletenessExtra;

public class TestArticleCompletenessExtra {
	
	public static void main(String [] args) {
		ArticleCompletenessExtra extra = new ArticleCompletenessExtra();
		extra.setContent_id("11");
		extra.setContent_type("dsfds");
		ContentPacket cpack = new ContentPacket();
		extra.setCpack(cpack);
		extra.setDisplay("111");
		extra.setProportion("90");
		System.out.println(extra.toString());
	}

}
