package soda.scala.web.resp

import play.api.libs.json._

import soda.scala.web.BusinessCode
import soda.scala.web.exception.ServiceException

object ResponseFactory {

  private val HTTP_OK = 200

  private val HTTP_INTERNAL_SERVER_ERROR = 500

  case class CommonResp(code: Int, message: String, detail: JsValue)

  object CommonResp {
    implicit val outWrites: Writes[CommonResp] = Json.writes[CommonResp]
  }

  def response(httpCode: Int, bizCode: Int, message: String, detail: JsValue): Response = {
    val d = if (detail != null) detail else Json.obj()
    JsonResponse.create(httpCode, CommonResp(bizCode, message, d))
  }

  def response[T](httpCode: Int, bizCode: Int, message: String, detail: T)(implicit writes: Writes[T]): Response = {
    response(httpCode, bizCode, message, Json.toJson(detail))
  }

  def response(httpCode: Int, bizCode: Int, message: String): Response = {
    response(httpCode, bizCode, message, null)
  }

  def success(message: String, detail: JsValue): Response = {
    response(HTTP_OK, BusinessCode.SUCCESS, message, detail)
  }

  def success[T](message: String, detail: T)(implicit writes: Writes[T]): Response = {
    success(message, Json.toJson(detail))
  }

  def success(message: String): Response = {
    success(message, null)
  }

  def success(): Response = {
    success("success")
  }

  def exception(ex: ServiceException): Response = {
    response(ex.httpCode, ex.bizCode, ex.errMessage)
  }

  def exception(ex: Throwable): Response = {
    response(HTTP_INTERNAL_SERVER_ERROR, BusinessCode.COMMON_ERROR, s"internal server error: $ex")
  }

  def text(httpCode: Int, content: String): Response = new TextResponse(httpCode, content)

  def text(content: String): Response = new TextResponse(HTTP_OK, content)

}
