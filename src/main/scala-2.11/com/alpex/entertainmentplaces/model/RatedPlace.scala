package com.alpex.entertainmentplaces.model

import com.alpex.entertainmentplaces.util.NumUtils

/**
  * Created by alpex on 14/07/16.
  */

case class Place(id: String, name: String, address: String, hash: String)
case class RatedPlace(title: String, address: String, rating: String) {
  @transient
  val ratingNum = NumUtils.parseFloat(rating)
}


