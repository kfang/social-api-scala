package com.github.kfang.twitter

import scala.util.{Failure, Success, Try}
import scalaj.http.{Token, Http}
import spray.json._

class FriendsService(access_token: Token, client: TwitterAPI) {

  //https://dev.twitter.com/rest/reference/get/friends/ids
  private val FRIENDS_URL = "https://api.twitter.com/1.1/friends/ids.json"
  //https://dev.twitter.com/rest/reference/get/followers/ids
  private val FOLLOWERS_URL = "https://api.twitter.com/1.1/followers/ids.json"

  case class IdsResponse(
    previous_cursor: Int,
    previous_cursor_str: String,
    ids: List[Long],
    next_cursor: Int,
    next_cursor_str: String
  )
  object IdsResponse extends DefaultJsonProtocol {
    implicit val friendsResponseJS = jsonFormat5(IdsResponse.apply)
  }

  def getFriends(user_id: String, next_cursor: Option[Int] = None): List[Long] = Try {

    val request_params = List(
      Some("user_id" -> user_id),
      Some("count" -> "5000"),
      next_cursor.map(i => "cursor" -> i.toString)
    ).flatten

    val friends_response = Http.get(FRIENDS_URL).params(request_params)
      .options(client.CLIENT_CONFIG.HTTP_OPTS)
      .oauth(client.CONSUMER_TOKEN, access_token)
      .asString.asJson.convertTo[IdsResponse]

    val friendIds = friends_response.ids
    if(friendIds.size < 5000) {
      friendIds
    } else {
      friendIds ++ getFriends(user_id, Some(friends_response.next_cursor))
    }
  } match {
    case Success(friendIds) => friendIds
    case Failure(error) => throw models.Error.parse(error)
  }

  def getFollowers(userId: String, next_cursor: Option[Int] = None): List[Long] = Try {

    val request_params = List(
      Some("user_id" -> userId),
      Some("count" -> "5000"),
      next_cursor.map(i => "cursor" -> i.toString)
    ).flatten

    val followers_response = Http.get(FOLLOWERS_URL).params(request_params)
      .options(client.CLIENT_CONFIG.HTTP_OPTS)
      .oauth(client.CONSUMER_TOKEN, access_token)
      .asString.asJson.convertTo[IdsResponse]

    val followerIds = followers_response.ids

    if(followerIds.size < 5000){
      followerIds
    } else {
      followerIds ++ getFollowers(userId, Some(followers_response.next_cursor))
    }
  } match {
    case Success(followerIds) => followerIds
    case Failure(error) => throw models.Error.parse(error)
  }

}


