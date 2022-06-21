package soda.scala.web.http

import com.sun.net.httpserver.HttpExchange

import java.net.URLDecoder
import java.nio.charset.StandardCharsets

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

}
