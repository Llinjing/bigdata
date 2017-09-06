use db_mta;
select product,config_id,top_percentage,language,min_impression,content_type,scenario_channel from t_config_high_impression where status=0 and feedback_type=2;
