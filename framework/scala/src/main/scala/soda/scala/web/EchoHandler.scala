package soda.scala.web
import com.sun.net.httpserver.HttpExchange

class EchoHandler extends BaseHandler {
  override def handleJob(exchange: HttpExchange): String = exchange.getRequestURI.getQuery
}
