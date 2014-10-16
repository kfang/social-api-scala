package com.github.kfang.twitter

import scalaj.http.Http
import spray.json._

class FriendsService(bearerToken: String, client: TwitterAPI) {

  private val auth_header = "Authorization" -> ("Bearer " + bearerToken)
  //https://dev.twitter.com/rest/reference/get/friends/ids
  private val FRIENDS_URL = "https://api.twitter.com/1.1/friends/ids.json"


  case class FriendsResponse(
    previous_cursor: Int,
    previous_cursor_str: String,
    ids: List[Long],
    next_cursor: Int,
    next_cursor_str: String
  )
  object FriendsResponse extends DefaultJsonProtocol {
    implicit val friendsResponseJS = jsonFormat5(FriendsResponse.apply)
  }

  def getFriendsByUserID(user_id: String, next_cursor: Option[Int] = None): List[Long] = {

    val request_params = List(
      Some("user_id" -> user_id),
      Some("count" -> "5000"),
      next_cursor.map(i => "cursor" -> i.toString)
    ).flatten

    val friends_response = Http(FRIENDS_URL).method("GET").headers(auth_header).params(request_params)
      .asString.parseJson.convertTo[FriendsResponse]

    friends_response.ids ++ getFriendsByUserID(user_id, Some(friends_response.next_cursor))

  }
}
