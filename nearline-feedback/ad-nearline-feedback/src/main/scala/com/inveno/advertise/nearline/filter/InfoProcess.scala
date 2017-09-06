package com.inveno.advertise.nearline.filter

trait InfoProcess {

  def checkPayModel(model: String): Boolean = {
    model.toLowerCase == "cpc"
  }

  def checkLogType(log_type: String): Boolean = {
    log_type.toLowerCase == "malacca-ad"
  }

  def getCompleteRate(model: String, daily_count: String, log_type: String, request: Long, impression: Long, click: Long): String = {
    if (checkPayModel(model)) {
      val rate = if (daily_count.toLong <= 0) 0.0 else click.toDouble / daily_count.toLong
      f"$rate%1.10f"
    } else if (checkLogType(log_type)) {
      val rate = if (daily_count.toLong <= 0) 0.0 else impression.toDouble / daily_count.toLong
      f"$rate%1.10f"
    } else {
      val rate = if (daily_count.toLong <= 0) 0.0 else request.toDouble / daily_count.toLong
      f"$rate%1.10f"
    }
  }

  def getBudgetCost(model: String, price: String, log_type: String, request: Long, impression: Long, click: Long): String = {
    if (checkPayModel(model)) {
      val cast = price.toFloat * click
      f"$cast%1.4f"
    } else if (checkLogType(log_type)) {
      val cast = price.toFloat * impression / 1000
      f"$cast%1.4f"
    } else {
      val cast = price.toFloat * request / 1000
      f"$cast%1.4f"
    }
  }

  def getFlag(value: Long): String = {
    if (value > 0) "true" else "false"
  }

}
