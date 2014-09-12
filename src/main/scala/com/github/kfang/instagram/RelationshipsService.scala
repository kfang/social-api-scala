package com.github.kfang.instagram

import com.github.kfang.instagram.models.FollowsResponse
import spray.json._
import scala.util.{Failure, Success, Try}
import scalaj.http.Http

/**
 * http://instagram.com/developer/endpoints/relationships/#
 */
class RelationshipsService(accessToken: String, instagram: InstagramAPI) {

  def getFollowsURL(userID: String) = {
    s"https://api.instagram.com/v1/users/$userID/follows?access_token=$accessToken"
  }

  def getFollows(userID: String): FollowsResponse = Try {
    val url = getFollowsURL(userID)
    val res = Http.get(url).options(instagram.CLIENT_CONFIG.HTTP_OPTS).asString
    res.parseJson.asJsObject.convertTo[FollowsResponse]
  } match {
    case Success(fr) => fr
    case Failure(e)  => throw models.Error.parse(e)
  }

  def getFollows: FollowsResponse = getFollows("self")

}


