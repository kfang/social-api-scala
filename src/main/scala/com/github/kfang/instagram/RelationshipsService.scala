package com.github.kfang.instagram

import com.github.kfang.instagram.models.{FollowersResponse, FollowsResponse}
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
  def getFollowersURL(userID: String) = {
    s"https://api.instagram.com/v1/users/$userID/followed-by?access_token=$accessToken"
  }

  def getFollows(userID: String): FollowsResponse = Try {
    val url = getFollowsURL(userID)
    val res = Http.get(url).options(instagram.CLIENT_CONFIG.HTTP_OPTS).asString
    res.asJson.asJsObject.convertTo[FollowsResponse]
  } match {
    case Success(fr) => fr
    case Failure(e)  => throw models.Error.parse(e)
  }

  def getFollows: FollowsResponse = getFollows("self")

  def getFollowers(userID: String): FollowersResponse = Try {
    val url = getFollowersURL(userID)
    val res = Http.get(url).options(instagram.CLIENT_CONFIG.HTTP_OPTS).asString
    res.asJson.asJsObject.convertTo[FollowersResponse]
  } match {
    case Success(fr) => fr
    case Failure(e)  => throw models.Error.parse(e)
  }

  def getFollowers: FollowersResponse = getFollowers("self")

}


