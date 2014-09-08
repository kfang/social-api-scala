package ninja.fangs.instagram

import ninja.fangs.instagram.models.{FollowsResponse, User}
import spray.json._
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future, ExecutionContext}
import scalaj.http.Http

/**
 * http://instagram.com/developer/endpoints/relationships/#
 */
object RelationshipsService {

  def getFollowsURL(userID: String, accessToken: String) = {
    s"https://api.instagram.com/v1/users/$userID/follows?access_token=$accessToken"
  }

  def getFollows(userID: String, accessToken: String)(implicit ec: ExecutionContext): Future[FollowsResponse] = Future {
    val url = getFollowsURL(userID, accessToken)
    val res = Http.get(url).options(Config.HTTP_OPTS).asString
    res.parseJson.asJsObject.convertTo[FollowsResponse]
  } recover {
    case e => throw models.Error.parse(e)
  }

  def getFollows(accessToken: String)(implicit ec: ExecutionContext): Future[FollowsResponse] = {
    getFollows("self", accessToken)
  }

}


