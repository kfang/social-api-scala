package ninja.fangs.instagram

import ninja.fangs.instagram.models.{Error, AuthResponse}
import spray.json._
import scala.concurrent.{ExecutionContext, Future}
import scalaj.http.Http

/**
 * http://instagram.com/developer/authentication/#
 */
object AuthService {

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
      "client_id" -> Config.CLIENT_ID,
      "client_secret" -> Config.CLIENT_SECRET,
      "grant_type" -> "authorization_code",
      "redirect_uri" -> Config.REDIRECT_URI,
      "code" -> code
    )
      .toList
      .map({case (key, value) => s"$key=$value" })
      .mkString("&")

      Http
        .postData(Config.REQUEST_ACCESS_TOKEN, data)
        .options(Config.HTTP_OPTS)
        .asString
        .parseJson
        .convertTo[AuthResponse]
  } recover {
    case e => throw Error.parse(e)
  }

  def requestExplicitUrl(scopes: IGAuthScope*): String = {
    Config.AUTH_EXPLICIT_URL + "&" + genScopeParam(scopes)
  }

  def requestImplicitUrl(scopes: IGAuthScope*): String = {
    Config.AUTH_IMPLICIT_URL + "&" + genScopeParam(scopes)
  }

}

