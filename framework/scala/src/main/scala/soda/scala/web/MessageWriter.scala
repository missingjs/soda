package soda.scala.web

import com.sun.net.httpserver.HttpExchange

import java.nio.charset.StandardCharsets

trait MessageWriter {

  def sendMessage(exchange: HttpExchange, code: Int, message: String): Unit = {
    val data = message.getBytes(StandardCharsets.UTF_8)
    exchange.sendResponseHeaders(code, data.length)
    exchange.getResponseBody.write(data)
    exchange.getResponseBody.close()
  }

  def sendMessageWithCatch(exchange: HttpExchange, code: Int, message: String): Unit = {
    try {
      sendMessage(exchange, code, message)
    } catch {
      case ex: Throwable => Logger.exception(s"message sending error, code - $code, msg - $message", ex)
    }
  }

}
