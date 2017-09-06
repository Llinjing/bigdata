package com.inveno.news.reformat.constant;

public class TestStrategy {
	
	public static void main(String [] args) {
		System.out.println(Strategy.getStrategy(Strategy.StrategyValue.UNKNOWN.getValue()));
		System.out.println(Strategy.getStrategy(Strategy.StrategyValue.FALLBACK.getValue()));
		System.out.println(Strategy.getStrategy(Strategy.StrategyValue.FLOW_EXPLORATION.getValue()));
		System.out.println(Strategy.getStrategy(Strategy.StrategyValue.FORCE_TOP.getValue()));
		System.out.println(Strategy.getStrategy(Strategy.StrategyValue.RECOMMENDATION.getValue()));
		System.out.println(Strategy.getStrategy(Strategy.StrategyValue.MIXED_INSERT_VERTICALNEWS_POOL1.getValue()));
		System.out.println(Strategy.getStrategy(Strategy.StrategyValue.MIXED_INSERT_QUALIFIED_NEWS.getValue()));
		System.out.println(Strategy.getStrategy(Strategy.StrategyValue.MIXED_INSERT_FB_RATE_100W.getValue()));
		System.out.println(Strategy.getStrategy(Strategy.StrategyValue.MIXED_INSERT_FB_RATE_50W.getValue()));
		System.out.println(Strategy.getStrategy(Strategy.StrategyValue.MIXED_INSERT_FB_RATE_10W.getValue()));
		System.out.println(Strategy.getStrategy(Strategy.StrategyValue.MIXED_INSERT_FB_RATE_1W.getValue()));
	}

}
