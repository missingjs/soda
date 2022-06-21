package soda.scala.web

import com.sun.net.httpserver.HttpExchange
import soda.scala.web.http.Part

import java.util.regex.Pattern
import scala.io.Source

trait MessageReader {

  def parseQuery(exchange: HttpExchange): Map[String, String] = {
    parseQuery(exchange.getRequestURI.getQuery)
  }

  def parseQuery(query: String): Map[String, String] = {
    query.split("&")
      .map(_.split("=", 2))
      .filter(_.length == 2)
      .map(a => (a(0), a(1)))
      .toMap
  }

  def readPostBody(exchange: HttpExchange): String = {
    Source.fromInputStream(exchange.getRequestBody, "UTF-8").mkString
  }

  def parseMultipart(exchange: HttpExchange): Map[String, Part] = {
    val headers = exchange.getRequestHeaders
    val contentType = headers.getFirst("Content-Type")
    if (contentType == null || !contentType.startsWith("multipart/form-data")) {
      throw new RuntimeException("content type must be multipart/form-data")
    }

    var boundary = _match("boundary=\"(.+?)\"", contentType, 1)
    if (boundary == null) {
      boundary = _match("boundary=(\\S+)", contentType, 1)
      if (boundary == null) {
        throw new RuntimeException("no boundary found in content type header")
      }
    }

    val input = exchange.getRequestBody
    val parser = new MultipartParser(input, boundary)
    parser.parse().map(it => it.getName -> it).toMap
  }

  private def _match(pattern: String, text: String, group: Int): String = {
    val m = Pattern.compile(pattern).matcher(text)
    if (m.find()) m.group(group) else null
  }

}
