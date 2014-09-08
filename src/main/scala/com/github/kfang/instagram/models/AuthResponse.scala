package com.github.kfang.instagram.models

import spray.json.DefaultJsonProtocol

case class AuthResponse(
  access_token: String,
  user: User
)

object AuthResponse extends DefaultJsonProtocol {
  implicit val igAuthResponsJs = jsonFormat2(AuthResponse.apply)
}
