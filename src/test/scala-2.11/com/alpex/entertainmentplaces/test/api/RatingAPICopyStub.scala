package com.alpex.entertainmentplaces.test.api

import com.alpex.entertainmentplaces.api.{PlacesAPI, RatingAPI}
import com.alpex.entertainmentplaces.model.{Place, RatedPlace}

import scala.concurrent.{ExecutionContext, Future}

/**
  * Created by alpex on 17/07/16.
  */

trait PlacesStub extends PlacesAPI {

  def count: Int
  def ec: ExecutionContext

  override def getPlaces(what: String, where: String): Future[Seq[Place]] = {
    val range = 1.0 to count.toDouble + 1 by 1.0
    val places = range.map(i => {
      val iStr = i.toString
      Place(iStr, iStr, iStr, iStr)
    })
    Future(places)(ec)
  }

}

class SimplePlacesStub(val count: Int)(implicit val ec: ExecutionContext) extends PlacesStub

class RatingAPICopyStub(implicit ec: ExecutionContext) extends RatingAPI {
  override def getRating(p: Place): Future[Option[RatedPlace]] = {
    Future(Option(RatedPlace(p.name, p.address, p.id)))
  }
}

class RatingAPIFillStub(val value: String)(implicit ec: ExecutionContext) extends RatingAPI {
  override def getRating(p: Place): Future[Option[RatedPlace]] = {
    Future(Option(RatedPlace(p.name, p.address, value)))
  }
}
