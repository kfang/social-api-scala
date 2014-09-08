package com.github.kfang.instagram.models

import spray.json._
import scala.util.{Failure, Success, Try}
import scalaj.http.HttpException

case class Error(
  code: Int,
  error_type: String,
  error_message: String
) extends Throwable

object Error extends DefaultJsonProtocol {
  implicit val igErrorJs = jsonFormat3(Error.apply)

  def parse: Throwable => Error = {
    case e: Error         => e
    case e: HttpException => Try(e.body.parseJson.convertTo[Error]) match {
      case Success(err) => err
      case Failure(_)   => Error(e.code, "http-exception", e.body)
    }
    case e                => Error(0, "throwable", e.getMessage)
  }

}
