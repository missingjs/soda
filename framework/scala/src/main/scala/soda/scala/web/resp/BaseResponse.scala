package soda.scala.web.resp

import com.sun.net.httpserver.Headers

abstract class BaseResponse(private val httpCode: Int, contentType: String) extends Response {

  private val headers = new Headers()

  headers.set("Content-Type", contentType)

  override def getHttpCode: Int = httpCode

  override def getHeaders: Headers = headers

  override def getContentType: String = headers.getFirst("Content-Type")

}
