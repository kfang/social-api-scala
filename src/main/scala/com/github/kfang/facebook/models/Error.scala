package com.github.kfang.facebook.models

import spray.json._

import scala.util.{Failure, Success, Try}
import scalaj.http.HttpException

case class Error(
  code: Int,
  error_message: String,
  error_type: String,
  error_code: Int
) extends Throwable

object Error extends DefaultJsonProtocol {
  implicit val errorJS = jsonFormat4(Error.apply)

  private def parseHttpException(e: HttpException) = Try {
    val error_fields = e.body.parseJson.asJsObject.fields("error").asJsObject.fields
    Error(
      code = e.code,
      error_message = error_fields("message").convertTo[String],
      error_type = error_fields("type").convertTo[String],
      error_code = error_fields("code").convertTo[Int]
    )
  } match {
    case Success(err) => err
    case Failure(_)   => Error(e.code, e.body, e.message, 0)
  }

  def parse: Throwable => Error = {
    case e: HttpException => parseHttpException(e)
    case e                => Error(0, e.getMessage, "exception", 0)
  }
}
