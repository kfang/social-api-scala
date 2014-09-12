package com.github.kfang.google


abstract class PeopleCollectionList(val value: String)
object PeopleCollectionList {
  case object Connected extends PeopleCollectionList("connected")
  case object Visible extends PeopleCollectionList("visible")
}
