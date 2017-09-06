package com.inveno.bigdata.common

object TestTimeUtil {
        
    def main(args: Array[String]) {
				println(TimeUtil.getCurrDate())
				println(TimeUtil.getCurrDate("yyyyMMddHH"))
				println(TimeUtil.getValidDate())
				println(TimeUtil.getValidDate("yyyyMMddHH"))
				println(TimeUtil.getValidDate("yyyyMMddHH", 48))
    }
}