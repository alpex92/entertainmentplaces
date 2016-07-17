package com.alpex.entertainmentplaces.web.services

import akka.http.scaladsl.client.RequestBuilding
import akka.http.scaladsl.model.Uri
import akka.stream.Materializer
import com.alpex.entertainmentplaces.api.PlacesAPI
import com.alpex.entertainmentplaces.model.{FailedResponse, Place, PlacesResponse}
import com.alpex.entertainmentplaces.util.Logging
import com.alpex.entertainmentplaces.web.{ApiUsage, HttpClient}
import com.alpex.entertainmentplaces.json.SprayJson

import scala.concurrent.{ExecutionContext, Future}

/**
  * Created by alpex on 14/07/16.
  */
class PlacesService(val client: HttpClient, val apiKey: String, val apiVersion: String)
                   (implicit val executionContext: ExecutionContext, implicit val materializer: Materializer)
  extends HttpClientService with ApiUsage with PlacesAPI with SprayJson with Logging {

  override def getPlaces(what: String, where: String): Future[Seq[Place]] = {

    val params = query("what" -> what, "where" -> where, "sort" -> "rating")
    val uri = Uri("/search").withQuery(params)
    val request = RequestBuilding.Get(uri)

    logger.debug(s"Getting $what in $where ...")

    startRequest(request).flatMap(processResponse[Either[FailedResponse, PlacesResponse]]).flatMap {
      case Left(err) =>
        Future.failed(new Exception(s"${err.errorCode}: ${err.errorMessage}"))
      case Right(placesResponse) =>
        logger.debug(s"Got ${placesResponse.result.size} $what in $where")
        Future.successful(placesResponse.result)
    }
  }

}
