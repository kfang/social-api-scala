package com.github.kfang.google

import com.github.kfang.ClientConfig
import com.github.kfang.google.models.AuthResponse
import com.typesafe.config.Config
import scala.util.{Failure, Success, Try}
import scalaj.http.Http
import spray.json._

/**
 * https://developers.google.com/accounts/docs/OAuth2Login
 * https://developers.google.com/youtube/v3/guides/authentication#server-side-apps
 */
class GoogleAPI(config: Config) {

  val CLIENT_CONFIG   = new ClientConfig(config)
  val CONFIG          = config.getConfig("google-client")
  val CLIENT_ID       = CONFIG.getString("id")
  val CLIENT_SECRET   = CONFIG.getString("secret")
  val REDIRECT_URI    = CONFIG.getString("redirect-uri")

  private val OAUTH2_CODE_REQUEST_URL = "https://accounts.google.com/o/oauth2/auth"
  private val OAUTH2_ACCESS_TOKEN_REQUEST_URL = "https://accounts.google.com/o/oauth2/token"

  sealed trait GoogleAuthScope
  case object OpenID extends GoogleAuthScope { override def toString = "openid" }
  case object Email extends GoogleAuthScope { override def toString = "email" }
  case object Profile extends GoogleAuthScope { override def toString = "profile" }
  case object PlusLogin extends GoogleAuthScope {override def toString = "https://www.googleapis.com/auth/plus.login" }

  //'state' is like a session ID - verifies that the user who authenticated is the person who sent the auth code
  def getAccountsOauth2URL(state: String, scope: GoogleAuthScope, scopes: GoogleAuthScope*): String = {
    val allScopes = List(scope).++(scopes)
    val params = List(
      "client_id" -> CLIENT_ID,
      "response_type" -> "code",
      "scope" -> allScopes.mkString(" "),
      "redirect_uri" -> REDIRECT_URI,
      "state" -> state
    ).map(t => s"${t._1}=${t._2}").mkString("&")

    s"$OAUTH2_CODE_REQUEST_URL?$params"
  }

  def requestAccessToken(code: String): AuthResponse = Try {
    val data = List(
      "code" -> code,
      "client_id" -> CLIENT_ID,
      "client_secret" -> CLIENT_SECRET,
      "redirect_uri" -> REDIRECT_URI,
      "grant_type" -> "authorization_code"
    ).map(t => s"${t._1}=${t._2}").mkString("&")

    val res = Http.postData(OAUTH2_ACCESS_TOKEN_REQUEST_URL, data).options(CLIENT_CONFIG.HTTP_OPTS).asString
    res.parseJson.convertTo[AuthResponse]
  } match {
    case Success(ar) => ar
    case Failure(e)  => throw models.Error.parse(e)
  }


  //accessors for different services
  def usersService(accessToken: String) = new UsersService(accessToken, this)

}


