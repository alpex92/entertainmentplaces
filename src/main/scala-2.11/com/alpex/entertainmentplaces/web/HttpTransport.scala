package com.alpex.entertainmentplaces.web

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpResponse, HttpRequest}
import akka.stream.Materializer
import akka.stream.scaladsl.{Sink, Source, Flow}
import com.alpex.entertainmentplaces.util.{Logging, Configurable}
import com.alpex.entertainmentplaces.web.HttpTransport.HttpFlow
import com.typesafe.config.Config

import scala.concurrent.Future

/**
  * Created by alpex on 14/07/16.
  */
object HttpTransport {
  type HttpFlow = Flow[HttpRequest, HttpResponse, Any]
}

class HttpClient(val config: Config)
                (implicit val system: ActorSystem, implicit val materializer: Materializer) extends Configurable {
  lazy val flow = Http().outgoingConnection(config.getString("api.host"))
  def startRequest(request: HttpRequest): Future[HttpResponse] = Source.single(request).via(flow).runWith(Sink.head)
}

class HttpServer(val handler: HttpFlow, val config: Config)
                (implicit val actorSystem: ActorSystem, implicit val materializer: Materializer) extends Logging {
  def start = {
    val interface = config.getString("http.interface")
    val port = config.getInt("http.port")
    logger.info(s"Starting web server on $interface: $port")
    Http().bindAndHandle(handler, interface, port)
  }
}
