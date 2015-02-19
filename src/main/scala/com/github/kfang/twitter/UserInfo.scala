package com.github.kfang.twitter

import spray.json.DefaultJsonProtocol

case class UserInfo(
                     id: Long,
                     name: String,
                     screen_name: String,
                     description: String,
                     followers_count: Int,

                     friends_count: Int,
                     listed_count: Int,
                     created_at: String,
                     favourites_count: Int,
                     verified: Boolean,

                     statuses_count: Int,
                     lang: String,
                     profile_image_url: String,
                     profile_image_url_https: String,
                     default_profile: Boolean,

                     default_profile_image: Boolean,
                     following: Boolean,
                     follow_request_sent: Boolean,
                     notifications: Boolean
                     )

object UserInfo extends DefaultJsonProtocol {
  implicit val userInfoJS = jsonFormat19(UserInfo.apply)
}
