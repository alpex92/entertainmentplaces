package com.alpex.entertainmentplaces.test

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import com.alpex.entertainmentplaces.test.api.{RatingAPICopyStub, RatingAPIFillStub, SimplePlacesStub}
import com.alpex.entertainmentplaces.web.HttpClient
import com.alpex.entertainmentplaces.web.services.{PlacesService, RatingService, SearchService}
import com.typesafe.config.ConfigFactory
import org.scalatest.time.{Millis, Seconds, Span}

/**
  * Created by alpex on 17/07/16.
  */

class SearchServiceSpec extends BaseSpec {

  implicit val system = ActorSystem()
  implicit val executor = system.dispatcher
  implicit val materializer = ActorMaterializer()

  val config = ConfigFactory.load()
  val client = new HttpClient(config)

  val apiKey = config.getString("api.key")
  val apiVersion = config.getString("api.version")

  val placesApi = new PlacesService(client, apiKey, apiVersion)
  val ratingApi = new RatingService(client, apiKey, apiVersion)
  val searchService = new SearchService(placesApi, ratingApi)

  val cinema = "кино"
  val abracadabra = "uyrTQQwBcR"

  val networkPatienceConfig = PatienceConfig(Span(5, Seconds), interval = Span(500, Millis))

  // Rating

  "SearchService" should "get all places with 5.0 rating" in {
    val service = new SearchService(new SimplePlacesStub(4), new RatingAPICopyStub())
    val future = service.search("")
    val places = future.futureValue
    assert(places.count(_.rating == "5.0") == service.cities.size)
  }

  it should "get no places with 0.0 rating" in {
    val service = new SearchService(new SimplePlacesStub(4), new RatingAPIFillStub("0.0"))
    val future = service.search("")
    assert(future.futureValue.isEmpty)
  }

  it should "get no places with non-numeric rating" in {
    val service = new SearchService(new SimplePlacesStub(4), new RatingAPIFillStub("asdasd121dasd"))
    val future = service.search("")
    assert(future.futureValue.isEmpty)
  }

  // Requests via 2Gis API

  it should s"find some $cinema" in {
    assert(searchService.search(cinema).futureValue(networkPatienceConfig).nonEmpty)
  }

  it should s"not find $abracadabra" in {
    assert(searchService.search(abracadabra).futureValue(networkPatienceConfig).isEmpty)
  }

}
