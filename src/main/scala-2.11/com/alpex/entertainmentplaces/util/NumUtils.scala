package com.alpex.entertainmentplaces.util

/**
  * Created by alpex on 14/07/16.
  */
object NumUtils {

  def parseFloat(floatStr: String): Option[Float] = {
    try {
      Some(floatStr.toFloat)
    } catch {
      case _: NumberFormatException =>
        None
    }
  }
}
