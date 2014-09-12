package com.github.kfang.instagram

import com.github.kfang.ClientConfig
import com.github.kfang.instagram.models.{Error, AuthResponse}
import com.typesafe.config.Config
import spray.json._
import scala.concurrent.{ExecutionContext, Future}
import scalaj.http.Http

/**
 * http://instagram.com/developer/authentication/#
 */
class InstagramAPI(config: Config){

  val CLIENT_CONFIG   = new ClientConfig(config)
  val CONFIG          = config.getConfig("instagram-client")
  val CLIENT_ID       = CONFIG.getString("id")
  val CLIENT_SECRET   = CONFIG.getString("secret")
  val REDIRECT_URI    = CONFIG.getString("redirect-uri")

  //Authentication URLs
  private val AUTH_EXPLICIT_URL =
    s"https://api.instagram.com/oauth/authorize/?client_id=$CLIENT_ID&redirect_uri=$REDIRECT_URI&response_type=code"
  private val AUTH_IMPLICIT_URL =
    s"https://api.instagram.com/oauth/authorize/?client_id=$CLIENT_ID&redirect_uri=$REDIRECT_URI&response_type=token"
  private val REQUEST_ACCESS_TOKEN =
    s"https://api.instagram.com/oauth/access_token"

  //Scope Permissions
  sealed trait IGAuthScope
  case object Basic extends IGAuthScope { override def toString = "basic"}
  case object Comments extends IGAuthScope { override def toString = "comments" }
  case object Relationships extends IGAuthScope { override def toString = "relationships" }
  case object Likes extends IGAuthScope { override def toString = "likes" }

  private def genScopeParam(scopes: Seq[IGAuthScope]): String = {
    if(scopes.size == 0) "" else "scope=" + scopes.mkString("+")
  }

  def requestAccessToken(code: String)(implicit ec: ExecutionContext): Future[AuthResponse] = Future {
    val data = Map(
      "client_id" -> CLIENT_ID,
      "client_secret" -> CLIENT_SECRET,
      "grant_type" -> "authorization_code",
      "redirect_uri" -> REDIRECT_URI,
      "code" -> code
    )
      .toList
      .map({case (key, value) => s"$key=$value" })
      .mkString("&")

      Http
        .postData(REQUEST_ACCESS_TOKEN, data)
        .options(CLIENT_CONFIG.HTTP_OPTS)
        .asString
        .parseJson
        .convertTo[AuthResponse]
  } recover {
    case e => throw Error.parse(e)
  }

  def requestExplicitUrl(scopes: IGAuthScope*): String = {
    AUTH_EXPLICIT_URL + "&" + genScopeParam(scopes)
  }

  def requestImplicitUrl(scopes: IGAuthScope*): String = {
    AUTH_IMPLICIT_URL + "&" + genScopeParam(scopes)
  }

  //accessors for different services
  def usersService(accessToken: String): UsersService = new UsersService(accessToken, this)
  def relationshipsService(accessToken: String): RelationshipsService = new RelationshipsService(accessToken, this)

}


