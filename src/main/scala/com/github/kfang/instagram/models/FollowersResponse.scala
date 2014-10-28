package com.github.kfang.instagram.models

import spray.json._
import scala.util.{Failure, Success, Try}
import scalaj.http.{Http, HttpOptions}

case class FollowersResponse (
  pagination: Pagination,
  data: List[User]
)

object FollowersResponse extends DefaultJsonProtocol {
  implicit val followersResponseJS = jsonFormat2(FollowersResponse.apply)

  implicit class FollowersResponsePager(fr: FollowersResponse){

    def hasNext: Boolean = fr.pagination.next_url.isDefined

    def next(timeout: Int = 2000): FollowersResponse = Try {
      if(fr.pagination.next_url.isDefined) {
        val HTTP_OPTS = List(HttpOptions.readTimeout(timeout), HttpOptions.connTimeout(timeout))
        val res = Http.get(fr.pagination.next_url.get).options(HTTP_OPTS).asString.asJson
        res.convertTo[FollowersResponse]
      } else {
        fr.copy(data = List())
      }
    } match {
      case Success(nr) => nr
      case Failure(e)  => throw Error.parse(e)
    }

  }
}
