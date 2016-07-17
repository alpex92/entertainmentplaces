package com.alpex.entertainmentplaces.web


import akka.actor.ActorSystem
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server.Directives._
import akka.stream.Materializer
import com.alpex.entertainmentplaces.util.{Logging, Configurable}
import com.alpex.entertainmentplaces.web.services.{RatingService, PlacesService, SearchService}
import com.alpex.entertainmentplaces.json.SprayJson
import com.typesafe.config.Config

import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success}

/**
  * Created by alpex on 14/07/16.
  */
class Gateway(val config: Config)
             (implicit val as: ActorSystem,
              implicit val ec: ExecutionContext,
              implicit val mt: Materializer)
  extends Configurable with SprayJson with Logging {

  val apiKey = config.getString("api.key")
  val apiVersion = config.getString("api.version")

  val httpClient = new HttpClient(config)

  val placesApi = new PlacesService(httpClient, apiKey, apiVersion)
  val ratingApi = new RatingService(httpClient, apiKey, apiVersion)
  val searchApi = new SearchService(placesApi, ratingApi)

  val routes = path("search" / Segment) { what =>
    get {
      onComplete(searchApi.search(what)) {
        case Success(places) =>
          complete(places)
        case Failure(t) =>
          logger.warn(s"search failed: ${t.getMessage}")
          complete(InternalServerError -> t.getMessage)
      }
    }
  }
}
