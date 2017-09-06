package com.inveno.news.reformat.schema;

import com.inveno.news.reformat.schema.news.ArticleImpressionExtra;

public class TestArticleImpressionExtra {
	
	public static void main(String [] args) {
		ArticleImpressionExtra extra = new ArticleImpressionExtra();
		extra.setServer_time("2345");
		extra.setContent_id("11");
		extra.setContent_type("dsfds");
		ContentPacket cpack = new ContentPacket();
		extra.setCpack(cpack);
		System.out.println(extra.toString());
	}

}
