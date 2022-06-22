package soda.scala.web

import scala.jdk.CollectionConverters._

import com.sun.net.httpserver.{HttpExchange, HttpHandler}
import soda.scala.web.resp.{Response, ResponseFactory}

abstract class BaseHandler extends HttpHandler {

  protected def doGet(exchange: HttpExchange): Response = {
    throw new RuntimeException("not implemented")
  }

  protected def doPost(exchange: HttpExchange): Response = {
    throw new RuntimeException("not implemented")
  }

  override def handle(exchange: HttpExchange): Unit = {
    val method = exchange.getRequestMethod
    val uri = exchange.getRequestURI
    var resp: Response = null
    try {
      val startNano = System.nanoTime()

      if (method == "GET") {
        resp = doGet(exchange)
      } else if (method == "POST") {
        resp = doPost(exchange)
      } else {
        throw new RuntimeException(s"method $method not supported")
      }

      val endNano = System.nanoTime()
      val elapseMs = (endNano - startNano) / 1e6
      val code = resp.getHttpCode
      Logger.info(s"$method $uri $code $elapseMs")
    } catch {
      case ex: ServiceException =>
        val message = s"$method $uri ${ex.httpCode} business error"
        Logger.exception(message, ex)
        resp = ResponseFactory.exception(ex)
      case ex: Throwable =>
        val message = s"$method $uri 500 internal error"
        Logger.exception(message, ex)
        resp = ResponseFactory.exception(ex)
    }

    writeResponse(exchange, resp)
  }

  private def writeResponse(exchange: HttpExchange, resp: Response): Unit = {
    val headers = resp.getHeaders
    for (key <- headers.keySet().asScala) {
      val values = headers.get(key)
      for (value <- values.asScala) {
        exchange.getResponseHeaders.add(key, value)
      }
    }

    val body = resp.getBody
    exchange.getResponseHeaders.set("Content-Type", resp.getContentType)
    exchange.sendResponseHeaders(resp.getHttpCode, body.length)

    val out = exchange.getResponseBody
    out.write(body)
    out.close()
  }

}
