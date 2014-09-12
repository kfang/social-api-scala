package com.github.kfang.facebook

import com.github.kfang.facebook.models.{Friend, UserInfo}
import spray.json._
import scala.util.{Failure, Success, Try}
import scalaj.http.Http

/**
 * https://developers.facebook.com/docs/graph-api/reference/v2.1/user/
 */
class UsersService(accessToken: String, facebook: FacebookAPI) {

  //User Endpoint URLS
  def getUserInfoURL(userID: String): String =
    s"https://graph.facebook.com/v2.1/$userID?access_token=$accessToken"

  def getUserFriendsURL(userID: String): String =
    s"https://graph.facebook.com/v2.1/$userID/friends?access_token=$accessToken&limit=5000"

  //User Info
  def getUserInfo: UserInfo =
    getUserInfo("me")

  def getUserInfo(userID: String): UserInfo = Try {
    val url = getUserInfoURL(userID)
    val res = Http.get(url).options(facebook.CLIENT_CONFIG.HTTP_OPTS).asString.parseJson
    res.convertTo[UserInfo]
  } match {
    case Success(ui) => ui
    case Failure(e)  => throw models.Error.parse(e)
  }


  //User Friends
  def getUserFriends: List[Friend] =
    getUserFriends("me")

  def getUserFriends(userID: String): List[Friend] = Try {
    val url = getUserFriendsURL(userID)
    val res = Http.get(url).options(facebook.CLIENT_CONFIG.HTTP_OPTS).asString.parseJson
    res.asJsObject.fields("data").convertTo[List[Friend]]
  } match {
    case Success(fl) => fl
    case Failure(e)  => throw models.Error.parse(e)
  }

}


