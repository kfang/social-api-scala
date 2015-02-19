package com.github.kfang.google

import com.github.kfang.google.models.{PeopleResponse, UserInfo}
import scalaj.http.Http
import spray.json._

/**
 * https://developers.google.com/+/api/latest/people/get
 */
class UsersService(accessToken: String, google: GoogleAPI) {

  def getUserInfoURL(userID: String, accessToken: String) =
    s"https://www.googleapis.com/plus/v1/people/$userID?access_token=$accessToken"

  def getUserFriendsURL(userID: String, accessToken: String, collection: PeopleCollectionList) =
    s"https://www.googleapis.com/plus/v1/people/$userID/people/$collection?access_token=$accessToken"

  def getUserFriendsURL(userID: String, accessToken: String, collection: PeopleCollectionList, pageToken: String) =
    s"https://www.googleapis.com/plus/v1/people/$userID/people/$collection?access_token=$accessToken&pageToken=$pageToken"


  //User Info
  def getUserInfo: UserInfo = getUserInfo("me")

  def getUserInfo(userID: String): UserInfo = {
    val url = getUserInfoURL(userID, accessToken)
    val res = Http(url).options(google.httpOpts).asString.body.parseJson
    res.convertTo[UserInfo]
  }


  //User Friends
  def getUserFriends(collection: PeopleCollectionList): PeopleResponse =
    getUserFriends("me", collection)

  def getUserFriends(userID: String, collection: PeopleCollectionList): PeopleResponse  =
    getUserFriends(userID, collection, None)

  def getUserFriends(userID: String, collection: PeopleCollectionList, pageToken: Option[String]): PeopleResponse = {

    val url = pageToken match {
      case None     => getUserFriendsURL(userID, accessToken, collection)
      case Some(pt) => getUserFriendsURL(userID, accessToken, collection, pt)
    }

    Http(url).options(google.httpOpts).asString.body.parseJson.convertTo[PeopleResponse]
  }

}


