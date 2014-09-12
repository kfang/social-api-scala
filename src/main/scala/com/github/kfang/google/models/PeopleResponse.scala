package com.github.kfang.google.models

import spray.json.DefaultJsonProtocol

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

case class PeopleResponse(
  kind: String,
  title: String,
  totalItems: Int,
  nextPageToken: Option[String],
  items: List[Person]
)

object PeopleResponse extends DefaultJsonProtocol {
  implicit val peopleResponseJS = jsonFormat5(PeopleResponse.apply)
}
