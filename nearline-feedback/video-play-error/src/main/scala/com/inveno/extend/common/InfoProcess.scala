package com.inveno.extend.common

/**
 * Created by dory on 2017/2/10.
 */
trait InfoProcess {
  
  def getRate(numerator: Long, denominator: Long): String = {
    val rate = if (denominator <= 0) 0.0 else numerator.toDouble / denominator
    f"$rate%1.4f"
  }
}
