package com.alpex.entertainmentplaces.web.services

import akka.http.scaladsl.client.RequestBuilding
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.model.Uri
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.stream.Materializer
import com.alpex.entertainmentplaces.api.PlacesAPI
import com.alpex.entertainmentplaces.model.{Place, PlacesResponse}
import com.alpex.entertainmentplaces.util.{Logging, JsonSupport}
import com.alpex.entertainmentplaces.web.{HttpClient, ApiUsage}

import scala.concurrent.{ExecutionContext, Future}

/**
  * Created by alpex on 14/07/16.
  */
class PlacesService(val client: HttpClient, val apiKey: String, val apiVersion: String)
                   (implicit val executionContext: ExecutionContext, implicit val materializer: Materializer)
  extends HttpClientService with ApiUsage with PlacesAPI with JsonSupport with Logging {

  override def getPlaces(what: String, where: String): Future[Seq[Place]] = {

    val params = query("what" -> what, "where" -> where, "sort" -> "rating")
    val uri = Uri("/search").withQuery(params)
    val request = RequestBuilding.Get(uri)

    logger.debug(s"Getting $what in $where ...")

    startRequest(request).flatMap(response => response.status match {
      case OK =>
        Unmarshal(response.entity).to[PlacesResponse].map(placesResponse => {
          logger.debug(s"Got ${placesResponse.result.size} $what in $where")
          placesResponse.result
        })
      case _ =>
        Future.failed(new Exception("Ololo failed"))
    })
  }

}
