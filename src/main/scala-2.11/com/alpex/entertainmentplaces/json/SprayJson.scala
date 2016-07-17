package com.alpex.entertainmentplaces.json

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import com.alpex.entertainmentplaces.model._
import com.scalapenos.spray.SnakifiedSprayJsonSupport

/**
  * Created by alpex on 14/07/16.
  */
trait SprayJson extends SprayJsonSupport with SnakifiedSprayJsonSupport {

  implicit val placeFormat = jsonFormat4(Place)
  implicit val ratedPlaceFormat = jsonFormat3(RatedPlace)

  implicit val failedResponseFormat = jsonFormat3(ErrorResponse)
  implicit val placesResponseFormat = jsonFormat2(PlacesResponse)
  implicit val ratingResponseFormat = jsonFormat3(RatingResponse)
}
