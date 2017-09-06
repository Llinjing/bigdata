package com.inveno.bigdata.emotion.gmp


object TestArticleGmpConfig {
  
    private val config = ArticleEmotionGmpConfig.getInstance();
    
    def main(args: Array[String]) {
        println(config)
        val conf = config.getConfig
        println(conf.getString("hdfs_debug"));
        println(conf.getString("config.type"))
				println(conf.getInt("valid_hour"))
				println(conf.getString("date_format"))
    }
}
