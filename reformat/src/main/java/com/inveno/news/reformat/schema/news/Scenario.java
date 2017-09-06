package com.inveno.news.reformat.schema.news;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.inveno.news.reformat.constant.ScenarioType;

public class Scenario {
	
	private static final Logger logger = LoggerFactory.getLogger(Scenario.class);

	private static final String UNKNOWN = "unknown";
	
	private String position_type = UNKNOWN;
	
	private String position_type_desc = UNKNOWN;

	private String position = UNKNOWN;
	
	private String position_desc = UNKNOWN;

	private String channel = UNKNOWN;

	private String channel_desc = UNKNOWN;
	
	private String desc = UNKNOWN;

	public void setPosition_type(String position_type) {
		this.position_type = position_type;
	}

	public String getPosition_type() {
		return position_type;
	}
	
	public void setPosition_type_desc(String position_type_desc) {
		this.position_type_desc = position_type_desc;
	}

	public String getPosition_type_desc() {
		return position_type_desc;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getPosition() {
		return position;
	}
	
	public void setPosition_desc(String position_desc) {
		this.position_desc = position_desc;
	}

	public String getPosition_desc() {
		return position_desc;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getChannel() {
		return channel;
	}
	
	public void setChannel_desc(String channel_desc) {
		this.channel_desc = channel_desc;
	}

	public String getChannel_desc() {
		return channel_desc;
	}
	
	public void setDesc(String desc) {
		this.desc = desc;
	}
	
	public String getDesc() {
		return desc;
	}

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}
	
	public static Scenario parseScenario(String product_id, String scenario_str) {
		Scenario scenario = null;
		
		try {
			int value = Integer.valueOf(scenario_str.substring(2), 16).intValue();
			int position_type = ((value >> 16) & 0xFF);	// 6-23 bit
			scenario = new Scenario();
			scenario.setPosition_type("0x" + scenario_str.substring(2, 4));
			scenario.setPosition_type_desc(ScenarioType.PositionType.getPositionTypeDesc(position_type));
			scenario.setDesc(scenario.getPosition_type_desc());
			scenario.setPosition("0x" + scenario_str.substring(4, 6));
			scenario.setPosition_desc(ScenarioType.Position.getPositionDesc(product_id + scenario_str.substring(0, 6)));
			
			String channel_str = "0x" + scenario_str.substring(6, 8);
			scenario.setChannel(channel_str);
			scenario.setChannel_desc(ScenarioType.Channel.getChannelDesc(channel_str));
		} catch (Exception e) {
			logger.warn(e.getMessage(), e);
			scenario = new Scenario();
		}
		
		return scenario;
	}

}
