package com.github.kfang.instagram

import com.github.kfang.instagram.models.UserInfo
import spray.json._
import scalaj.http.Http

/**
 * http://instagram.com/developer/endpoints/users/#
 */
class UsersService(accessToken: String, instagram: InstagramAPI) {

  def getUserInfoURL(userID: String) = s"https://api.instagram.com/v1/users/$userID/?access_token=$accessToken"

  def getInfo(userID: String): UserInfo = {
    val url = getUserInfoURL(userID)
    val response = Http(url).options(instagram.httpOpts).asString.body.parseJson
    response.asJsObject.fields("data").convertTo[UserInfo]
  }

  def getInfo: UserInfo = getInfo("self")

}

