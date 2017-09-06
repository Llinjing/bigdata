package com.inveno.bigdata.sync.constant;

public class GmpSchema {
	
	public String product_id = null;
	
	public float click = 0.0f;
	
	public float impression = 0.0f;
	
	public float ctr = -1.0f;
	
	public GmpSchema(String product_id, float click, float impression, float ctr) {
		this.product_id = product_id;
		this.click = click;
		this.impression = impression;
		this.ctr = ctr;
	}
	
}
