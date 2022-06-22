package soda.scala.web.resp

import java.nio.charset.StandardCharsets

class TextResponse(httpCode: Int, var content: String)
  extends BaseResponse(httpCode, "text/plain; charset=utf-8") {

  override def getBody: Array[Byte] = content.getBytes(StandardCharsets.UTF_8)

}
