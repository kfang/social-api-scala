package com.github.kfang.google

import com.github.kfang.google.models.AuthResponse
import scalaj.http.{HttpOptions, Http}
import spray.json._

/**
 * https://developers.google.com/accounts/docs/OAuth2Login
 * https://developers.google.com/youtube/v3/guides/authentication#server-side-apps
 */
case class GoogleAPI(clientId: String, clientSecret: String, clientRedirect: String, connTimeout: Int = 10000, readTimeout: Int = 10000) {
  val httpOpts = Seq(HttpOptions.readTimeout(readTimeout), HttpOptions.connTimeout(connTimeout))
  private val OAUTH2_CODE_REQUEST_URL = "https://accounts.google.com/o/oauth2/auth"
  private val OAUTH2_ACCESS_TOKEN_REQUEST_URL = "https://accounts.google.com/o/oauth2/token"

  //'state' is like a session ID - verifies that the user who authenticated is the person who sent the auth code
  def getAccountsOauth2URL(state: String, scope: GoogleAuthScope, scopes: GoogleAuthScope*): String = {
    val allScopes = List(scope.value).++(scopes.map(_.value))
    val params = List(
      "client_id" -> clientId,
      "response_type" -> "code",
      "scope" -> allScopes.mkString(" "),
      "redirect_uri" -> clientRedirect,
      "state" -> state
    ).map(t => s"${t._1}=${t._2}").mkString("&")

    s"$OAUTH2_CODE_REQUEST_URL?$params"
  }

  def requestAccessToken(code: String): AuthResponse = {
    val data = List(
      "code" -> code,
      "client_id" -> clientId,
      "client_secret" -> clientSecret,
      "redirect_uri" -> clientRedirect,
      "grant_type" -> "authorization_code"
    ).map(t => s"${t._1}=${t._2}").mkString("&")

    val res = Http(OAUTH2_ACCESS_TOKEN_REQUEST_URL).postData(data).options(httpOpts).asString
    res.body.parseJson.convertTo[AuthResponse]
  }


  //accessors for different services
  def usersService(accessToken: String) = new UsersService(accessToken, this)

}


