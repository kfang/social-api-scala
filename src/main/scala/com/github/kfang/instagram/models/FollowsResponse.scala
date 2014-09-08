package com.github.kfang.instagram.models

import com.github.kfang.Config
import spray.json._
import scala.concurrent.{ExecutionContext, Future}
import scalaj.http.Http

case class FollowsResponse(
  pagination: Pagination,
  data: List[User]
)

object FollowsResponse extends DefaultJsonProtocol {
  implicit val followsResponsJS = jsonFormat2(FollowsResponse.apply)

  implicit class FollowsResponsePager(fr: FollowsResponse){

    def hasNext: Boolean = fr.pagination.next_url.isDefined

    def next(implicit ec: ExecutionContext): Future[FollowsResponse] = Future {
      if(fr.pagination.next_url.isDefined) {
        val res = Http.get(fr.pagination.next_url.get).options(Config.HTTP_OPTS).asString.parseJson
        res.convertTo[FollowsResponse]
      } else {
        fr.copy(data = List())
      }
    }

  }
}

