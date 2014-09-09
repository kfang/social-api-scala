package com.github.kfang.facebook

import com.github.kfang.Config
import com.github.kfang.facebook.models.{Friend, UserInfo}
import spray.json._
import scala.concurrent.{ExecutionContext, Future}
import scalaj.http.Http

/**
 * https://developers.facebook.com/docs/graph-api/reference/v2.1/user/
 */
object UsersService {

  //User Endpoint URLS
  def getUserInfoURL(userID: String, accessToken: String): String =
    s"https://graph.facebook.com/v2.1/$userID?access_token=$accessToken"

  def getUserFriendsURL(userID: String, accessToken: String): String =
    s"https://graph.facebook.com/v2.1/$userID/friends?access_token=$accessToken&limit=5000"

  //User Info
  def getUserInfo(accessToken: String)(implicit ec: ExecutionContext): Future[UserInfo] =
    getUserInfo("me", accessToken)

  def getUserInfo(userID: String, accessToken: String)(implicit ec: ExecutionContext): Future[UserInfo] = Future {
    val url = getUserInfoURL(userID, accessToken)
    val res = Http.get(url).options(Config.HTTP_OPTS).asString.parseJson
    res.convertTo[UserInfo]
  } recover {
    case e => throw models.Error.parse(e)
  }


  //User Friends
  def getUserFriends(accessToken: String)(implicit ec: ExecutionContext): Future[List[Friend]] =
    getUserFriends("me", accessToken)

  def getUserFriends(userID: String, accessToken: String)(implicit ec: ExecutionContext): Future[List[Friend]] = Future {
    val url = getUserFriendsURL(userID, accessToken)
    val res = Http.get(url).options(Config.HTTP_OPTS).asString.parseJson
    res.asJsObject.fields("data").convertTo[List[Friend]]
  } recover {
    case e => throw models.Error.parse(e)
  }

}


