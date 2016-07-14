package com.alpex.entertainmentplaces.web

import akka.http.scaladsl.model.Uri.Query

/**
  * Created by alpex on 14/07/16.
  */
trait ApiUsage {

  def apiVersion: String
  def apiKey: String

  def query(params: (String, String)*): Query = {
    val query = Query("key" -> apiKey, "version" -> apiVersion)
    params.foreach(p => query :+ p)
    query
  }

}
