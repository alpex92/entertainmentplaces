package com.alpex.entertainmentplaces.web.services

import akka.http.scaladsl.client.RequestBuilding
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.model.Uri
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.stream.Materializer
import com.alpex.entertainmentplaces.api.PlacesAPI
import com.alpex.entertainmentplaces.model.{Place, PlacesResponse}
import com.alpex.entertainmentplaces.util.JsonSupport
import com.alpex.entertainmentplaces.web.ApiUsage
import com.alpex.entertainmentplaces.web.HttpTransport.HttpFlow

import scala.concurrent.{ExecutionContext, Future}

/**
  * Created by alpex on 14/07/16.
  */
class PlacesService(val flow: HttpFlow, val apiKey: String, val apiVersion: String)
                   (implicit val executionContext: ExecutionContext, implicit val materializer: Materializer)
  extends HttpClientService with ApiUsage with PlacesAPI with JsonSupport {

  override def getPlaces(what: String, where: String): Future[Seq[Place]] = {

    val params = query("what" -> what, "where" -> where)
    val uri = Uri("/search").withQuery(params)
    val request = RequestBuilding.Get(uri)

    startRequest(request).flatMap(response => response.status match {
      case OK =>
        Unmarshal(response.entity).to[PlacesResponse].map(_.result)
      case _ =>
        Future.failed(new Exception("Ololo failed"))
    })
  }

}
