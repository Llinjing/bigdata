package com.inveno.news.reformat.constant;

public class Promotion {
	
	public static final Promotion UNKNOWN = new Promotion("unknown");
	
	// to B
	public static final Promotion INVENO = new Promotion("inveno");
	
	public static final Promotion COOLPAD = new Promotion("coolpad");
	
	public static final Promotion QIKU = new Promotion("qiku");
	
	public static final Promotion HUAWEI = new Promotion("emui");
	
	public static final Promotion LENOVO = new Promotion("lenovo");
	
	public static final Promotion BAIDU = new Promotion("baidu");
	
	public static final Promotion HUAQIN = new Promotion("huaqin");
	
	public static final Promotion HAOCHENG = new Promotion("haocheng");
	
	public static final Promotion FOXCONN = new Promotion("foxconn");
	
	public static final Promotion MONEYLOCKER = new Promotion("moneylocker");
	
	public static final Promotion TCL = new Promotion("tcl");
	
	public static final Promotion DUOWEI = new Promotion("duowei");
	
	public static final Promotion ANGDA = new Promotion("angda");
	
	public static final Promotion XIAOLAJIAO = new Promotion("xiaolajiao");
	
	public static final Promotion TIANYU = new Promotion("tianyu");
	
	public static final Promotion BORUI = new Promotion("borui");
	
	public static final Promotion QINGCHENG = new Promotion("qingcheng");
	
	public static final Promotion TIANYI = new Promotion("tianyi");
	
	public static final Promotion GIONEE = new Promotion("gionee");
	
	public static final Promotion ZUIMEI_TIANQI = new Promotion("zuimeitianqi");
	
	public static final Promotion SUMSUNG = new Promotion("sumsung");
	
	public static final Promotion QIEZI_KUAICHUAN = new Promotion("qiezikuaichuan");
	
	public static final Promotion FIREFOX = new Promotion("firefox");
	
	public static final Promotion DUOTUO = new Promotion("duotuo");
	
	public static final Promotion WIFIYAOSHI = new Promotion("wifiyaoshi");
	
	public static final Promotion KUBI = new Promotion("kubi");
	
	public static final Promotion MICROMAX = new Promotion("micromax");
	
	public static final Promotion PANASONIC = new Promotion("panasonic");
	
	public static final Promotion KARBOON = new Promotion("karboon");
	
	public static final Promotion ZHONGXING = new Promotion("zte");
	
	public static final Promotion INTEX = new Promotion("intex");
	
	public static final Promotion GULI = new Promotion("guli");
	
	public static final Promotion ZHIZHEN = new Promotion("zhizhen");
	
	public static final Promotion KEFEI = new Promotion("kefei");
	
	public static final Promotion WATERWORLD = new Promotion("waterworld");
	
	public static final Promotion KUNLUN = new Promotion("kunlun");
	
	public static final Promotion CHANGYOU = new Promotion("changyou");
	
	public static final Promotion UC = new Promotion("uc");
	
	public static final Promotion LAVA = new Promotion("lava");
	
	public static final Promotion MEIZU = new Promotion("meizu");
	
	public static final Promotion ALI = new Promotion("ali");
	
	
	// to C
	public static final Promotion GOOGLE_PLAY = new Promotion("gp");
	
	public static final Promotion AMAZON = new Promotion("amazon");
	
	public static final Promotion BAIDU_MOBILE = new Promotion("baidu_m");
	
	public static final Promotion UC_9APPS = new Promotion("uc_9apps");
	
	public static final Promotion USER_UPGRADE = new Promotion("common");
	
	public static final Promotion WANGMENG_APK = new Promotion("wangmeng");
	
	public static final Promotion HELP_360 = new Promotion("360help");
	
	public static final Promotion BAIDU_SHOUJI = new Promotion("baidu_91_shouji_zhushou");
	
	public static final Promotion XIAOMI_APP_STORE = new Promotion("xiaomi");
	
	public static final Promotion YINGYONGBAO = new Promotion("tencent");
	
	public static final Promotion UC_APP_STORE = new Promotion("uc_pp");
	
	public static final Promotion SOGOU_SHOUJI_ZHUSHOU = new Promotion("sogou");
	
	public static final Promotion WANDOUJIA = new Promotion("wandoujia");
	
	public static final Promotion OPPO_APP_STORE = new Promotion("oppo");
	
	public static final Promotion ANZHI_MARKET = new Promotion("anzhi");
	
	public static final Promotion JIFENG_MARKET = new Promotion("jifeng");
	
	public static final Promotion LESHI_APP_STORE = new Promotion("leshi");
	
	public static final Promotion VIVO_MARKET = new Promotion("vivo");
	
	public static final Promotion YINGYONGHUI = new Promotion("yingyonghui");
	
	public static final Promotion YOUYI_MARKET = new Promotion("youyi");
	
	public static final Promotion ANRUAN_MARKET = new Promotion("anruan");
	
	public static final Promotion ANZHUO_MARKET_3G = new Promotion("3g");
	
	public static final Promotion MUMAYI_MARKET = new Promotion("mumayi");
	
	public static final Promotion TIANYI_OPEN_PLATFORM = new Promotion("tianyi");
	
	public static final Promotion YIDONG_MM = new Promotion("yidong_mm");
	
	public static final Promotion LIANTONG_WO = new Promotion("wo");
	
	public static final Promotion ANFEN = new Promotion("anfen");
	
	public static final Promotion NDUO = new Promotion("n_duo");
	
	private String promotion = null;
	
	private Promotion(String promotion) {
		this.promotion = promotion;
	}
	
	@Override
	public String toString() {
		return promotion;
	}
	
	@Override
	public boolean equals(Object obj) {
		boolean ret = false;
		
		if (obj instanceof ProductID) {
			ret = (promotion.contentEquals(((Promotion)obj).promotion));
		} else if (obj instanceof String) {
			ret = (promotion.contentEquals((String)obj));
		}
		
		return ret;
	}

}
