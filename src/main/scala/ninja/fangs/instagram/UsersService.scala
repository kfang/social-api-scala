package ninja.fangs.instagram

import ninja.fangs.instagram.models.{Error, UserInfo}
import spray.json._
import scala.concurrent.{ExecutionContext, Future}
import scalaj.http.Http

/**
 * http://instagram.com/developer/endpoints/users/#
 */
object UsersService {

  def getUserInfoURL(userID: String, accessToken: String) = s"https://api.instagram.com/v1/users/$userID/?access_token=$accessToken"

  def getInfo(userID: String, accessToken: String)(implicit ec: ExecutionContext): Future[UserInfo] = Future {
    val url = getUserInfoURL(userID, accessToken)
    val response = Http.get(url).options(Config.HTTP_OPTS).asString.parseJson
    println(response.prettyPrint)
    response.asJsObject.fields("data").convertTo[UserInfo]
  } recover {
    case e => throw Error.parse(e)
  }

  def getInfo(accessToken: String)(implicit ec: ExecutionContext): Future[UserInfo] = getInfo("self", accessToken)

}

