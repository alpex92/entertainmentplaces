package com.alpex.entertainmentplaces.util

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import com.alpex.entertainmentplaces.model.{RatedPlace, Place, RatingResponse, PlacesResponse}
import spray.json.DefaultJsonProtocol

/**
  * Created by alpex on 14/07/16.
  */
trait JsonSupport extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val placeFormat = jsonFormat4(Place)
  implicit val placesResponseFormat = jsonFormat1(PlacesResponse)
  implicit val ratingResponseFormat = jsonFormat1(RatingResponse)
  implicit val ratedPlaceFormat = jsonFormat3(RatedPlace)
}
