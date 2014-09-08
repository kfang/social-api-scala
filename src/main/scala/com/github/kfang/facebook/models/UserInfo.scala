package com.github.kfang.facebook.models

import spray.json.DefaultJsonProtocol

case class UserInfo(
  id: String,
  bio: Option[String],        //requires: user_about_me
  birthday: Option[String],   //requires: user_birthday
  email: Option[String],      //requires Extended Permissions: email
  website: Option[String],    //requires: user_website

  first_name: String,
  gender: String,
  last_name: String,
  link: String,
  locale: String,

  name: String,
  timezone: Int,
  updated_time: String,
  verified: Boolean
)

object UserInfo extends DefaultJsonProtocol {
  implicit val UserInfoJS = jsonFormat14(UserInfo.apply)
}
