package com.github.kfang.instagram

import com.github.kfang.instagram.models.{Error, UserInfo}
import spray.json._
import scala.util.{Failure, Success, Try}
import scalaj.http.Http

/**
 * http://instagram.com/developer/endpoints/users/#
 */
class UsersService(accessToken: String, instagram: InstagramAPI) {

  def getUserInfoURL(userID: String) = s"https://api.instagram.com/v1/users/$userID/?access_token=$accessToken"

  def getInfo(userID: String): UserInfo = Try {
    val url = getUserInfoURL(userID)
    val response = Http.get(url).options(instagram.CLIENT_CONFIG.HTTP_OPTS).asString.asJson
    response.asJsObject.fields("data").convertTo[UserInfo]
  } match {
    case Success(ui) => ui
    case Failure(e)  => throw Error.parse(e)
  }

  def getInfo: UserInfo = getInfo("self")

}

