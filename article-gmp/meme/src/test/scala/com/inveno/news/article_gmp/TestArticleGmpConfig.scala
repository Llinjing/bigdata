package com.inveno.news.article_gmp

import com.inveno.bigdata.meme.gmp.ArticleMemeGmpConfig


object TestArticleGmpConfig {
  
    private val config = ArticleMemeGmpConfig.getInstance();
    
    def main(args: Array[String]) {
        println(config)
        val conf = config.getConfig
        println(conf.getString("config.type"))
				println(conf.getInt("valid_hour"))
				println(conf.getString("date_format"))
				println(conf.getString("redis.hash.key.prefix"))
				println(conf.getInt("redis.hash.key.partitions"))
    }
}