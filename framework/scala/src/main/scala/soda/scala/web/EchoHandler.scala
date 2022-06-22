package soda.scala.web

import com.sun.net.httpserver.HttpExchange

import soda.scala.web.http.RequestHelper
import soda.scala.web.resp.{Response, ResponseFactory}

class EchoHandler extends BaseHandler {

  override protected def doGet(exchange: HttpExchange): Response = {
    ResponseFactory.success("echo " + RequestHelper.queryString(exchange))
  }

}
