package com.github.kfang.google.models

import com.github.kfang.google.UsersService
import com.github.kfang.google.UsersService.PeopleCollectionList
import spray.json.DefaultJsonProtocol

import scala.concurrent.{ExecutionContext, Future}

case class PersonImage(
  url: String
)
object PersonImage extends DefaultJsonProtocol {
  implicit val personImageJS = jsonFormat1(PersonImage.apply)
}

case class Person(
  kind: String,
  id: String,
  displayName: String,
  url: String,
  image: PersonImage
)
object Person extends DefaultJsonProtocol {
  implicit val personJS = jsonFormat5(Person.apply)
}

case class PartPeopleResponse(
  kind: String,
  title: String,
  totalItems: Int,
  nextPageToken: Option[String],
  items: List[Person]
)

object PartPeopleResponse extends DefaultJsonProtocol {
  implicit val peopleResponseJS = jsonFormat5(PartPeopleResponse.apply)
}

case class PeopleResponse(
  userId: String,
  accessToken: String,
  collection: PeopleCollectionList,
  kind: String,
  title: String,
  totalItems: Int,
  nextPageToken: Option[String],
  items: List[Person]
) {

  def hasNext: Boolean = nextPageToken.isDefined

  def next(implicit ec: ExecutionContext): Future[PeopleResponse] = nextPageToken match {
    case None     => Future(this.copy(items = List()))
    case Some(pt) => UsersService.getUserFriends(userId, accessToken, collection, Some(pt))
  }

}
