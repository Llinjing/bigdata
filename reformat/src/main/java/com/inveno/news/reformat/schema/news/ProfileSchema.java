package com.inveno.news.reformat.schema.news;

import com.alibaba.fastjson.JSON;

public class ProfileSchema {
	
	private String log_type = null;
	
	private String protocol = null;

	private String gate_ip = null;

	private String event_time = null;

	private String app = null;

	private String product_id = null;

	private String promotion = null;

	private String uid = null;

	private String aid = null;

	private String app_ver = null;

	private String network = null;

	private String model = null;

	private String osv = null;
	
	private String app_lan = null;
	
	private String mac = null;

	private String imei = null;
	
	private String platform = null;
	
	public void setLog_type(String log_type) {
		this.log_type = log_type;
	}
	
	public String getLog_type() {
		return log_type;
	}
	
	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}
	
	public String getProtocol() {
		return protocol;
	}

	public void setGate_ip(String gate_ip) {
		this.gate_ip = gate_ip;
	}

	public String getGate_ip() {
		return gate_ip;
	}

	public void setEvent_time(String event_time) {
		this.event_time = event_time;
	}

	public String getEvent_time() {
		return event_time;
	}

	public void setApp(String app) {
		this.app = app;
	}

	public String getApp() {
		return app;
	}

	public void setProduct_id(String product_id) {
		this.product_id = product_id;
	}

	public String getProduct_id() {
		return product_id;
	}

	public void setPromotion(String promotion) {
		this.promotion = promotion;
	}

	public String getPromotion() {
		return promotion;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getUid() {
		return uid;
	}

	public void setAid(String aid) {
		this.aid = aid;
	}

	public String getAid() {
		return aid;
	}

	public void setApp_ver(String app_ver) {
		this.app_ver = app_ver;
	}

	public String getApp_ver() {
		return app_ver;
	}

	public void setNetwork(String network) {
		this.network = network;
	}

	public String getNetwork() {
		return network;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getModel() {
		return model;
	}

	public void setOsv(String osv) {
		this.osv = osv;
	}

	public String getOsv() {
		return osv;
	}
	
	public void setApp_lan(String app_lan) {
		this.app_lan = app_lan;
	}
	
	public String getApp_lan() {
		return app_lan;
	}

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}
	
	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}

}
