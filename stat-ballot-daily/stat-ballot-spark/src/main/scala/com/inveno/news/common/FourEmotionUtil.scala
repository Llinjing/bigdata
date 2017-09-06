package com.inveno.news.common



/**
 * Created by dory on 2016/12/15.
 */
object FourEmotionUtil {
  val KEY_TAG = Constant.KEY_TAG
  def makeCombineKey(uid:String, product_id:String, language:String, emotion:String): List[String] ={
    val All = Constant.ALL
    val Ballot = Constant.BALLOT
    List(s"$uid$KEY_TAG$product_id$KEY_TAG$language$KEY_TAG$emotion",
    s"$uid$KEY_TAG$product_id$KEY_TAG$All$KEY_TAG$emotion",
    s"$uid$KEY_TAG$product_id$KEY_TAG$language$KEY_TAG$Ballot",
    s"$uid$KEY_TAG$product_id$KEY_TAG$All$KEY_TAG$Ballot"
    )
  }

  def makeArticleCombineKey(uid:String, product_id: String, language: String, content_id: String): String ={
    s"$uid$KEY_TAG$product_id$KEY_TAG$language$KEY_TAG$content_id"
  }
}
