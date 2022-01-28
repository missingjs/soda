package soda.scala.web
import com.sun.net.httpserver.HttpExchange

class EchoHandler extends BaseHandler {
  override def handleWork(exchange: HttpExchange): String = exchange.getRequestURI.getQuery
}
