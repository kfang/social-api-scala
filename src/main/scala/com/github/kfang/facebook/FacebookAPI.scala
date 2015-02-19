package com.github.kfang.facebook

import scalaj.http.HttpOptions

case class FacebookAPI(readTimeout: Int = 10000, connTimeout: Int = 10000) {

  val httpOpts = Seq(HttpOptions.readTimeout(readTimeout), HttpOptions.connTimeout(connTimeout))

  def usersService(accessToken: String): UsersService = new UsersService(accessToken, this)

}


