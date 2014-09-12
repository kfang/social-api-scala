package com.github.kfang.instagram

abstract class IGAuthScope(val value: String)
object IGAuthScope {
  case object Basic extends IGAuthScope("basic")
  case object Comments extends IGAuthScope("comments")
  case object Relationships extends IGAuthScope("relationships")
  case object Likes extends IGAuthScope("likes")
}
