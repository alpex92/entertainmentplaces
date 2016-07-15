package com.alpex.entertainmentplaces.web

import akka.actor.ActorSystem
import akka.http.scaladsl.server.Directives._
import akka.stream.Materializer
import com.alpex.entertainmentplaces.util.{Configurable, JsonSupport}
import com.alpex.entertainmentplaces.web.services.SearchService
import com.typesafe.config.Config

import scala.concurrent.ExecutionContext

/**
  * Created by alpex on 14/07/16.
  */
class Gateway(val config: Config)
             (implicit val as: ActorSystem,
              implicit val ec: ExecutionContext,
              implicit val mt: Materializer)
  extends Configurable with JsonSupport {

  val apiKey = config.getString("api.key")
  val apiVersion = config.getString("api.version")

  val httpClient = new HttpClient(config)
  val searchApi = new SearchService(httpClient, config)

  val routes = path("search" / Segment) { what =>
    get {
      // TODO: Validate 'what'?
      onSuccess(searchApi.search(what)) { places =>
        complete(places)
      }
    }
  }
}
