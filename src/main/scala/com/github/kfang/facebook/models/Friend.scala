package com.github.kfang.facebook.models

import spray.json.DefaultJsonProtocol

case class Friend(
  name: String,
  id: String
)
object Friend extends DefaultJsonProtocol {
  implicit val friendJS = jsonFormat2(Friend.apply)
}
