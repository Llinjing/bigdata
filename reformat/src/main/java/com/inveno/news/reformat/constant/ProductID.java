package com.inveno.news.reformat.constant;

public class ProductID {
	
	/*
	 * 日志中的productid		reformat后的productid
		lenovowid				lenovo_widget
		0febb9b4-486			wifiyaoshi
		huiyueapp||huiyue		huisuopin
		fuyiping-gionee||fyp-ginee-h5	fuyiping-gionee
		H5						h5_sdk
	 */
	
	public static final ProductID XIAOZHI = new ProductID("xiaozhi");

	public static final ProductID COOLPAD = new ProductID("coolpad");
	
	public static final ProductID QIKU = new ProductID("qiku");
	
	public static final ProductID HUAWEI_APK = new ProductID("emui");
	
	public static final ProductID HUAWEI_PAD = new ProductID("emuihd");
	
	public static final ProductID HUAWEI_QINGGAN = new ProductID("emuiqg");
	
	public static final ProductID LENOVO_WIDGET = new ProductID("lenovo_widget");
	
	public static final ProductID HUAQIN = new ProductID("huqin");
	
	public static final ProductID HAOCHENG = new ProductID("haocheng");
	
	public static final ProductID INFOCUS = new ProductID("infocus");
	
	public static final ProductID TCL = new ProductID("tcl");
	
	public static final ProductID DUOWEI = new ProductID("duowei");
	
	public static final ProductID ANGDA = new ProductID("angda");
	
	public static final ProductID XIAOLAJIAO = new ProductID("xiaolajiao");
	
	public static final ProductID TIANYU = new ProductID("tianyu");
	
	public static final ProductID TIANYUAPK = new ProductID("tianyuapk");
	
	public static final ProductID KUYUE = new ProductID("kuyue");
	
	public static final ProductID QINGCHENG = new ProductID("qingcheng");
	
	public static final ProductID BJTIANYI = new ProductID("bjtianyi");
	
	public static final ProductID GIONEE_CALANDAR_SDK = new ProductID("04b482c0-089");

	public static final ProductID GIONEE_FUYIPING = new ProductID("fuyiping-gionee");
	
	public static final ProductID ZUIMEITIANQI = new ProductID("6c4702b0-181");
	
	public static final ProductID SAMSUNG = new ProductID("samsung");
	
	public static final ProductID QIEZIKUAICHUAN = new ProductID("75ac6eb8-779");
	
	public static final ProductID FIREFOX = new ProductID("fireforx");
	
	public static final ProductID IDO_HOME_SDK = new ProductID("7e93d401-483");
	
	public static final ProductID FLASHLOCK = new ProductID("flashlock");
	
	public static final ProductID WIFIYAOSHI = new ProductID("wifiyaoshi");
	
	public static final ProductID LENOVO_FUYIPING_SDK = new ProductID("lenovo-launcher");
	
	public static final ProductID KUBI_SDK = new ProductID("8214d701-b92");
	
	public static final ProductID HUISUOPING = new ProductID("huisuoping");
	
	public static final ProductID H5_SDK = new ProductID("h5_sdk");


	// hotoday
	// app start with("hotoday_India_English|hotoday_India_Hindi") or app=hotoday
	public static final ProductID HOTODAY = new ProductID("hotoday");	// 500

	public static final ProductID H5IN = new ProductID("h5in");	// 501
	
	public static final ProductID H5CHN = new ProductID("h5chn");	// 502
	
	public static final ProductID QYSDK = new ProductID("qysdk");	// 504
	
	public static final ProductID QYGB = new ProductID("qygb");	// 505
		
	public static final ProductID MEIZU = new ProductID("meizu");	// 506
	
	public static final ProductID ALI_TEST = new ProductID("ali_test");	// 507
	
	public static final ProductID ALI = new ProductID("ali");	// 508
	
	public static final ProductID PJCOM = new ProductID("pjcom");	// 509
	
	public static final ProductID MOXIU_ZHUOMIAN = new ProductID("94559a5-0628");	// 510
	
	public static final ProductID THEME_LOCK = new ProductID("themelock");	// 511
	
	public static final ProductID GIONEE_CALENDAR_H5_LIST = new ProductID("amicallist-h5");	// 512
	
	public static final ProductID LENOVO_H5 = new ProductID("lenovoh5");	// 513
	
	//public static final ProductID GIONEE_FUYIPING_H5 = new ProductID("fyp-ginee-h5");	// 514
	
	public static final ProductID FIREFOX_BROWSER_H5 = new ProductID("firefoxnewh5");	// 515
	
	public static final ProductID KUBI_SDK_H5 = new ProductID("8214d701-b92-h5");	// 516
	
	public static final ProductID MOXIU_SUOPING = new ProductID("1bce954-0711");	// 517
	
	public static final ProductID MATA = new ProductID("mata");	// 518
	
	public static final ProductID NUBIABROWSER = new ProductID("nubiabrowser");	// 519
	
	public static final ProductID YUN_OS_NEWS = new ProductID("yunosnews");	// 520
	
	public static final ProductID ZUIMEITIANQI_NEW = new ProductID("zuimeitianqi");	// 521
	
	public static final ProductID NOTICIAS = new ProductID("noticias");	// 522
	
	public static final ProductID NOTICIAS_BOOM = new ProductID("noticiasboom");	// 523
	
	public static final ProductID MOXIU_SUOPING_NEW = new ProductID("moxiulockscreen");	// 524
	
	public static final ProductID MOXIU_SEARCH = new ProductID("moxiusearch");	// 525
	
	public static final ProductID MOXIU_ZHUOMIAN_NEW = new ProductID("moxiulauncher");	// 526
	
	public static final ProductID NOTICIAS_BOOM_CHILE = new ProductID("noticiasboomchile");	// 
	
	public static final ProductID NOTICIAS_BOOM_COLOMBIA = new ProductID("noticiasboomcolombia");	// 
	
	private String product_name = null;
	
	private ProductID(String product_name) {
		this.product_name = product_name;
	}
	
	@Override
	public String toString() {
		return product_name;
	}
	
	@Override
	public boolean equals(Object obj) {
		boolean ret = false;
		
		if (obj instanceof ProductID) {
			ret = (product_name.contentEquals(((ProductID)obj).product_name));
		} else if (obj instanceof String) {
			ret = (product_name.contentEquals((String)obj));
		}
		
		return ret;
	}
	
}
