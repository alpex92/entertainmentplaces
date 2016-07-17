package com.alpex.entertainmentplaces.web.services

import akka.stream.Materializer
import com.alpex.entertainmentplaces.api.SearchAPI
import com.alpex.entertainmentplaces.model.{Place, RatedPlace}
import com.alpex.entertainmentplaces.util.{Logging, Configurable}
import com.alpex.entertainmentplaces.web.{ApiUsage, HttpClient}
import com.typesafe.config.Config

import scala.concurrent.{ExecutionContext, Future}
import scala.util.Success

/**
  * Created by alpex on 14/07/16.
  */

class SearchService(val client: HttpClient, val config: Config)
                   (implicit val executionContext: ExecutionContext, implicit val materializer: Materializer)
  extends SearchAPI with ApiUsage with Configurable with Logging {

  val cities = List("Новосибирск", "Омск", "Томск", "Кемерово", "Новокузнецк")

  val apiKey = config.getString("api.key")
  val apiVersion = config.getString("api.version")

  val placesApi = new PlacesService(client, apiKey, apiVersion)
  val ratingApi = new RatingService(client, apiKey, apiVersion)

  override def search(what: String): Future[Seq[RatedPlace]] = {
    sortByRating(getRatedPlaces(what))
  }

  protected def getRatedPlaces(what: String) = {
    Future.sequence(cities.map(where => maxByRating(getRatedPlacesForCity(what, where))))
  }

  protected def getRatedPlacesForCity(what: String, city: String) = {
    val places = placesApi.getPlaces(what, city)
    places.flatMap(getRatingForPlaces)
  }

  protected def getRatingForPlaces(places: Seq[Place]) = {
    // Flatten to discard places without rating
    Future.sequence(places.map(ratingApi.getRating)).map(_.flatten)
  }

  protected def sortByRating(supplier: => Future[Seq[RatedPlace]]) = {
    supplier.map(_.sortBy(-_.ratingNum))
  }

  protected def maxByRating(supplier: => Future[Seq[RatedPlace]]) = {
    supplier.map(_.maxBy(_.rating))
  }

}
