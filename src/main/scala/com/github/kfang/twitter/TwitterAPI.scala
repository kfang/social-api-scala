package com.github.kfang.twitter

import java.net.URLEncoder

import com.github.kfang.ClientConfig
import com.typesafe.config.Config
import scala.util.{Failure, Success, Try}
import scalaj.http.{Token, Http, Base64}
import spray.json._
import DefaultJsonProtocol._
import models.Error

class TwitterAPI(config: Config) {

  val CLIENT_CONFIG   = new ClientConfig(config)
  val CONFIG          = config.getConfig("twitter-client")
  val CONSUMER_KEY    = CONFIG.getString("key")
  val CONSUMER_SECRET = CONFIG.getString("secret")
  val CONSUMER_TOKEN  = Token(CONSUMER_KEY, CONSUMER_SECRET)

  /**
   * PIN-based Authentication
   * https://dev.twitter.com/oauth/pin-based
   */
  //OAUTH 1.0:
  //Step 1: https://dev.twitter.com/oauth/reference/post/oauth/request_token
  private val OAUTH_REQUEST_TOKEN = "https://api.twitter.com/oauth/request_token"

  def getRequestToken: Token = {
    val param = ("oauth_callback", "oob")
    Http.post(OAUTH_REQUEST_TOKEN).params(param).oauth(CONSUMER_TOKEN).asToken
  }

  //Step 2:
  //  https://dev.twitter.com/oauth/reference/get/oauth/authorize    - asks every time to authorize the app
  //  https://dev.twitter.com/oauth/reference/get/oauth/authenticate - asks to authorize the first time only
  private val OAUTH_REQUEST_AUTHORIZE = "https://api.twitter.com/oauth/authorize"
  private val OAUTH_REQUEST_AUTHENTICATE = "https://api.twitter.com/oauth/authenticate"

  def getRequestAuthorizeURL(request_token: Token) = {
    OAUTH_REQUEST_AUTHORIZE + "?oauth_token=" + request_token.key
  }

  def getRequestAuthenticateURL(request_token: Token) = {
    OAUTH_REQUEST_AUTHENTICATE + "?oauth_token=" + request_token.key
  }

  //Step 3: https://dev.twitter.com/oauth/reference/post/oauth/access_token
  private val OAUTH_ACCESS_TOKEN = "https://api.twitter.com/oauth/access_token"

  def getAccessToken(request_token: Token, pin_code: String) = {
    val consumer_token = Token(CONSUMER_KEY, CONSUMER_SECRET)
    Http.post(OAUTH_ACCESS_TOKEN).oauth(consumer_token, request_token, pin_code).asToken
  }

  /**
   * Application-Only Authentication
   * https://dev.twitter.com/oauth/application-only
   */
  //OAUTH 2.0: application-only
  //https://dev.twitter.com/oauth/reference/post/oauth2/token
  private val OAUTH_TOKEN_URL = "https://api.twitter.com/oauth2/token"

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

  def friendsService(access_token: Token) = new FriendsService(access_token, this)

}


