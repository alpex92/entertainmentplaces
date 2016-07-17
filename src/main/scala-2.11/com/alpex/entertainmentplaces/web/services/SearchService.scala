package com.alpex.entertainmentplaces.web.services

import akka.stream.Materializer
import com.alpex.entertainmentplaces.api.SearchAPI
import com.alpex.entertainmentplaces.model.{Place, RatedPlace}
import com.alpex.entertainmentplaces.util.{Configurable, Logging}
import com.alpex.entertainmentplaces.web.{ApiUsage, HttpClient}
import com.typesafe.config.Config

import scala.concurrent.{ExecutionContext, Future}

/**
  * Created by alpex on 14/07/16.
  */

class SearchService(val client: HttpClient, val config: Config)
                   (implicit val executionContext: ExecutionContext, implicit val materializer: Materializer)
  extends SearchAPI with ApiUsage with Configurable with Logging {

  val cities = List("Новосибирск", "Омск", "Томск", "Кемерово", "Новокузнецк")

  val ratedPlaceOrdering = Ordering.by((_: RatedPlace).rating)

  val apiKey = config.getString("api.key")
  val apiVersion = config.getString("api.version")

  val placesApi = new PlacesService(client, apiKey, apiVersion)
  val ratingApi = new RatingService(client, apiKey, apiVersion)

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
