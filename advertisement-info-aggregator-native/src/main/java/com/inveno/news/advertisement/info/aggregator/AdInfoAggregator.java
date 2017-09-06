package com.inveno.news.advertisement.info.aggregator;

import com.inveno.news.advertisement.info.aggregator.constant.*;
import com.inveno.news.advertisement.info.aggregator.schema.*;
import com.inveno.news.advertisement.info.aggregator.util.MySqlDeal;
import com.inveno.news.advertisement.info.aggregator.util.RedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by dory on 2016/10/10.
 */
public class AdInfoAggregator {
    public static final String CLASS_NAME = AdInfoAggregator.class.getSimpleName();

    public static final Logger logger = LoggerFactory.getLogger(CLASS_NAME);
    public Map<String,String> deliveryMap = new HashMap<String,String>();

    public boolean init(String mysql_conf_file, String redis_conf_file){
        int ret_sql = MySqlDeal.init(mysql_conf_file);
        if( ret_sql < 0){
            logger.info("init {} fail, the ret_sql is {}", mysql_conf_file, ret_sql);
            return false;
        }
        
        if(!RedisUtil.init(redis_conf_file)){
            logger.info("init {} fail ", redis_conf_file);
            return false;
        }
        return true;
    }

    public void uninit(){
        RedisUtil.uninit();
        MySqlDeal.close();
    }
    
    public String getNeedTime(int imin){
        String need_time = null;
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        need_time = df.format(System.currentTimeMillis() - 1000 * 60 * imin);
        logger.info("the need time is {} ", need_time);
        return need_time;
    }

    public String dealTimeStr(String str_time){
        String str_ret;
        if(str_time.length() <= 19) {     //"2016-10-10 19:20:20" 共19位
            str_ret = str_time;
        }
        str_ret = str_time.split("\\.")[0];
        return  str_ret;
    }
    public List<String> getAdId(int imin) throws SQLException {
        List<String> arr_ad_id= new ArrayList<String>();
        String need_time = getNeedTime(imin);
        String sql = "select ad_id from t_dsp_ad where update_time >='" + need_time + "'";
        //String sql = "select ad_id from t_dsp_ad";
        //String sql = "select ad_id from t_dsp_ad limit 10";
        logger.info("select advertiment ids sql is " + sql);
        
        ResultSet rs = MySqlDeal.querySql(sql);
        while (rs.next()){
            String ad_id = rs.getString("ad_id");
            arr_ad_id.add(ad_id);
        }

        return arr_ad_id;
    }

    public void dealJson(int imin) throws SQLException {
        List<String> arr_ad_id = getAdId(imin);
        String ad_id = null;

        Ad ad_schema = new Ad();
        Marketing market_schema = new Marketing();
        OperationStrategy oper_stgy = new OperationStrategy();
        Delivery delivery = new Delivery();

        for(int j=0; j < arr_ad_id.size(); j++) {
            ad_id = arr_ad_id.get(j);
            
            //广告
            ad_schema.setAd_id(ad_id);
            ad_schema.setAd_source("");
            ad_schema.setAdvertiser_id("");
            String advertiser_name_sql = "select " +
                    "                        c.comp_name as advertiser_name " +
                    "                    from t_dsp_ad a inner join t_base_plat_user b " +
                    "                    on a.user_id = b.user_id " +
                    "                    inner join t_dsp_open_user c on b.plat_user_id = c.user_id " +
                    "                    where b.plat_id='DSP' and ad_id = '" + ad_id + "'";
            logger.info("advertisement advertiser_name_sql is " + advertiser_name_sql);
            ResultSet rs_advertiser_name = MySqlDeal.querySql(advertiser_name_sql);
            while (rs_advertiser_name.next()){
                ad_schema.setAdvertiser_name(rs_advertiser_name.getString("advertiser_name"));
            }
            String ad_sql = "select ad_type," +
                    "case when business_type is null then '' else business_type end business_type " +
                    "from t_dsp_ad where ad_id ='" + ad_id +"'";
            logger.info("advertisement ad_sql is " + ad_sql);
            ResultSet ad_rs = MySqlDeal.querySql(ad_sql);
            while(ad_rs.next()){
                String type = AdType.getAdType(ad_rs.getString("ad_type"));
                ad_schema.setType(type);
                String industry = ad_rs.getString("business_type");
                ad_schema.setIndustry(industry);
            }
            //logger.info(ad_schema.toString());

            //推广
            market_schema.setMarketing_id(ad_id);
            String daily_cnt_limit_sql="select" +
                    "    case when pay_model=1 and a.ad_price>0 then round(b.threshold/a.ad_price) " +
                    "        when pay_model=2 and a.ad_price >0 then round(b.threshold/a.ad_price*1000) " +
                    "        else ''" +
                    "    end daily_count_limit " +
                    " from t_dsp_ad a inner join t_dsp_extention b on a.plan_id=b.plan_id" +
                    " where a.ad_id = '" + ad_id + "'";
            logger.info("market daily_cnt_limit_sql is " + daily_cnt_limit_sql);
            ResultSet rs_daily_cnt_limit = MySqlDeal.querySql(daily_cnt_limit_sql);
            while (rs_daily_cnt_limit.next()){
                market_schema.setDaily_count_limit(rs_daily_cnt_limit.getString("daily_count_limit"));
            }
            String target_more_sql = "select " +
                    "b.pay_model," +
                    "b.ad_pose_type position_type," +
                    "b.ad_price unit_price " +
                    "from t_dsp_base_orientation a inner join t_dsp_ad  b on a.ad_id=b.ad_id" +
                    " where a.ad_id = '" + ad_id + "'";
            logger.info("market target_more_sql is " + target_more_sql);
            ResultSet rs_target_more = MySqlDeal.querySql(target_more_sql);
            while (rs_target_more.next()){
                String position_type = PositionType.getPositionType(rs_target_more.getString("position_type"));
                market_schema.setPosition_type(position_type);
                String pay_model = PayModel.getPayModel(rs_target_more.getString("pay_model"));
                market_schema.setPay_model(pay_model);
                market_schema.setUnit_price(rs_target_more.getString("unit_price"));
            }

            String str_target_size = "";
            String target_size_sql = "select case when target_size is null then '' else target_size end target_size" +
                    " from (" +
                    "select concat(ad_position_width,'*',ad_position_height) target_size " +
                    "from t_dsp_ad a left join ssp_ad_position_size b on a.ad_pose_size_id=b.id " +
                    "where a.ad_id='" + ad_id + "')a";
            logger.info("market target_size_sql is " + target_size_sql);
            ResultSet rs_target_size = MySqlDeal.querySql(target_size_sql);
            while (rs_target_size.next()){
                str_target_size += (rs_target_size.getString("target_size") + ",");
            }
            String target_size = str_target_size.substring(0,str_target_size.lastIndexOf(","));
            market_schema.setTarget_size(target_size);
            //logger.info(market_schema.toString());

            //策略
            oper_stgy.setOperation_strategy_id(ad_id);
            String stgy_sql = "select weight sort_weight " +
                              "from t_dsp_ad where ad_id='" + ad_id + "'";
            logger.info("strategy stgy_sql is " + stgy_sql);
            ResultSet rs_stgy = MySqlDeal.querySql(stgy_sql);
            while (rs_stgy.next()){
                oper_stgy.setSort_weight(rs_stgy.getString("sort_weight"));
            }
            logger.info(oper_stgy.toString());

            delivery.setDelivery_id(ad_id);
            delivery.setAd(ad_schema);
            delivery.setMarketing(market_schema);
            delivery.setMaterial_id(ad_id);
            delivery.setOperation_strategy(oper_stgy);
            logger.info(delivery.toString());
            deliveryMap.put(ad_id, delivery.toString());
        }
    }
    public void writeRedis(){
        if(deliveryMap.size() <= 0) {
            logger.info("the delivermap is empty");
            return;
        }
        for(String key : deliveryMap.keySet()){
            RedisUtil.update(key, deliveryMap.get(key));
        }
    }
    
    public static void usage(){
        System.out.println(CLASS_NAME + " mysql_conf_file redis_conf_file min_num");
    }
    
    public static void main(String[] args) throws SQLException {
       if(args.length < 3){
           usage();
           return;
       }

       String mysql_conf_file = args[0];
       String redis_conf_file = args[1];
       int imin = Integer.valueOf(args[2]);
       AdInfoAggregator adAgg = new AdInfoAggregator();
       if(!adAgg.init(mysql_conf_file, redis_conf_file)){
           logger.info("init {} fail");
           return;
       }
       adAgg.dealJson(imin);
       adAgg.writeRedis();

       adAgg.uninit();
    }
}
