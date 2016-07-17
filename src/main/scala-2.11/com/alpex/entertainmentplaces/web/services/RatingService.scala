package com.alpex.entertainmentplaces.web.services

import akka.http.scaladsl.client.RequestBuilding
import akka.http.scaladsl.model.Uri
import akka.stream.Materializer
import com.alpex.entertainmentplaces.api.RatingAPI
import com.alpex.entertainmentplaces.model.{ErrorResponse, Place, RatedPlace, RatingResponse}
import com.alpex.entertainmentplaces.web.{ApiUsage, HttpClient}
import com.alpex.entertainmentplaces.json.SprayJson

import scala.concurrent.{ExecutionContext, Future}

/**
  * Created by alpex on 14/07/16.
  */
class RatingService(val client: HttpClient, val apiKey: String, val apiVersion: String)
                   (implicit val executionContext: ExecutionContext, implicit val materializer: Materializer)
  extends HttpClientService with ApiUsage with RatingAPI with SprayJson {

  override def getRating(p: Place): Future[Option[RatedPlace]] = {

    val params = query("id" -> p.id, "hash" -> p.hash)
    val uri = Uri("/profile").withQuery(params)
    val request = RequestBuilding.Get(uri)

    startRequest(request).flatMap(processResponse[Either[ErrorResponse, RatingResponse]]).flatMap {
      case Left(err) =>
        processError(err, {
          case "404" =>
            Future.successful(None)
        })
      case Right(response) =>
        Future.successful(makeRatedPlace(p, response))
    }
  }

  protected def makeRatedPlace(p: Place, ratingResponse: RatingResponse) = {
    val fullAddress = s"${ratingResponse.cityName}, ${p.address}"
    ratingResponse.rating.map(rating => RatedPlace(p.name, fullAddress, rating))
  }

}
