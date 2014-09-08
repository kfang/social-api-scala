package com.github.kfang.instagram.models

import spray.json.DefaultJsonProtocol

case class Pagination(
  next_url: Option[String],
  next_max_id: Option[String]
)

object Pagination extends DefaultJsonProtocol {
  implicit val paginationJS = jsonFormat2(Pagination.apply)
}
