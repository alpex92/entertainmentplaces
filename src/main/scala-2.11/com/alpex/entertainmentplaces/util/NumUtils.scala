package com.alpex.entertainmentplaces.util

/**
  * Created by alpex on 14/07/16.
  */
object NumUtils {

  def parseFloat(floatStr: String): Float = {
    try {
      floatStr.toFloat
    } catch {
      case _: NumberFormatException =>
        0
    }
  }
}
