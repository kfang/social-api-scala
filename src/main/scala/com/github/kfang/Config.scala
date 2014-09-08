package com.github.kfang

import com.typesafe.config.ConfigFactory

import scalaj.http.HttpOptions

object Config {

  val HTTP_OPTS = List(
    HttpOptions.readTimeout(ConfigFactory.load.getInt("common-client.read-timeout")),
    HttpOptions.connTimeout(ConfigFactory.load.getInt("common-client.connect-timeout"))
  )

}
