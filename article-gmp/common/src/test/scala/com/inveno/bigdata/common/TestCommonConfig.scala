package com.inveno.bigdata.common

object TestCommonConfig {
    
    private val config = CommonConfig.getInstance();
    
    def main(args: Array[String]) {
        println(config)
        val conf = config.getConfig
				println(conf.getString("key_tag"))
        println(conf.getString("product_key_tag"))
    }
}