package soda.scala.web.http

import com.sun.net.httpserver.HttpExchange

import java.net.URLDecoder
import java.nio.charset.StandardCharsets
import soda.scala.web.Utils
import java.util.regex.Pattern
import soda.scala.web.MultipartParserEx

object RequestHelper {

  def queryString(exchange: HttpExchange): String = {
    exchange.getRequestURI.getQuery
  }

  def contentType(exchange: HttpExchange): String = {
    val c = exchange.getRequestHeaders.getFirst("Content-Type")
    if (c != null) c else ""
  }

  def queryMap(exchange: HttpExchange): Map[String, String] = {
    queryStringToMap(queryString(exchange))
  }

  def queryMultimap(exchange: HttpExchange): Map[String, List[String]] = {
    queryStringToMultimap(queryString(exchange))
  }

  def body(exchange: HttpExchange): Array[Byte] = {
    Utils.toByteArray(exchange.getRequestBody)
  }

  def bodyString(exchange: HttpExchange): String = {
    new String(body(exchange), StandardCharsets.UTF_8)
  }

  def formMap(exchange: HttpExchange): Map[String, String] = {
    contentType(exchange) match {
      case f if f.startsWith("application/x-www-form-urlencoded") =>
        queryStringToMap(bodyString(exchange))
      case _ => Map.empty
    }
  }

  def formMultimap(exchange: HttpExchange): Map[String, List[String]] = {
    contentType(exchange) match {
      case f if f.startsWith("application/x-www-form-urlencoded") =>
        queryStringToMultimap(bodyString(exchange))
      case _ => Map.empty
    }
  }

  def formParts(exchange: HttpExchange): List[Part] = {
    val boundary = parseBoundary(contentType(exchange))
    val parser = new MultipartParserEx(exchange.getRequestBody, boundary)
    parser.parse()
  }

  def multipartFormData(exchange: HttpExchange): MultipartFormData = {
    new MultipartFormData(formParts(exchange).groupBy(_.getName))
  }

  private def queryStringToMap(query: String): Map[String, String] = {
    query.split("&")
      .map(_.split("=", 2))
      .filter(_.length == 2)
      .map(ss => (decode(ss(0)), decode(ss(1))))
      .toMap
  }

  private def queryStringToMultimap(query: String): Map[String, List[String]] = {
    query.split("&")
      .map(_.split("=", 2))
      .filter(_.length == 2)
      .toList
      .groupMap(ss => decode(ss(0)))(_(1))
  }

  private def decode(encodedText: String): String = {
    URLDecoder.decode(encodedText, StandardCharsets.UTF_8)
  }

  private def parseBoundary(contentType: String): String = {
    search("boundary=\"(.+?)\"", contentType, 1) match {
      case Some(b) => b
      case None =>
        search("boundary=(\\S+)", contentType, 1) match {
          case Some(bd) => bd
          case None => throw new RuntimeException("no boundary found in content type header")
        }
    }
  }

  private def search(pattern: String, text: String, group: Int): Option[String] = {
    val m = Pattern.compile(pattern).matcher(text)
    Option(if (m.find()) m.group(group) else null)
  }

}
