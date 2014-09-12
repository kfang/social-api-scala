package com.github.kfang

import com.typesafe.config.Config

import scalaj.http.HttpOptions

class ClientConfig(config: Config) {

  val HTTP_OPTS = List(
    HttpOptions.readTimeout(config.getInt("common-client.read-timeout")),
    HttpOptions.connTimeout(config.getInt("common-client.connect-timeout"))
  )

}
