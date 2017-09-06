package com.inveno.bigdata.gmp

object TestArticleGmpConfig {
  
    private val config = ArticleGmpConfig.getInstance();
    
    def main(args: Array[String]) {
        println(config)
        val conf = config.getConfig
        println(conf.getString("topic.article.gmp"));
				println(conf.getInt("impression_threshold_meizu"));
				println(conf.getString("product_id_ali"));
				println(conf.getString("redis.servers"))
				println(conf.getInt("src.kafka.batch.size"))
				println(conf.getInt("src.kafka.batch.size1", 2000))
				println(conf.getBoolean("src.kafka.sync"))
				println(conf.getString("product_id_tcl"));
        println(conf.getString("product_id_duowei"));
        println(conf.getString("product_id_xiaolajiao"));
        println(conf.getString("product_id_union_tcl_dw_xlj"));
    }
}
