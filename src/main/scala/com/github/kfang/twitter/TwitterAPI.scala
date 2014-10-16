package com.github.kfang.twitter

import java.net.URLEncoder

import com.github.kfang.ClientConfig
import com.typesafe.config.Config
import scala.util.{Failure, Success, Try}
import scalaj.http.{Http, Base64}
import spray.json._
import DefaultJsonProtocol._
import models.Error

class TwitterAPI(config: Config) {

  val CLIENT_CONFIG   = new ClientConfig(config)
  val CONFIG          = config.getConfig("twitter-client")
  val CONSUMER_KEY    = CONFIG.getString("key")
  val CONSUMER_SECRET = CONFIG.getString("secret")


  //https://dev.twitter.com/oauth/reference/post/oauth2/token
  private val OAUTH_TOKEN_URL = "https://api.twitter.com/oauth2/token"

  /**
   * Application-Only Authentication
   * https://dev.twitter.com/oauth/application-only
   */
  def getBearerToken: String = Try {
    //url encode key and secret
    val encoded_key = URLEncoder.encode(CONSUMER_KEY, "UTF-8")
    val encoded_secret = URLEncoder.encode(CONSUMER_SECRET, "UTF-8")

    //concatenate and base64 encode to generate credentials
    val concat_creds = encoded_key + ":" + encoded_secret
    val base64_creds = Base64.encodeString(concat_creds)

    //execute request
    val auth_body = "grant_type=client_credentials"
    val auth_header = "Authorization" -> ("Basic " + base64_creds)
    val auth_response = Http.postData(OAUTH_TOKEN_URL, auth_body).method("POST").headers(auth_header)

    //parse response
    val auth_fields = auth_response.asString.parseJson.asJsObject.fields
    auth_fields("access_token").convertTo[String]
  } match {
    case Success(token) => token
    case Failure(error) => throw Error.parse(error)
  }

  def friendsService(bearerToken: String) = new FriendsService(bearerToken, this)

}


