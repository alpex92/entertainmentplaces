package com.alpex.entertainmentplaces.web.services

import akka.http.scaladsl.model.{HttpEntity, HttpRequest, HttpResponse}
import akka.http.scaladsl.unmarshalling.{Unmarshal, Unmarshaller}
import akka.stream.Materializer
import com.alpex.entertainmentplaces.web.HttpClient

import scala.concurrent.duration._
import scala.concurrent.{ExecutionContext, Future}

/**
  * Created by alpex on 14/07/16.
  */

trait HttpClientService {

  implicit val materializer: Materializer
  implicit val executionContext: ExecutionContext

  def client: HttpClient

  def startRequest(request: HttpRequest): Future[HttpResponse] = client.startRequest(request)

  /**
    * Using toStrict to avoid "Substream Source cannot be materialized more than once"
    * exception when fallback to Left in Either
    */
  def processResponse[T](response: HttpResponse)(implicit um: Unmarshaller[HttpEntity.Strict, T]) = {
    response.entity.toStrict(collectTimeOut).flatMap(Unmarshal(_).to[T])
  }

  def collectTimeOut = 5.second
}
