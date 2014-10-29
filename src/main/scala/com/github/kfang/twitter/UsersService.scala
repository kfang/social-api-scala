package com.github.kfang.twitter

import com.github.kfang.twitter.models.UserInfo

import scala.util.{Failure, Success, Try}
import scalaj.http.{Http, Token}
import spray.json._

class UsersService(access_token: Token, client: TwitterAPI) {

  //https://dev.twitter.com/rest/reference/get/account/verify_credentials
  private val VERIFY_CREDENTIALS_URL = "https://api.twitter.com/1.1/account/verify_credentials.json"

  def verifyCredentials = Try {
    Http.get(VERIFY_CREDENTIALS_URL)
      .options(client.CLIENT_CONFIG.HTTP_OPTS)
      .oauth(client.CONSUMER_TOKEN, access_token)
      .asString.asJson.convertTo[UserInfo]
  } match {
    case Success(info) => info
    case Failure(error) => throw models.Error.parse(error)
  }

}
