package com.alpex.entertainmentplaces.web.services

import akka.http.scaladsl.model.{HttpResponse, HttpRequest}
import akka.stream.Materializer
import akka.stream.scaladsl.{Sink, Source}
import com.alpex.entertainmentplaces.web.HttpClient

import scala.concurrent.{Future, ExecutionContext}

/**
  * Created by alpex on 14/07/16.
  */

trait HttpClientService {
  implicit val materializer: Materializer
  implicit val executionContext: ExecutionContext
  def client: HttpClient
  def startRequest(request: HttpRequest): Future[HttpResponse] = client.startRequest(request)
}
