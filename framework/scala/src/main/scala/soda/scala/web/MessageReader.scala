package soda.scala.web

import com.sun.net.httpserver.HttpExchange

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

}
