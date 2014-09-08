package ninja.fangs.instagram.models

import spray.json.DefaultJsonProtocol

case class UserInfoCounts(
  media: Int,
  follows: Int,
  followed_by: Int
)

object UserInfoCounts extends DefaultJsonProtocol {
  implicit val userInfoCountJS = jsonFormat3(UserInfoCounts.apply)
}
