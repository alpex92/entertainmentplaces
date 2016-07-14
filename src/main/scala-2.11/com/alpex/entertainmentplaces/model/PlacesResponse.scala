package com.alpex.entertainmentplaces.model

import com.alpex.entertainmentplaces.util.NumUtils

/**
  * Created by alpex on 14/07/16.
  */
case class PlacesResponse(result: List[Place])

// WTF: the rating field is returned as String in response, despite it's documented as number

// TODO: Make optional?
case class RatingResponse(cityName: String, rating: String) {
  def getRatingNum = NumUtils.parseFloat(rating)
}
