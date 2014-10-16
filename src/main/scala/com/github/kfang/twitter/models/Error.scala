package com.github.kfang.twitter.models

import spray.json._
import scala.util.{Failure, Success, Try}
import scalaj.http.HttpException

case class ErrorItem(
  code: Int,
  message: String,
  label: String
)
object ErrorItem extends DefaultJsonProtocol {
  implicit val errorItemJS = jsonFormat3(ErrorItem.apply)
}

case class Error(
  errors: List[ErrorItem]
) extends Throwable
object Error extends DefaultJsonProtocol {

  implicit val errorJS = jsonFormat1(Error.apply)

  def parse: Throwable => Error = {
    case e: Error         => e
    case e: HttpException => Try(e.body.parseJson.convertTo[Error]) match {
      case Success(err) => err
      case Failure(_)   => Error(List(ErrorItem(e.code, e.body, "http-exception")))
    }
    case e                => Error(List(ErrorItem(0, e.getMessage, "exception")))
  }
}
