use dashboard
create table t_daily_ad_material
(
    id int(32) unsigned not null auto_increment,
    timestamp datetime,
    ad_id varchar(50),
    advertiser_name varchar(200),
    scenario varchar(64),
    ad_source varchar(64),
    channel_id varchar(100),
    product_id varchar(100),
    app_ver varchar(32),
    position_id varchar(64)
    position_type varchar(100),
    position_size varchar(100),
    industry varchar(100) comment '投放行业',
    ad_type varchar(20),    
    news_config_id varchar(32),
    biz_config_id varchar(32),
    ad_config_id varchar(32),
    unit_price varchar(16),
    pay_model varchar(16),
    impression int(32),
    click int(32),
    income float,   
    primary key(id)
) ENGINE=MyISAM AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;

