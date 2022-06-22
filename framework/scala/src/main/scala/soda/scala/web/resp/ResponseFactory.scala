package soda.scala.web.resp

import play.api.libs.json._

import soda.scala.web.{BusinessCode, ServiceException}

object ResponseFactory {

  private val HTTP_OK = 200

  private val HTTP_INTERNAL_SERVER_ERROR = 500

  case class SimpleResp(code: Int, message: String)

  implicit val outWrites: Writes[SimpleResp] = Json.writes[SimpleResp]

  def success(message: String): Response = {
    JsonResponse.create(HTTP_OK, SimpleResp(BusinessCode.SUCCESS, message))
  }

  def success(): Response = {
    success("success")
  }

  def error(httpCode: Int, bizCode: Int, message: String): Response = {
    JsonResponse.create(httpCode, SimpleResp(bizCode, message))
  }

  def exception(ex: ServiceException): Response = {
    error(ex.httpCode, ex.bizCode, ex.errMessage)
  }

  def exception(ex: Throwable): Response = {
    error(HTTP_INTERNAL_SERVER_ERROR, BusinessCode.COMMON_ERROR, s"internal server error: $ex")
  }

  def text(httpCode: Int, content: String): Response = new TextResponse(httpCode, content)

  def text(content: String): Response = new TextResponse(HTTP_OK, content)

}
