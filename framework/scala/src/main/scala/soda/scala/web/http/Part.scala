package soda.scala.web.http

import java.nio.charset.StandardCharsets

class Part {

  var contentDisposition: ContentDisposition = null

  var contentType: String = null

  var payload: Array[Byte] = Array.empty

  def getName: String = contentDisposition.name

  def bodyString = new String(payload, StandardCharsets.UTF_8)

}
