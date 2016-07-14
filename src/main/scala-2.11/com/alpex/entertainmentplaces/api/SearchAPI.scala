package com.alpex.entertainmentplaces.api

import com.alpex.entertainmentplaces.model.{Place, RatedPlace}

import scala.concurrent.Future

/**
  * Created by alpex on 14/07/16.
  */
trait SearchAPI {
  def search(what: String): Future[Seq[RatedPlace]]
}

trait PlacesAPI {
  def getPlaces(what: String, where: String): Future[Seq[Place]]
}

trait RatingAPI {
  def getRating(p: Place): Future[RatedPlace]
}
