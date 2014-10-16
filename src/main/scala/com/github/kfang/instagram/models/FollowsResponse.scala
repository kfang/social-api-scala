package com.github.kfang.instagram.models

import spray.json._
import scala.util.{Failure, Success, Try}
import scalaj.http.{HttpOptions, Http}

case class FollowsResponse(
  pagination: Pagination,
  data: List[User]
)

object FollowsResponse extends DefaultJsonProtocol {
  implicit val followsResponsJS = jsonFormat2(FollowsResponse.apply)

  implicit class FollowsResponsePager(fr: FollowsResponse){

    def hasNext: Boolean = fr.pagination.next_url.isDefined

    def next(timeout: Int = 2000): FollowsResponse = Try {
      if(fr.pagination.next_url.isDefined) {
        val HTTP_OPTS = List(HttpOptions.readTimeout(timeout), HttpOptions.connTimeout(timeout))
        val res = Http.get(fr.pagination.next_url.get).options(HTTP_OPTS).asString.asJson
        res.convertTo[FollowsResponse]
      } else {
        fr.copy(data = List())
      }
    } match {
      case Success(nr) => nr
      case Failure(e)  => throw Error.parse(e)
    }

  }
}

