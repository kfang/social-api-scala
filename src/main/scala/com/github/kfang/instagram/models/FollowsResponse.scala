package com.github.kfang.instagram.models

import spray.json._
import scala.concurrent.{ExecutionContext, Future}
import scalaj.http.{HttpOptions, Http}

case class FollowsResponse(
  pagination: Pagination,
  data: List[User]
)

object FollowsResponse extends DefaultJsonProtocol {
  implicit val followsResponsJS = jsonFormat2(FollowsResponse.apply)

  implicit class FollowsResponsePager(fr: FollowsResponse){

    def hasNext: Boolean = fr.pagination.next_url.isDefined

    def next(timeout: Int = 2000)(implicit ec: ExecutionContext): Future[FollowsResponse] = Future {
      if(fr.pagination.next_url.isDefined) {
        val HTTP_OPTS = List(HttpOptions.readTimeout(timeout), HttpOptions.connTimeout(timeout))
        val res = Http.get(fr.pagination.next_url.get).options(HTTP_OPTS).asString.parseJson
        res.convertTo[FollowsResponse]
      } else {
        fr.copy(data = List())
      }
    }

  }
}

