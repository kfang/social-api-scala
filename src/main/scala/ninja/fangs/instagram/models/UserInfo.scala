package ninja.fangs.instagram.models

import spray.json.DefaultJsonProtocol

case class UserInfo(
  id: String,
  username: String,
  full_name: String,
  profile_picture: String,
  bio: String,
  website: String,
  counts: UserInfoCounts
)

object UserInfo extends DefaultJsonProtocol {
  implicit val userInfoJS = jsonFormat7(UserInfo.apply)
}
