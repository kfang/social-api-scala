package com.github.kfang.twitter

import java.net.URLEncoder

import scalaj.http.{HttpOptions, Token, Http, Base64}
import spray.json._
import DefaultJsonProtocol._

case class TwitterAPI(key: String, secret: String, readTimeout: Int = 10000, connTimeout: Int = 10000) {

  val token  = Token(key, secret)
  val httpOpts = Seq(HttpOptions.readTimeout(readTimeout), HttpOptions.connTimeout(connTimeout))

  /**
   * PIN-based Authentication
   * https://dev.twitter.com/oauth/pin-based
   * Step 1 - user the consumer_token to get a request_token
   * Step 2 - sent the user to a url to authorize the app and get a PIN code
   * Step 3 - use the consumer_token, request_token, and the user's PIN code to get a user access_token
   */
  //OAUTH 1.0:
  //Step 1: https://dev.twitter.com/oauth/reference/post/oauth/request_token
  private val OAUTH_REQUEST_TOKEN = "https://api.twitter.com/oauth/request_token"

  def getRequestToken(oauth_callback: String = "oob"): Token = {
    val param = ("oauth_callback", oauth_callback)
    Http(OAUTH_REQUEST_TOKEN)
      .method("POST")
      .params(param)
      .options(httpOpts)
      .oauth(token)
      .asToken.body
  }

  //Step 2:
  //  https://dev.twitter.com/oauth/reference/get/oauth/authorize    - asks every time to authorize the app
  //  https://dev.twitter.com/oauth/reference/get/oauth/authenticate - asks to authorize the first time only
  private val OAUTH_REQUEST_AUTHORIZE = "https://api.twitter.com/oauth/authorize"
  private val OAUTH_REQUEST_AUTHENTICATE = "https://api.twitter.com/oauth/authenticate"

  def getRequestAuthorizeURL(request_token: Token): String = {
    OAUTH_REQUEST_AUTHORIZE + "?oauth_token=" + request_token.key
  }

  def getRequestAuthenticateURL(request_token: Token): String = {
    OAUTH_REQUEST_AUTHENTICATE + "?oauth_token=" + request_token.key
  }

  //Step 3: https://dev.twitter.com/oauth/reference/post/oauth/access_token
  private val OAUTH_ACCESS_TOKEN = "https://api.twitter.com/oauth/access_token"

  def getAccessToken(request_token: Token, pin_code: String): Token = {
    Http(OAUTH_ACCESS_TOKEN)
      .options(httpOpts)
      .oauth(token, request_token, pin_code)
      .asToken.body
  }

  /**
   * Application-Only Authentication
   * https://dev.twitter.com/oauth/application-only
   */
  //OAUTH 2.0: application-only
  //https://dev.twitter.com/oauth/reference/post/oauth2/token
  private val OAUTH_TOKEN_URL = "https://api.twitter.com/oauth2/token"

  def getBearerToken: String = {
    //url encode key and secret
    val encoded_key = URLEncoder.encode(key, "UTF-8")
    val encoded_secret = URLEncoder.encode(secret, "UTF-8")

    //concatenate and base64 encode to generate credentials
    val concat_creds = encoded_key + ":" + encoded_secret
    val base64_creds = Base64.encodeString(concat_creds)

    //execute request
    val auth_body = "grant_type=client_credentials"
    val auth_header = "Authorization" -> ("Basic " + base64_creds)
    val auth_response = Http(OAUTH_TOKEN_URL)
      .postData(auth_body)
      .options(httpOpts)
      .method("POST")
      .headers(auth_header)

    //parse response
    val auth_fields = auth_response.asString.body.parseJson.asJsObject.fields
    auth_fields("access_token").convertTo[String]
  }

  def friendsService(access_token: Token) = new FriendsService(access_token, this)
  def usersService(access_token: Token) = new UsersService(access_token, this)

}


