package com.github.kfang.google.models

import spray.json.DefaultJsonProtocol

case class UserInfoNamePart(
  familyName: String,
  givenName: String
)
object UserInfoNamePart extends DefaultJsonProtocol {
  implicit val userInfoNamePartJS = jsonFormat2(UserInfoNamePart.apply)
}

case class UserInfoImagePart(
  url: String,
  isDefault: Boolean
)
object UserInfoImagePart extends DefaultJsonProtocol {
  implicit val userInfoImagePartJS = jsonFormat2(UserInfoImagePart.apply)
}

case class UserInfoEmailPart(
  value: String,
  `type`: String
)

object UserInfoEmailPart extends DefaultJsonProtocol {
  implicit val userInfoEmailPartJS = jsonFormat2(UserInfoEmailPart.apply)
}

case class UserInfoAgeRangePart(
  min: Int
)
object UserInfoAgeRangePart extends DefaultJsonProtocol {
  implicit val userInfoAgeRangePartJS = jsonFormat1(UserInfoAgeRangePart.apply)
}

case class UserInfo(
  kind: String,
  etag: String,
  gender: String,
  objectType: String,
  id: String,

  displayName: String,
  name: UserInfoNamePart,
  url: String,
  image: UserInfoImagePart,
  isPlusUser: Boolean,

  circledByCount: Int,
  verified: Boolean,
  emails: Option[List[UserInfoEmailPart]], // required scope: email
  language: Option[String],                // required scope: profile
  ageRange: Option[UserInfoAgeRangePart]   // required scope: plus.login
)

object UserInfo extends DefaultJsonProtocol {
  implicit val userInfoJS = jsonFormat15(UserInfo.apply)
}
