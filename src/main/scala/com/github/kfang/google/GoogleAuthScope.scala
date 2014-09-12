package com.github.kfang.google


abstract class GoogleAuthScope(val value: String)
object GoogleAuthScope {
  case object OpenID extends GoogleAuthScope("openid")
  case object Email extends GoogleAuthScope("email")
  case object Profile extends GoogleAuthScope("profile")
  case object PlusLogin extends GoogleAuthScope("https://www.googleapis.com/auth/plus.login")
}
