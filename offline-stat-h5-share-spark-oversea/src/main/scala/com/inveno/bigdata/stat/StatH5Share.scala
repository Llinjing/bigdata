package com.inveno.bigdata.stat

import org.slf4j.LoggerFactory
import org.apache.spark.SparkContext
import org.apache.spark.SparkConf

object StatH5Share {
  
    private val CLASS_NAME = StatH5Share.getClass.getSimpleName 
    private val logger = LoggerFactory.getLogger(CLASS_NAME)
    
    def main(args: Array[String]): Unit = {
        val conf = new SparkConf()
        val sc = new SparkContext(conf)
        val h5ShareRdd = H5Share.h5Share(args.apply(0), sc);
        //(hotoday###Hindi###1038987530###01011701240027114801000025707302###0x0409ff,1)
        h5ShareRdd.saveAsTextFile(args.apply(1)) 
    }  
  
}