package com.github.kfang.google

import com.github.kfang.Config
import com.github.kfang.google.models.{PartPeopleResponse, PeopleResponse, UserInfo}
import scala.concurrent.{Future, ExecutionContext}
import scalaj.http.Http
import spray.json._

/**
 * https://developers.google.com/+/api/latest/people/get
 */
object UsersService {

  sealed trait PeopleCollectionList
  case object Connected extends PeopleCollectionList { override def toString = "connected" }
  case object Visible extends PeopleCollectionList { override def toString = "visible" }

  def getUserInfoURL(userID: String, accessToken: String) =
    s"https://www.googleapis.com/plus/v1/people/$userID?access_token=$accessToken"
  def getUserFriendsURL(userID: String, accessToken: String, collection: PeopleCollectionList) =
    s"https://www.googleapis.com/plus/v1/people/$userID/people/$collection?access_token=$accessToken"
  def getUserFriendsURL(userID: String, accessToken: String, collection: PeopleCollectionList, pageToken: String) =
    s"https://www.googleapis.com/plus/v1/people/$userID/people/$collection?access_token=$accessToken&pageToken=$pageToken"


  //User Info
  def getUserInfo(accessToken: String)(implicit ec: ExecutionContext): Future[UserInfo] =
    getUserInfo("me", accessToken)

  def getUserInfo(userID: String, accessToken: String)(implicit ec: ExecutionContext): Future[UserInfo] = Future {
    val url = getUserInfoURL(userID, accessToken)
    val res = Http.get(url).options(Config.HTTP_OPTS).asString.parseJson
    println(res.prettyPrint)
    res.convertTo[UserInfo]
  } recover {
    case e => throw models.Error.parse(e)
  }

  def getUserFriends(accessToken: String, collection: PeopleCollectionList)(implicit ec: ExecutionContext): Future[PeopleResponse] =
    getUserFriends("me", accessToken, collection)

  def getUserFriends(userID: String, accessToken: String, collection: PeopleCollectionList)(implicit ec: ExecutionContext): Future[PeopleResponse] =
    getUserFriends(userID, accessToken, collection, None)

  def getUserFriends(userID: String, accessToken: String, collection: PeopleCollectionList, pageToken: Option[String])
                    (implicit ec: ExecutionContext): Future[PeopleResponse] = Future {
    val url = pageToken match {
      case None     => getUserFriendsURL(userID, accessToken, collection)
      case Some(pt) => getUserFriendsURL(userID, accessToken, collection, pt)
    }
    val p = Http.get(url).options(Config.HTTP_OPTS).asString.parseJson.convertTo[PartPeopleResponse]
    PeopleResponse(userID, accessToken, collection, p.kind, p.title, p.totalItems, p.nextPageToken, p.items)
  }

}


