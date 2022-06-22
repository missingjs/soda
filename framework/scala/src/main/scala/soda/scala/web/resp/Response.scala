package soda.scala.web.resp

import com.sun.net.httpserver.Headers

trait Response {

  def getHttpCode: Int

  def getHeaders: Headers

  def getContentType: String

  def getBody: Array[Byte]

}
