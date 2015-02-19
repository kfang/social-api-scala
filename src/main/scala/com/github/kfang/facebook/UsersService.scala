package com.github.kfang.facebook

import com.github.kfang.facebook.models.{Friend, UserInfo}
import spray.json._
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

  def getUserInfo(userID: String): UserInfo = {
    val url = getUserInfoURL(userID)
    val res = Http(url).options(facebook.httpOpts).asString.body.parseJson
    res.convertTo[UserInfo]
  }


  //User Friends
  def getUserFriends: List[Friend] = getUserFriends("me")

  def getUserFriends(userID: String): List[Friend] = {
    val url = getUserFriendsURL(userID)
    val res = Http(url).options(facebook.httpOpts).asString.body.parseJson
    res.asJsObject.fields("data").convertTo[List[Friend]]
  }

}


