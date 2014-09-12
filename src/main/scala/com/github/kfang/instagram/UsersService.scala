package com.github.kfang.instagram

import com.github.kfang.instagram.models.{Error, UserInfo}
import spray.json._
import scala.concurrent.{ExecutionContext, Future}
import scalaj.http.Http

/**
 * http://instagram.com/developer/endpoints/users/#
 */
class UsersService(accessToken: String, instagram: InstagramAPI) {

  def getUserInfoURL(userID: String) = s"https://api.instagram.com/v1/users/$userID/?access_token=$accessToken"

  def getInfo(userID: String)(implicit ec: ExecutionContext): Future[UserInfo] = Future {
    val url = getUserInfoURL(userID)
    val response = Http.get(url).options(instagram.CLIENT_CONFIG.HTTP_OPTS).asString.parseJson
    response.asJsObject.fields("data").convertTo[UserInfo]
  } recover {
    case e => throw Error.parse(e)
  }

  def getInfo(implicit ec: ExecutionContext): Future[UserInfo] = getInfo("self")

}

