package com.github.kfang.instagram.models

import spray.json.DefaultJsonProtocol

case class User(
  id: String,
  username: String,
  full_name: String,
  profile_picture: String
)

object User extends DefaultJsonProtocol {
  implicit val igUserJs = jsonFormat4(User.apply)
}