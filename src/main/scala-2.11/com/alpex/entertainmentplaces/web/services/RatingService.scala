package com.alpex.entertainmentplaces.web.services

import akka.http.scaladsl.client.RequestBuilding
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.model.Uri
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.stream.Materializer
import com.alpex.entertainmentplaces.api.RatingAPI
import com.alpex.entertainmentplaces.model.{Place, RatedPlace, RatingResponse}
import com.alpex.entertainmentplaces.util.JsonSupport
import com.alpex.entertainmentplaces.web.ApiUsage
import com.alpex.entertainmentplaces.web.HttpTransport.HttpFlow

import scala.concurrent.{ExecutionContext, Future}

/**
  * Created by alpex on 14/07/16.
  */
class RatingService(val flow: HttpFlow, val apiKey: String, val apiVersion: String)
                   (implicit val executionContext: ExecutionContext, implicit val materializer: Materializer)
  extends HttpClientService with ApiUsage with RatingAPI with JsonSupport {

  override def getRating(p: Place): Future[Option[RatedPlace]] = {

    val params = query("id" -> p.id, "hash" -> p.hash)
    val uri = Uri("/profile").withQuery(params)
    val request = RequestBuilding.Get(uri)

    startRequest(request).flatMap(response => response.status match {
      case OK =>
        val ratingResponseFuture = Unmarshal(response.entity).to[RatingResponse]
        ratingResponseFuture.map(r => makeRatedPlace(p, r))
      case _ =>
        Future.failed(new Exception("Ololo failed"))
    })
  }

  def makeRatedPlace(p: Place, ratingResponse: RatingResponse) = {
    val city = ratingResponse.cityName
    val address = p.address
    val fullAddress = s"$city, $address"
    ratingResponse.getRatingNum.map(rating => RatedPlace(p.name, fullAddress, rating))
  }

}
