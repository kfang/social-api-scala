package com.github.kfang.google.models

import spray.json._
import scala.util.{Failure, Success, Try}
import scalaj.http.HttpException

case class Error(
  error: String,
  code: Int
) extends Throwable

object Error extends DefaultJsonProtocol {
  implicit val errorJS = jsonFormat2(Error.apply)

  private def parseHttpException(e: HttpException): Error = Try {
    val err = e.body.parseJson.asJsObject.fields("error").convertTo[String]
    Error(err, e.code)
  } match {
    case Success(s) => s
    case Failure(f) => Error(e.body, e.code)
  }

  def parse: Throwable => Error = {
    case e: HttpException => parseHttpException(e)
    case e                => Error(e.getMessage, 0)
  }
}
