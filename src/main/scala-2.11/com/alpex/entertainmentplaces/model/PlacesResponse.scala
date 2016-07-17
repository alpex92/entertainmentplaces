package com.alpex.entertainmentplaces.model

/**
  * Created by alpex on 14/07/16.
  */


abstract class TwoGisResponse(responseCode: String)

case class ErrorResponse(responseCode: String, errorCode: String, errorMessage: String)
  extends TwoGisResponse(responseCode)

case class PlacesResponse(responseCode: String, result: List[Place])
  extends TwoGisResponse(responseCode)

case class RatingResponse(responseCode: String, cityName: String, rating: Option[String])
  extends TwoGisResponse(responseCode)
