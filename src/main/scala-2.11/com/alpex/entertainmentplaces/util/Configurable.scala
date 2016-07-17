package com.alpex.entertainmentplaces.util

import com.typesafe.config.Config

/**
  * Created by alpex on 14/07/16.
  */
trait Configurable {
  def config: Config
}
