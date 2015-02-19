package com.github.kfang.instagram

import com.github.kfang.instagram.models.AuthResponse
import spray.json._
import scalaj.http.{HttpOptions, Http}

/**
 * http://instagram.com/developer/authentication/#
 */
case class InstagramAPI(clientId: String, clientSecret: String, clientRedirect: String, readTimeout: Int = 10000, connTimeout: Int = 10000){

  val httpOpts = Seq(HttpOptions.readTimeout(readTimeout), HttpOptions.connTimeout(connTimeout))

  //Authentication URLs
  private def AUTH_EXPLICIT_URL(redirect_uri: String): String =
    s"https://api.instagram.com/oauth/authorize/?client_id=$clientId&redirect_uri=$redirect_uri&response_type=code"
  private def AUTH_IMPLICIT_URL(redirect_uri: String): String =
    s"https://api.instagram.com/oauth/authorize/?client_id=$clientId&redirect_uri=$redirect_uri&response_type=token"
  private val REQUEST_ACCESS_TOKEN =
    s"https://api.instagram.com/oauth/access_token"

  private def genScopeParam(scopes: Seq[IGAuthScope]): String = {
    if(scopes.size == 0) "" else "scope=" + scopes.map(_.value).mkString("+")
  }

  def requestAccessToken(code: String, redirect_uri: String = clientRedirect): AuthResponse = {
    val data = Map(
      "client_id" -> clientId,
      "client_secret" -> clientSecret,
      "grant_type" -> "authorization_code",
      "redirect_uri" -> redirect_uri,
      "code" -> code
    )
      .toList
      .map({case (key, value) => s"$key=$value" })
      .mkString("&")

      Http(REQUEST_ACCESS_TOKEN)
        .postData(data)
        .options(httpOpts)
        .asString
        .body
        .parseJson
        .convertTo[AuthResponse]
  }

  //response_type=code
  def requestExplicitUrl(scopes: Seq[IGAuthScope], redirect_uri: String = clientRedirect): String = {
    AUTH_EXPLICIT_URL(redirect_uri) + "&" + genScopeParam(scopes)
  }

  //response_type=token
  def requestImplicitUrl(scopes: Seq[IGAuthScope], redirect_uri: String = clientRedirect): String = {
    AUTH_IMPLICIT_URL(redirect_uri) + "&" + genScopeParam(scopes)
  }

  //accessors for different services
  def usersService(accessToken: String): UsersService = new UsersService(accessToken, this)
  def relationshipsService(accessToken: String): RelationshipsService = new RelationshipsService(accessToken, this)

}


