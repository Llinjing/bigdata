USE dashboard;
set names utf8;select source,product_id,language,sum(article_available_amount) from t_article_publish where timestamp_day='2017-03-14' and content_type='all' and body_images_count='all' group by source,product_id,language;
