package com.inveno.news.reformat.schema;

import com.alibaba.fastjson.JSON;
import com.inveno.news.reformat.schema.ad.AdClickExtra;
import com.inveno.news.reformat.schema.ad.AdImpressionExtra;
import com.inveno.news.reformat.schema.ad.AdRequestExtra;
import com.inveno.news.reformat.schema.news.ActivityDwelltimeExtra;
import com.inveno.news.reformat.schema.news.ArticleClickExtra;
import com.inveno.news.reformat.schema.news.ArticleCompletenessExtra;
import com.inveno.news.reformat.schema.news.ArticleDwelltimeExtra;
import com.inveno.news.reformat.schema.news.ArticleImpressionExtra;
import com.inveno.news.reformat.schema.news.ArticleRequestExtra;
import com.inveno.news.reformat.schema.news.BackendServiceExtra;
import com.inveno.news.reformat.schema.news.ExtendEventExtra;
import com.inveno.news.reformat.schema.news.ListpageDwelltimeExtra;
import com.inveno.news.reformat.schema.news.Scenario;

public class CommonSchema {

	// common fields
	private String log_type = null;
	
	private String log_time = null;
	
	private String protocol = null;
	
	private String gate_ip = null;
	
	private String app = null;
	
	private String product_id = null;

	private String promotion = null;

	private String uid = null;

	private String fuid = null;

	private String app_ver = null;

	private String sdk_ver = null;

	private String api_ver = null;

	private String tk = null;

	private String report_time = null;

	private String network = null;

	private String sid = null;

	private String seq = null;
	
	private String imei = null;

	private String aid = null;

	private String idfa = null;

	private String brand = null;

	private String model = null;

	private String osv = null;

	private String platform = null;

	private String language = null;

	private String app_lan = null;

	private String mcc = null;

	private String mnc = null;

	private String nmcc = null;

	private String nmnc = null;

	private UserPacket upack = null;
	
	private Object referrer = null;

	private String event_id = null;
	
	private Scenario scenario = null;
	
	private String event_time = null;
	
	private String ad_source = null;
	
	private String result_code = null;

	private String order_num = null;
	
	// extra fields
	private ListpageDwelltimeExtra listpage_dwelltime_extra = null;
	
	private ArticleRequestExtra article_request_extra = null;

	private ArticleImpressionExtra article_impression_extra = null;

	private ArticleClickExtra article_click_extra = null;

	private ArticleDwelltimeExtra article_dwelltime_extra = null;
	
	private ArticleCompletenessExtra article_completeness_extra = null;
	
	private ActivityDwelltimeExtra activity_dwelltime_extra = null;
	
	private BackendServiceExtra backend_service_extra = null;
	
	private ExtendEventExtra extend_event_extra = null;
	
	/////////// ad 
	private AdRequestExtra ad_request_extra = null; 
	
	private AdImpressionExtra ad_impression_extra = null;
	
	private AdClickExtra ad_click_extra = null;
	
	// common fields method
	public void setLog_type(String log_type) {
		this.log_type = log_type;
	}
	
	public String getLog_type() {
		return log_type;
	}
	
	public void setLog_time(String log_time) {
		this.log_time = log_time;
	}

	public String getLog_time() {
		return log_time;
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

	public void setFuid(String fuid) {
		this.fuid = fuid;
	}

	public String getFuid() {
		return fuid;
	}

	public void setApp_ver(String app_ver) {
		this.app_ver = app_ver;
	}

	public String getApp_ver() {
		return app_ver;
	}

	public void setSdk_ver(String sdk_ver) {
		this.sdk_ver = sdk_ver;
	}

	public String getSdk_ver() {
		return sdk_ver;
	}

	public void setApi_ver(String api_ver) {
		this.api_ver = api_ver;
	}

	public String getApi_ver() {
		return api_ver;
	}

	public void setTk(String tk) {
		this.tk = tk;
	}

	public String getTk() {
		return tk;
	}

	public void setReport_time(String report_time) {
		this.report_time = report_time;
	}

	public String getReport_time() {
		return report_time;
	}

	public void setNetwork(String network) {
		this.network = network;
	}

	public String getNetwork() {
		return network;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public String getSid() {
		return sid;
	}

	public void setSeq(String seq) {
		this.seq = seq;
	}

	public String getSeq() {
		return seq;
	}
	
	public void setImei(String imei) {
		this.imei = imei;
	}
	
	public String getImei() {
		return imei;
	}

	public void setAid(String aid) {
		this.aid = aid;
	}

	public String getAid() {
		return aid;
	}

	public void setIdfa(String idfa) {
		this.idfa = idfa;
	}

	public String getIdfa() {
		return idfa;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getBrand() {
		return brand;
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

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public String getPlatform() {
		return platform;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getLanguage() {
		return language;
	}

	public void setApp_lan(String app_lan) {
		this.app_lan = app_lan;
	}

	public String getApp_lan() {
		return app_lan;
	}

	public void setMcc(String mcc) {
		this.mcc = mcc;
	}

	public String getMcc() {
		return mcc;
	}

	public void setMnc(String mnc) {
		this.mnc = mnc;
	}

	public String getMnc() {
		return mnc;
	}

	public void setNmcc(String nmcc) {
		this.nmcc = nmcc;
	}

	public String getNmcc() {
		return nmcc;
	}

	public void setNmnc(String nmnc) {
		this.nmnc = nmnc;
	}

	public String getNmnc() {
		return nmnc;
	}

	public void setUpack(UserPacket upack) {
		this.upack = upack;
	}

	public UserPacket getUpack() {
		return upack;
	}
	
	public void setReferrer(Object referrer) {
		this.referrer = referrer;
	}
	
	public Object getReferrer() {
		return referrer;
	}

	public void setEvent_id(String event_id) {
		this.event_id = event_id;
	}

	public String getEvent_id() {
		return event_id;
	}
	
	public void setScenario(Scenario scenario) {
		this.scenario = scenario;
	}

	public Scenario getScenario() {
		return scenario;
	}
	
	public void setEvent_time(String event_time) {
		this.event_time = event_time;
	}
	
	public String getEvent_time() {
		return event_time;
	}
	
	public void setAd_source(String ad_source) {
		this.ad_source = ad_source;
	}
	
	public String getAd_source() {
		return ad_source;
	}
	
	public String getOrder_num() {
		return order_num;
	}

	public void setOrder_num(String order_num) {
		this.order_num = order_num;
	}

	// extra fields method
	public void setListpage_dwelltime_extra(ListpageDwelltimeExtra listpage_dwelltime_extra) {
		this.listpage_dwelltime_extra = listpage_dwelltime_extra;
	}

	public ListpageDwelltimeExtra getListpage_dwelltime_extra() {
		return listpage_dwelltime_extra;
	}
	
	public void setArticle_request_extra(ArticleRequestExtra article_request_extra) {
		this.article_request_extra = article_request_extra;
	}

	public ArticleRequestExtra getArticle_request_extra() {
		return article_request_extra;
	}

	public void setArticle_impression_extra(ArticleImpressionExtra article_impression_extra) {
		this.article_impression_extra = article_impression_extra;
	}

	public ArticleImpressionExtra getArticle_impression_extra() {
		return article_impression_extra;
	}

	public void setArticle_click_extra(ArticleClickExtra article_click_extra) {
		this.article_click_extra = article_click_extra;
	}

	public ArticleClickExtra getArticle_click_extra() {
		return article_click_extra;
	}
	
	public void setArticle_dwelltime_extra(ArticleDwelltimeExtra article_dwelltime_extra) {
		this.article_dwelltime_extra = article_dwelltime_extra;
	}

	public ArticleDwelltimeExtra getArticle_dwelltime_extra() {
		return article_dwelltime_extra;
	}

	public void setArticle_completeness_extra(ArticleCompletenessExtra article_completeness_extra) {
		this.article_completeness_extra = article_completeness_extra;
	}

	public ArticleCompletenessExtra getArticle_completeness_extra() {
		return article_completeness_extra;
	}
	
	public void setActivity_dwelltime_extra(ActivityDwelltimeExtra activity_dwelltime_extra) {
		this.activity_dwelltime_extra = activity_dwelltime_extra;
	}
	
	public ActivityDwelltimeExtra getActivity_dwelltime_extra() {
		return activity_dwelltime_extra;
	}
	
	public void setBackend_service_extra(BackendServiceExtra backend_service_extra) {
		this.backend_service_extra = backend_service_extra;
	}
	
	public BackendServiceExtra getBackend_service_extra() {
		return backend_service_extra;
	}
	
	public void setExtend_event_extra(ExtendEventExtra extend_event_extra) {
		this.extend_event_extra = extend_event_extra;
	}
	
	public ExtendEventExtra getExtend_event_extra() {
		return extend_event_extra;
	}
	
	public void setAd_request_extra(AdRequestExtra ad_request_extra) {
		this.ad_request_extra = ad_request_extra;
	}
	
	public AdRequestExtra getAd_request_extra() {
		return ad_request_extra;
	}
	
	public void setAd_impression_extra(AdImpressionExtra ad_impression_extra) {
		this.ad_impression_extra = ad_impression_extra;
	}
	
	public AdImpressionExtra getAd_impression_extra() {
		return ad_impression_extra;
	}
	
	public void setAd_click_extra(AdClickExtra ad_click_extra) {
		this.ad_click_extra = ad_click_extra;
	}
	
	public AdClickExtra getAd_click_extra() {
		return ad_click_extra;
	}
	
	public String getResult_code() {
		return result_code;
	}

	public void setResult_code(String result_code) {
		this.result_code = result_code;
	}
	
	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}

}
