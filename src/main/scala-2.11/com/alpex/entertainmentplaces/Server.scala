package com.alpex.entertainmentplaces

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import com.alpex.entertainmentplaces.web.{Gateway, HttpServer}
import com.typesafe.config.ConfigFactory

/**
  * Created by alpex on 14/07/16.
  */
object Server extends App {

  val config = ConfigFactory.load()

  implicit val system = ActorSystem("EP2Gis")
  implicit val executor = system.dispatcher
  implicit val materializer = ActorMaterializer()

  val handler = new Gateway(config)
  val httpServer = new HttpServer(handler.routes, config)

  httpServer.start
}
