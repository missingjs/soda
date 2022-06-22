package soda.scala.web.resp

import play.api.libs.json._

import java.nio.charset.StandardCharsets

class JsonResponse(httpCode: Int, var body: JsValue)
  extends BaseResponse(httpCode, "application/json; charset=utf-8") {

  override def getBody: Array[Byte] = {
    Json.stringify(body).getBytes(StandardCharsets.UTF_8)
  }

}

object JsonResponse {
  def create[T](httpCode: Int, body: T)(implicit fmt: Writes[T]): JsonResponse = {
    new JsonResponse(httpCode, Json.toJson(body))
  }
}
