package com.alpex.entertainmentplaces.web

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpResponse, HttpRequest}
import akka.stream.Materializer
import akka.stream.scaladsl.Flow
import com.alpex.entertainmentplaces.util.Configurable
import com.alpex.entertainmentplaces.web.HttpTransport.HttpFlow
import com.typesafe.config.Config

/**
  * Created by alpex on 14/07/16.
  */
object HttpTransport {
  type HttpFlow = Flow[HttpRequest, HttpResponse, Any]
}

class HttpClient(val config: Config)(implicit val system: ActorSystem) extends Configurable {
  lazy val httpFlow: HttpFlow = Http().outgoingConnection(config.getString("api.host"))
}

class HttpServer(val handler: HttpFlow, val config: Config)
                (implicit val actorSystem: ActorSystem, implicit val materializer: Materializer) {
  def start = Http().bindAndHandle(handler, config.getString("http.interface"), config.getInt("http.port"))
}
