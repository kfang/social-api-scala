package com.github.kfang.instagram

import com.github.kfang.instagram.models.{FollowersResponse, FollowsResponse}
import spray.json._
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

  def getFollows(userID: String): FollowsResponse = {
    val url = getFollowsURL(userID)
    val res = Http(url).options(instagram.httpOpts).asString
    res.body.parseJson.asJsObject.convertTo[FollowsResponse]
  }

  def getFollows: FollowsResponse = getFollows("self")

  def getFollowers(userID: String): FollowersResponse = {
    val url = getFollowersURL(userID)
    val res = Http(url).options(instagram.httpOpts).asString
    res.body.parseJson.asJsObject.convertTo[FollowersResponse]
  }

  def getFollowers: FollowersResponse = getFollowers("self")

}


