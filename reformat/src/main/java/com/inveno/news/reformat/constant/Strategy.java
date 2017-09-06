package com.inveno.news.reformat.constant;

import java.util.HashMap;
import java.util.Map;

public class Strategy {
	
	public static class StrategyValue {
		
		public static final StrategyValue UNKNOWN = new StrategyValue(-1);
		
		public static final StrategyValue PUSH = new StrategyValue(1);
		
		public static final StrategyValue NEARLINE_INTEREST = new StrategyValue(2);
		
		public static final StrategyValue FORCE_INSERT = new StrategyValue(3);
		
		public static final StrategyValue FALLBACK = new StrategyValue(4);
		
		public static final StrategyValue FLOW_EXPLORATION = new StrategyValue(5);
		
		public static final StrategyValue FORCE_TOP = new StrategyValue(6);

		public static final StrategyValue FIRSTSCREEN_FORCE_INSERT = new StrategyValue(7);

		public static final StrategyValue RECOMMENDATION = new StrategyValue(8);
		
		public static final StrategyValue MIXED_INSERT = new StrategyValue(9);
		
		public static final StrategyValue RELATIVE_RECOMMENDATION = new StrategyValue(10);
		
		public static final StrategyValue ADVERTISEMENT_RECOMMENDATION = new StrategyValue(11);
		
		public static final StrategyValue SEARCH = new StrategyValue(12);
		
		public static final StrategyValue SELF_MEDIA = new StrategyValue(13);
		
		public static final StrategyValue MIXED_INSERT_VIDEOBOOST = new StrategyValue(14);
		
		public static final StrategyValue MIXED_INSERT_PAID = new StrategyValue(15); 
		
		public static final StrategyValue DOWNGRADE_SCORE_61 = new StrategyValue(16);
		
		public static final StrategyValue DOWNGRADE_SCORE_81 = new StrategyValue(17); 
		
		public static final StrategyValue SPECIAL_ISSUE = new StrategyValue(18);
		
		public static final StrategyValue MIXED_INSERT_MOOD = new StrategyValue(19);
		
		// cn
		public static final StrategyValue MIXED_INSERT_HOT_NEWS_CN = new StrategyValue(101);
		                                  
		public static final StrategyValue MIXED_INSERT_GOSSIP_FASHION = new StrategyValue(102);
		                                  
		public static final StrategyValue MIXED_INSERT_QUALIFIED_LOWB = new StrategyValue(103);
		                                  
		public static final StrategyValue MIXED_INSERT_VERTICALNEWS_POOL1 = new StrategyValue(104);
		                                  
		public static final StrategyValue MIXED_INSERT_VERTICALNEWS_POOL2 = new StrategyValue(105);
		                                  
		public static final StrategyValue MIXED_INSERT_ZUIMEITIANQI = new StrategyValue(106);
		                                  
		public static final StrategyValue MIXED_INSERT_YOUTH_STROIES = new StrategyValue(107);
		                                  
		public static final StrategyValue MIXED_INSERT_DEEP_GOSSIP = new StrategyValue(108);
		                                  
		public static final StrategyValue MIXED_INSERT_ANECDOTES = new StrategyValue(109);
		                                  
		public static final StrategyValue MIXED_INSERT_UNOFFICIAL_HISTORY = new StrategyValue(110);
		                                  
		public static final StrategyValue MIXED_INSERT_ECCENTRIC_SOCIALNEWS = new StrategyValue(111);
		                                  
		public static final StrategyValue MIXED_INSERT_ALI_SPECIAL = new StrategyValue(112);
		                                  
		public static final StrategyValue MIXED_INSERT_DONGFANGWANG = new StrategyValue(113);
		                                  
		public static final StrategyValue MIXED_INSERT_JIANKANGYANGSHENG = new StrategyValue(114);
		                                  
		public static final StrategyValue MIXED_INSERT_TIEXUEJUNSHI = new StrategyValue(115);
		                                  
		public static final StrategyValue MIXED_INSERT_JUNSHITOUTIAO = new StrategyValue(116);
		
		public static final StrategyValue MIXED_INSERT_INTERNET = new StrategyValue(117);
		
		public static final StrategyValue MIXED_INSERT_ELECTRONIC_BUSINESS = new StrategyValue(118);
		
		public static final StrategyValue MIXED_INSERT_VIDEO = new StrategyValue(119);
		
		public static final StrategyValue MIXED_INSERT_FASHION_ENTERTAINMENT = new StrategyValue(120);
		
		// oversea                        
		public static final StrategyValue MIXED_INSERT_QUALIFIED_NEWS = new StrategyValue(201);
		                                  
		public static final StrategyValue MIXED_INSERT_HOT_NEWS_OVERSEA = new StrategyValue(202);
		                                  
		public static final StrategyValue MIXED_INSERT_FB_RATE_100W = new StrategyValue(203);
		                                  
		public static final StrategyValue MIXED_INSERT_FB_RATE_50W = new StrategyValue(204);
		                                  
		public static final StrategyValue MIXED_INSERT_FB_RATE_10W = new StrategyValue(205);
		                                  
		public static final StrategyValue MIXED_INSERT_FB_RATE_1W = new StrategyValue(206);
		                                  
		public static final StrategyValue MIXED_INSERT_COPYRIGHTEDBOOST = new StrategyValue(207);
		                                  
		public static final StrategyValue MIXED_INSERT_TWITTER_HOT_NEWS = new StrategyValue(208);
		                                  
		public static final StrategyValue MIXED_INSERT_FB_HOT_NEWS = new StrategyValue(209);
		
		public static final StrategyValue MIXED_INSERT_IMPORTANT_NEWS = new StrategyValue(210);
		
		public static final StrategyValue MIXED_INSERT_HIGH_CONTENT_QUALITY = new StrategyValue(211);
		
		public static final StrategyValue MIXED_INSERT_TOP_SCREEN = new StrategyValue(212);
		
		public static final StrategyValue MIXED_INSERT_TOP_ONE_POSITION = new StrategyValue(213);
		
		public static final StrategyValue MIXED_INSERT_YOUTUBE_RANKING = new StrategyValue(214);
		
		public static final StrategyValue MIXED_INSERT_YOUTUBE_100W50W = new StrategyValue(215);
		
		private int value = -1;
		
		private StrategyValue(int value) {
			this.value = value;
		}
		
		public int getValue() {
			return value;
		}
		
		@Override
		public String toString() {
			return String.valueOf(value);
		}
		
		@Override
		public boolean equals(Object obj) {
			boolean ret = false;
			
			if (obj instanceof LinkType) {
				ret = (value == ((StrategyValue)obj).value);
			} else if (obj instanceof Integer) {
				ret = (value == (Integer)obj);
			}
			
			return ret;
		}
	}
	
	public static class StrategyValueDesc {
		
		public static final StrategyValueDesc UNKNOWN = new StrategyValueDesc("unknown");
		
		public static final StrategyValueDesc PUSH = new StrategyValueDesc("push");
		
		public static final StrategyValueDesc NEARLINE_INTEREST = new StrategyValueDesc("nearline_interest");
		
		public static final StrategyValueDesc FORCE_INSERT = new StrategyValueDesc("force_insert");
		
		public static final StrategyValueDesc FALLBACK = new StrategyValueDesc("fallback");
		
		public static final StrategyValueDesc FLOW_EXPLORATION = new StrategyValueDesc("flow_exploration");
		
		public static final StrategyValueDesc FORCE_TOP = new StrategyValueDesc("force_top");
		
		public static final StrategyValueDesc FIRSTSCREEN_FORCE_INSERT = new StrategyValueDesc("firstscreen_force_insert");
		
		public static final StrategyValueDesc RECOMMENDATION = new StrategyValueDesc("recommendation");
		
		public static final StrategyValueDesc MIXED_INSERT = new StrategyValueDesc("mixed_insert");
		
		public static final StrategyValueDesc RELATIVE_RECOMMENDATION = new StrategyValueDesc("relative_recommendation");

		public static final StrategyValueDesc ADVERTISEMENT_RECOMMENDATION = new StrategyValueDesc("advertisement_recommendation");
		
		public static final StrategyValueDesc SEARCH = new StrategyValueDesc("search");
		
		public static final StrategyValueDesc SELF_MEDIA = new StrategyValueDesc("self_media");
		
		public static final StrategyValueDesc MIXED_INSERT_VIDEOBOOST = new StrategyValueDesc("mixed_insert_videoboost");
		
		public static final StrategyValueDesc MIXED_INSERT_PAID = new StrategyValueDesc("mixed_insert_paid");
		
		public static final StrategyValueDesc DOWNGRADE_SCORE_61 = new StrategyValueDesc("downgrade_score_0.61");
		
		public static final StrategyValueDesc DOWNGRADE_SCORE_81 = new StrategyValueDesc("downgrade_score_0.81"); 
		
		public static final StrategyValueDesc SPECIAL_ISSUE = new StrategyValueDesc("special_issue");
		
		public static final StrategyValueDesc MIXED_INSERT_MOOD = new StrategyValueDesc("mixed_insert_mood");
		
		// cn
		public static final StrategyValueDesc MIXED_INSERT_HOT_NEWS_CN = new StrategyValueDesc("mixed_insert_hotnews_cn");
		                                          
		public static final StrategyValueDesc MIXED_INSERT_GOSSIP_FASHION = new StrategyValueDesc("mixed_insert_gossip_fashion");
		                                          
		public static final StrategyValueDesc MIXED_INSERT_QUALIFIED_LOWB = new StrategyValueDesc("mixed_insert_qualified_lowb");
		                                          
		public static final StrategyValueDesc MIXED_INSERT_VERTICALNEWS_POOL1 = new StrategyValueDesc("mixed_insert_verticalnews_pool1");
		                                          
		public static final StrategyValueDesc MIXED_INSERT_VERTICALNEWS_POOL2 = new StrategyValueDesc("mixed_insert_verticalnews_pool2");
		                                          
		public static final StrategyValueDesc MIXED_INSERT_ZUIMEITIANQI = new StrategyValueDesc("mixed_insert_zuimeitianqi");
		                                          
		public static final StrategyValueDesc MIXED_INSERT_YOUTH_STROIES = new StrategyValueDesc("mixed_insert_youth_stroies");
		                                          
		public static final StrategyValueDesc MIXED_INSERT_DEEP_GOSSIP = new StrategyValueDesc("mixed_insert_deep_gossip");
		                                          
		public static final StrategyValueDesc MIXED_INSERT_ANECDOTES = new StrategyValueDesc("mixed_insert_anecdotes");
		                                          
		public static final StrategyValueDesc MIXED_INSERT_UNOFFICIAL_HISTORY = new StrategyValueDesc("mixed_insert_unofficial_history");
		                                          
		public static final StrategyValueDesc MIXED_INSERT_ECCENTRIC_SOCIALNEWS = new StrategyValueDesc("mixed_insert_eccentric_socialnews");
		                                          
		public static final StrategyValueDesc MIXED_INSERT_ALI_SPECIAL = new StrategyValueDesc("mixed_insert_ali_special");
		                                          
		public static final StrategyValueDesc MIXED_INSERT_DONGFANGWANG = new StrategyValueDesc("mixed_insert_dongfangwang");
		                                          
		public static final StrategyValueDesc MIXED_INSERT_JIANKANGYANGSHENG = new StrategyValueDesc("mixed_insert_jiankangyangsheng");
		                                          
		public static final StrategyValueDesc MIXED_INSERT_TIEXUEJUNSHI = new StrategyValueDesc("mixed_insert_tiexuejunshi");
		                                          
		public static final StrategyValueDesc MIXED_INSERT_JUNSHITOUTIAO = new StrategyValueDesc("mixed_insert_junshitoutiao");
		
		public static final StrategyValueDesc MIXED_INSERT_INTERNET = new StrategyValueDesc("mixed_insert_internet");
		
		public static final StrategyValueDesc MIXED_INSERT_ELECTRONIC_BUSINESS = new StrategyValueDesc("mixed_insert_electronic_business");
		
		public static final StrategyValueDesc MIXED_INSERT_VIDEO = new StrategyValueDesc("mixed_insert_video");
		
		public static final StrategyValueDesc MIXED_INSERT_FASHION_ENTERTAINMENT = new StrategyValueDesc("mixed_insert_fashion_entertainment");
		
		// oversea                                
		public static final StrategyValueDesc MIXED_INSERT_QUALIFIED_NEWS = new StrategyValueDesc("mixed_insert_qualified_news");
		                                          
		public static final StrategyValueDesc MIXED_INSERT_HOT_NEWS_OVERSEA = new StrategyValueDesc("mixed_insert_hotnews_oversea");
		                                          
		public static final StrategyValueDesc MIXED_INSERT_FB_RATE_100W = new StrategyValueDesc("mixed_insert_fb_rate_100w");
		                                      
		public static final StrategyValueDesc MIXED_INSERT_FB_RATE_50W = new StrategyValueDesc("mixed_insert_fb_rate_50w");
		                                          
		public static final StrategyValueDesc MIXED_INSERT_FB_RATE_10W = new StrategyValueDesc("mixed_insert_fb_rate_10w");
		                                          
		public static final StrategyValueDesc MIXED_INSERT_FB_RATE_1W = new StrategyValueDesc("mixed_insert_fb_rate_1w");
		                                          
		public static final StrategyValueDesc MIXED_INSERT_COPYRIGHTEDBOOST = new StrategyValueDesc("mixed_insert_copyrightedboost");
		
		public static final StrategyValueDesc MIXED_INSERT_TWITTER_HOT_NEWS = new StrategyValueDesc("mixed_insert_twitter_hot_news");
		
		public static final StrategyValueDesc MIXED_INSERT_FB_HOT_NEWS = new StrategyValueDesc("mixed_insert_fb_hot_news");
		
		public static final StrategyValueDesc MIXED_INSERT_IMPORTANT_NEWS = new StrategyValueDesc("mixed_insert_important_news");
		
		public static final StrategyValueDesc MIXED_INSERT_HIGH_CONTENT_QUALITY = new StrategyValueDesc("mixed_insert_high_content_quality");
		
		public static final StrategyValueDesc MIXED_INSERT_TOP_SCREEN = new StrategyValueDesc("mixed_insert_top_screen");
		
		public static final StrategyValueDesc MIXED_INSERT_TOP_ONE_POSITION = new StrategyValueDesc("mixed_insert_top_one_position");
		
		public static final StrategyValueDesc MIXED_INSERT_YOUTUBE_RANKING = new StrategyValueDesc("mixed_insert_youtube_ranking");
		
		public static final StrategyValueDesc MIXED_INSERT_YOUTUBE_100W50W = new StrategyValueDesc("mixed_insert_youtube_100w50w");
		
		private String desc = null;
		
		private StrategyValueDesc(String desc) {
			this.desc = desc;
		}
		
		@Override
		public String toString() {
			return desc;
		}
		
		@Override
		public boolean equals(Object obj) {
			boolean ret = false;
			
			if (obj instanceof ContentType) {
				ret = desc.contentEquals(((StrategyValueDesc)obj).desc);
			} else if (obj instanceof String) {
				ret = desc.contentEquals((String)obj);
			}
			
			return ret;
		}
	}
	
	public static Map<Integer, String> stragegy_desc = new HashMap<Integer, String>();
	
	static {
		stragegy_desc.put(StrategyValue.PUSH.getValue(), StrategyValueDesc.PUSH.toString());
		stragegy_desc.put(StrategyValue.NEARLINE_INTEREST.getValue(), StrategyValueDesc.NEARLINE_INTEREST.toString());
		stragegy_desc.put(StrategyValue.FORCE_INSERT.getValue(), StrategyValueDesc.FORCE_INSERT.toString());
		stragegy_desc.put(StrategyValue.FALLBACK.getValue(), StrategyValueDesc.FALLBACK.toString());
		stragegy_desc.put(StrategyValue.FLOW_EXPLORATION.getValue(), StrategyValueDesc.FLOW_EXPLORATION.toString());
		stragegy_desc.put(StrategyValue.FORCE_TOP.getValue(), StrategyValueDesc.FORCE_TOP.toString());
		stragegy_desc.put(StrategyValue.FIRSTSCREEN_FORCE_INSERT.getValue(), StrategyValueDesc.FIRSTSCREEN_FORCE_INSERT.toString());
		stragegy_desc.put(StrategyValue.RECOMMENDATION.getValue(), StrategyValueDesc.RECOMMENDATION.toString());
		stragegy_desc.put(StrategyValue.MIXED_INSERT.getValue(), StrategyValueDesc.MIXED_INSERT.toString());
		stragegy_desc.put(StrategyValue.RELATIVE_RECOMMENDATION.getValue(), StrategyValueDesc.RELATIVE_RECOMMENDATION.toString());
		stragegy_desc.put(StrategyValue.ADVERTISEMENT_RECOMMENDATION.getValue(), StrategyValueDesc.ADVERTISEMENT_RECOMMENDATION.toString());
		stragegy_desc.put(StrategyValue.SEARCH.getValue(), StrategyValueDesc.SEARCH.toString());
		stragegy_desc.put(StrategyValue.SELF_MEDIA.getValue(), StrategyValueDesc.SELF_MEDIA.toString());
		stragegy_desc.put(StrategyValue.MIXED_INSERT_VIDEOBOOST.getValue(), StrategyValueDesc.MIXED_INSERT_VIDEOBOOST.toString());
		stragegy_desc.put(StrategyValue.MIXED_INSERT_PAID.getValue(), StrategyValueDesc.MIXED_INSERT_PAID.toString());
		stragegy_desc.put(StrategyValue.DOWNGRADE_SCORE_61.getValue(), StrategyValueDesc.DOWNGRADE_SCORE_61.toString());
		stragegy_desc.put(StrategyValue.DOWNGRADE_SCORE_81.getValue(), StrategyValueDesc.DOWNGRADE_SCORE_81.toString());
		stragegy_desc.put(StrategyValue.SPECIAL_ISSUE.getValue(), StrategyValueDesc.SPECIAL_ISSUE.toString());
		stragegy_desc.put(StrategyValue.MIXED_INSERT_MOOD.getValue(), StrategyValueDesc.MIXED_INSERT_MOOD.toString());
		
		// cn
		stragegy_desc.put(StrategyValue.MIXED_INSERT_HOT_NEWS_CN.getValue(), StrategyValueDesc.MIXED_INSERT_HOT_NEWS_CN.toString());
		stragegy_desc.put(StrategyValue.MIXED_INSERT_GOSSIP_FASHION.getValue(), StrategyValueDesc.MIXED_INSERT_GOSSIP_FASHION.toString());
		stragegy_desc.put(StrategyValue.MIXED_INSERT_QUALIFIED_LOWB.getValue(), StrategyValueDesc.MIXED_INSERT_QUALIFIED_LOWB.toString());
		stragegy_desc.put(StrategyValue.MIXED_INSERT_VERTICALNEWS_POOL1.getValue(), StrategyValueDesc.MIXED_INSERT_VERTICALNEWS_POOL1.toString());
		stragegy_desc.put(StrategyValue.MIXED_INSERT_VERTICALNEWS_POOL2.getValue(), StrategyValueDesc.MIXED_INSERT_VERTICALNEWS_POOL2.toString());
		stragegy_desc.put(StrategyValue.MIXED_INSERT_ZUIMEITIANQI.getValue(), StrategyValueDesc.MIXED_INSERT_ZUIMEITIANQI.toString());
		stragegy_desc.put(StrategyValue.MIXED_INSERT_YOUTH_STROIES.getValue(), StrategyValueDesc.MIXED_INSERT_YOUTH_STROIES.toString());
		stragegy_desc.put(StrategyValue.MIXED_INSERT_DEEP_GOSSIP.getValue(), StrategyValueDesc.MIXED_INSERT_DEEP_GOSSIP.toString());
		stragegy_desc.put(StrategyValue.MIXED_INSERT_ANECDOTES.getValue(), StrategyValueDesc.MIXED_INSERT_ANECDOTES.toString());
		stragegy_desc.put(StrategyValue.MIXED_INSERT_UNOFFICIAL_HISTORY.getValue(), StrategyValueDesc.MIXED_INSERT_UNOFFICIAL_HISTORY.toString());
		stragegy_desc.put(StrategyValue.MIXED_INSERT_ECCENTRIC_SOCIALNEWS.getValue(), StrategyValueDesc.MIXED_INSERT_ECCENTRIC_SOCIALNEWS.toString());
		stragegy_desc.put(StrategyValue.MIXED_INSERT_ALI_SPECIAL.getValue(), StrategyValueDesc.MIXED_INSERT_ALI_SPECIAL.toString());
		stragegy_desc.put(StrategyValue.MIXED_INSERT_DONGFANGWANG.getValue(), StrategyValueDesc.MIXED_INSERT_DONGFANGWANG.toString());
		stragegy_desc.put(StrategyValue.MIXED_INSERT_JIANKANGYANGSHENG.getValue(), StrategyValueDesc.MIXED_INSERT_JIANKANGYANGSHENG.toString());
		stragegy_desc.put(StrategyValue.MIXED_INSERT_TIEXUEJUNSHI.getValue(), StrategyValueDesc.MIXED_INSERT_TIEXUEJUNSHI.toString());
		stragegy_desc.put(StrategyValue.MIXED_INSERT_JUNSHITOUTIAO.getValue(), StrategyValueDesc.MIXED_INSERT_JUNSHITOUTIAO.toString());
		stragegy_desc.put(StrategyValue.MIXED_INSERT_INTERNET.getValue(), StrategyValueDesc.MIXED_INSERT_INTERNET.toString());
		stragegy_desc.put(StrategyValue.MIXED_INSERT_ELECTRONIC_BUSINESS.getValue(), StrategyValueDesc.MIXED_INSERT_ELECTRONIC_BUSINESS.toString());
		stragegy_desc.put(StrategyValue.MIXED_INSERT_VIDEO.getValue(), StrategyValueDesc.MIXED_INSERT_VIDEO.toString());
		stragegy_desc.put(StrategyValue.MIXED_INSERT_FASHION_ENTERTAINMENT.getValue(), StrategyValueDesc.MIXED_INSERT_FASHION_ENTERTAINMENT.toString());
		
		// oversea
		stragegy_desc.put(StrategyValue.MIXED_INSERT_QUALIFIED_NEWS.getValue(), StrategyValueDesc.MIXED_INSERT_QUALIFIED_NEWS.toString());
		stragegy_desc.put(StrategyValue.MIXED_INSERT_HOT_NEWS_OVERSEA.getValue(), StrategyValueDesc.MIXED_INSERT_HOT_NEWS_OVERSEA.toString());
		stragegy_desc.put(StrategyValue.MIXED_INSERT_FB_RATE_100W.getValue(), StrategyValueDesc.MIXED_INSERT_FB_RATE_100W.toString());
		stragegy_desc.put(StrategyValue.MIXED_INSERT_FB_RATE_50W.getValue(), StrategyValueDesc.MIXED_INSERT_FB_RATE_50W.toString());
		stragegy_desc.put(StrategyValue.MIXED_INSERT_FB_RATE_10W.getValue(), StrategyValueDesc.MIXED_INSERT_FB_RATE_10W.toString());
		stragegy_desc.put(StrategyValue.MIXED_INSERT_FB_RATE_1W.getValue(), StrategyValueDesc.MIXED_INSERT_FB_RATE_1W.toString());
		stragegy_desc.put(StrategyValue.MIXED_INSERT_COPYRIGHTEDBOOST.getValue(), StrategyValueDesc.MIXED_INSERT_COPYRIGHTEDBOOST.toString());
		stragegy_desc.put(StrategyValue.MIXED_INSERT_TWITTER_HOT_NEWS.getValue(), StrategyValueDesc.MIXED_INSERT_TWITTER_HOT_NEWS.toString());
		stragegy_desc.put(StrategyValue.MIXED_INSERT_FB_HOT_NEWS.getValue(), StrategyValueDesc.MIXED_INSERT_FB_HOT_NEWS.toString());
		stragegy_desc.put(StrategyValue.MIXED_INSERT_IMPORTANT_NEWS.getValue(), StrategyValueDesc.MIXED_INSERT_IMPORTANT_NEWS.toString());
		stragegy_desc.put(StrategyValue.MIXED_INSERT_HIGH_CONTENT_QUALITY.getValue(), StrategyValueDesc.MIXED_INSERT_HIGH_CONTENT_QUALITY.toString());
		stragegy_desc.put(StrategyValue.MIXED_INSERT_TOP_SCREEN.getValue(), StrategyValueDesc.MIXED_INSERT_TOP_SCREEN.toString());
		stragegy_desc.put(StrategyValue.MIXED_INSERT_TOP_ONE_POSITION.getValue(), StrategyValueDesc.MIXED_INSERT_TOP_ONE_POSITION.toString());		
		stragegy_desc.put(StrategyValue.MIXED_INSERT_YOUTUBE_RANKING.getValue(), StrategyValueDesc.MIXED_INSERT_YOUTUBE_RANKING.toString());
		stragegy_desc.put(StrategyValue.MIXED_INSERT_YOUTUBE_100W50W.getValue(), StrategyValueDesc.MIXED_INSERT_YOUTUBE_100W50W.toString());
	}
	
	public static String getStrategy(String strategy_str) {
		int strategy = StrategyValue.UNKNOWN.getValue();
		try {
			strategy = Integer.valueOf(strategy_str);
		} catch (Exception e){
		}
		return getStrategy(strategy);
	}
	
	public static String getStrategy(int strategy) {
		String ret = stragegy_desc.get(strategy);
		if (ret == null) {
			ret = StrategyValueDesc.UNKNOWN.toString();	
		}
		return ret;
	}

}
