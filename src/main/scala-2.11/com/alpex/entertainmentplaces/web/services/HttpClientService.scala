package com.alpex.entertainmentplaces.web.services

import akka.http.scaladsl.model.{HttpRequest, HttpResponse}
import akka.stream.Materializer
import akka.stream.scaladsl.{Sink, Source}
import com.alpex.entertainmentplaces.web.HttpTransport.HttpFlow

import scala.concurrent.{ExecutionContext, Future}

/**
  * Created by alpex on 14/07/16.
  */

trait HttpClientService {
  implicit val materializer: Materializer
  implicit val executionContext: ExecutionContext
  def flow: HttpFlow
  def startRequest(request: HttpRequest): Future[HttpResponse] = Source.single(request).via(flow).runWith(Sink.head)
}
