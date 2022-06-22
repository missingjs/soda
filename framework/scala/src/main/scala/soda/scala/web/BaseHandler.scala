package soda.scala.web

import com.sun.net.httpserver.{HttpExchange, HttpHandler}

abstract class BaseHandler extends HttpHandler with MessageReader with MessageWriter {

  def handleWork(exchange: HttpExchange): String

  override def handle(exchange: HttpExchange): Unit = {
    val method = exchange.getRequestMethod
    val uri = exchange.getRequestURI
    try {
      val result = handleWork(exchange)
      sendMessage(exchange, 200, result)
      Logger.info(s"$method $uri 200 $result")
    } catch {
      case ex: Exception =>
        Logger.exception(s"$method $uri 500 request handling error", ex)
        sendMessageWithCatch(exchange, 500, Utils.toString(ex))
    }
  }

}
