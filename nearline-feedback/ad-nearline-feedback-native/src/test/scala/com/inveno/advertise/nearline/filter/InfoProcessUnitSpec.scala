package com.inveno.advertise.nearline.filter

import org.specs2.mutable.Specification


class InfoProcessUnitSpec extends Specification {

  val True = true
  val False = false

  "InfoProcess" should {

    "checkLogType should return the right result" in {
      val malacca = "malacca-ad"
      val gate = "gate-ad"

      val malaccaCheck = processInfo.checkLogType(malacca)
      val gateCheck = processInfo.checkLogType(gate)

      malaccaCheck mustEqual True
      gateCheck mustEqual False

    }

    "checkPayModel should return the right result" in {
      val cpc = "cpc"
      val cpm = "cpm"

      val cpcCheck = processInfo.checkPayModel(cpc)
      val cpmCheck = processInfo.checkPayModel(cpm)

      cpcCheck mustEqual True
      cpmCheck mustEqual False

    }

    "getFlag should return the right result" in {

      val value1 = 10
      val value2 = 0

      val cpcCheck = processInfo.getFlag(value1)
      val cpmCheck = processInfo.getFlag(value2)

      cpcCheck mustEqual "true"
      cpmCheck mustEqual "false"

    }

    "getBudgetCost should return the right result" in {

      val request = 100000
      val impression = 100000
      val click = 200

      val price = "1.5"

      val cpcModel = "cpc"
      val cpmModel = "cpm"

      val malacca = "malacca-ad"
      val gate = "gate-ad"

      val cpcCost = processInfo.getBudgetCost(cpcModel, price, malacca, request, impression, click)
      val cpmCost = processInfo.getBudgetCost(cpmModel, price, gate, request, impression, click)
      val cpmCost1 = processInfo.getBudgetCost(cpmModel, price, malacca, request, impression, click)
      val cpmCost2 = processInfo.getBudgetCost(cpmModel, price, gate, request, impression, click)

      cpcCost mustEqual "300.0000"
      cpmCost mustEqual "150.0000"
      cpmCost1 mustEqual "150.0000"
      cpmCost2 mustEqual "150.0000"

    }

    "getCompleteRate should return the right result" in {

      val request = 23700
      val impression = 179000
      val click = 291

      val daily_limit_count = "200000"

      val cpcModel = "cpc"
      val cpmModel = "cpm"

      val malacca = "malacca-ad"
      val gate = "gate-ad"

      val cpcRate = processInfo.getCompleteRate(cpcModel, daily_limit_count, malacca, request, impression, click)
      val cpmRate = processInfo.getCompleteRate(cpmModel, daily_limit_count, malacca, request, impression, click)
      val cpmGate = processInfo.getCompleteRate(cpmModel, daily_limit_count, gate, request, impression, click)

      cpcRate mustEqual "0.0014550000"
      cpmRate mustEqual "0.8950000000"
      cpmGate mustEqual "0.1185000000"

    }

  }

}


object processInfo extends InfoProcess

