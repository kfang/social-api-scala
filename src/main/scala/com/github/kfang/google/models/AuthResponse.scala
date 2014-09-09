package com.github.kfang.google.models

import spray.json.DefaultJsonProtocol

case class AuthResponse(
  access_token: String,
  id_token: String,
  expires_in: Long,
  token_type: String,
  refresh_token: Option[String]
)

object AuthResponse extends DefaultJsonProtocol {
  implicit val authResponseJS = jsonFormat5(AuthResponse.apply)
}
