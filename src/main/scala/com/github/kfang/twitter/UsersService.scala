package com.github.kfang.twitter

import scalaj.http.{Http, Token}
import spray.json._

class UsersService(access_token: Token, client: TwitterAPI) {

  //https://dev.twitter.com/rest/reference/get/account/verify_credentials
  private val VERIFY_CREDENTIALS_URL = "https://api.twitter.com/1.1/account/verify_credentials.json"

  def verifyCredentials: UserInfo = {
    Http(VERIFY_CREDENTIALS_URL)
      .options(client.httpOpts)
      .oauth(client.token, access_token)
      .asString
      .body
      .parseJson
      .convertTo[UserInfo]
  }

}
