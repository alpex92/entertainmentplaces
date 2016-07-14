package com.alpex.entertainmentplaces.model

/**
  * Created by alpex on 14/07/16.
  */

case class Place(id: String, name: String, address: String, hash: String)
case class Rating(placeId: String, rating: Float)
case class RatedPlace(title: String, address: String, rating: Float)


