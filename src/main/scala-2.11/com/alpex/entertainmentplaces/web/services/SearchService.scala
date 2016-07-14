package com.alpex.entertainmentplaces.web.services

import akka.stream.Materializer
import com.alpex.entertainmentplaces.api.SearchAPI
import com.alpex.entertainmentplaces.model.{Place, RatedPlace}
import com.alpex.entertainmentplaces.util.Configurable
import com.alpex.entertainmentplaces.web.ApiUsage
import com.alpex.entertainmentplaces.web.HttpTransport.HttpFlow
import com.typesafe.config.Config

import scala.concurrent.{ExecutionContext, Future}

/**
  * Created by alpex on 14/07/16.
  */

class SearchService(val flow: HttpFlow, val config: Config)
                   (implicit val executionContext: ExecutionContext, implicit val materializer: Materializer)
  extends SearchAPI with ApiUsage with Configurable {

  val cities = List("Новосибирск", "Омск", "Томск", "Кемерово", "Новокузнецк")

  val apiKey = config.getString("api.key")
  val apiVersion = config.getString("api.version")

  val placesApi = new PlacesService(flow, apiKey, apiVersion)
  val ratingApi = new RatingService(flow, apiKey, apiVersion)

  override def search(what: String): Future[Seq[RatedPlace]] = {
    // Get places with max rating for each city
    val maxed = Future.sequence(cities.map(where => getPlaceWithMaxRatingForCity(what, where)))
    // Sort by rating descending
    maxed.map(_.sortBy(_.rating))
  }

  protected def getPlaceWithMaxRatingForCity(what: String, city: String) = {
    getRatedPlacesForCity(what, city).map(_.maxBy(_.rating))
  }

  protected def getRatedPlacesForCity(what: String, city: String) = {
    placesApi.getPlaces(what, city).flatMap(getRatingForPlaces)
  }

  protected def getRatingForPlaces(places: Seq[Place]) = Future.sequence(places.map(ratingApi.getRating))

}
