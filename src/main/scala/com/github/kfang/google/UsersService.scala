package com.github.kfang.google

import com.github.kfang.google.models.{PeopleResponse, UserInfo}
import scala.util.{Failure, Success, Try}
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

  def getUserInfo(userID: String): UserInfo = Try {
    val url = getUserInfoURL(userID, accessToken)
    val res = Http.get(url).options(google.CLIENT_CONFIG.HTTP_OPTS).asString.parseJson
    res.convertTo[UserInfo]
  } match {
    case Success(ui) => ui
    case Failure(e)  => throw models.Error.parse(e)
  }


  //User Friends
  def getUserFriends(collection: PeopleCollectionList): PeopleResponse =
    getUserFriends("me", collection)

  def getUserFriends(userID: String, collection: PeopleCollectionList): PeopleResponse  =
    getUserFriends(userID, collection, None)

  def getUserFriends(userID: String, collection: PeopleCollectionList, pageToken: Option[String]): PeopleResponse = Try {

    val url = pageToken match {
      case None     => getUserFriendsURL(userID, accessToken, collection)
      case Some(pt) => getUserFriendsURL(userID, accessToken, collection, pt)
    }

    Http.get(url).options(google.CLIENT_CONFIG.HTTP_OPTS).asString.parseJson.convertTo[PeopleResponse]
  } match {
    case Success(pr) => pr
    case Failure(e)  => throw models.Error.parse(e)
  }

}


