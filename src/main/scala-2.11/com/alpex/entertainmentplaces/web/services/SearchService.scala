package com.alpex.entertainmentplaces.web.services

import akka.stream.Materializer
import com.alpex.entertainmentplaces.api.{PlacesAPI, RatingAPI, SearchAPI}
import com.alpex.entertainmentplaces.model.{Place, RatedPlace}
import com.alpex.entertainmentplaces.util.Logging

import scala.concurrent.{ExecutionContext, Future}

/**
  * Created by alpex on 14/07/16.
  */

class SearchService(placesApi: PlacesAPI, ratingApi: RatingAPI)
                   (implicit val executionContext: ExecutionContext,
                    implicit val materializer: Materializer)
  extends SearchAPI with Logging {

  val cities = List("Новосибирск", "Омск", "Томск", "Кемерово", "Новокузнецк")

  val ratedPlaceOrdering = Ordering.by((_: RatedPlace).rating)

  override def search(what: String): Future[Seq[RatedPlace]] = {
    getRatedPlaces(what).map(sortByRating)
  }

  protected def getRatedPlaces(what: String) = {
    val cityFutures = cities.map(where => getRatedPlacesForCity(what, where)
      .map(discardZeroRating)
      .map(maxByRating))
    Future.sequence(cityFutures).map(_.flatten)
  }

  protected def getRatedPlacesForCity(what: String, city: String) = {
    placesApi.getPlaces(what, city).flatMap(getRatingForPlaces)
  }

  protected def getRatingForPlaces(places: Seq[Place]) = {
    Future.sequence(places.map(ratingApi.getRating))
  }

  protected def discardZeroRating(places: Seq[Option[RatedPlace]]) = {
    places.flatten.filter(_.ratingNum > 0)
  }

  protected def sortByRating(places: Seq[RatedPlace]) = {
    places.sorted(ratedPlaceOrdering.reverse)
  }

  protected def maxByRating(places: Seq[RatedPlace]) = {
    places.reduceOption(ratedPlaceOrdering.max)
  }

}
