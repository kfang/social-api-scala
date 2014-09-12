package com.github.kfang.facebook

import com.github.kfang.ClientConfig
import com.typesafe.config.Config

class FacebookAPI(config: Config) {

  val CLIENT_CONFIG   = new ClientConfig(config)

  def usersService(accessToken: String): UsersService = new UsersService(accessToken, this)

}


