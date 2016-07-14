package com.alpex.entertainmentplaces.web

import akka.http.scaladsl.model.Uri.Query

/**
  * Created by alpex on 14/07/16.
  */
trait ApiUsage {

  def apiVersion: String
  def apiKey: String

  def defaultParams = Seq("key" -> apiKey, "version" -> apiVersion)

  def query(params: (String, String)*): Query = {
    Query((defaultParams ++ params).toMap)
  }
}
