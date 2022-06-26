package soda.scala.web.bootstrap

import com.sun.net.httpserver.HttpExchange
import soda.scala.web.exception.ParameterMissingException
import soda.scala.web.{BaseHandler, BusinessCode, Logger}
import soda.scala.web.http.RequestHelper
import soda.scala.web.resp.{Response, ResponseFactory}

class BootstrapHandler(private val contextManager: ContextManager) extends BaseHandler {

  override protected def doGet(exchange: HttpExchange): Response = {
    val qm = RequestHelper.queryMap(exchange)
    val key = qm.get("key") match {
      case Some(key) => key
      case None => throw new ParameterMissingException("key")
    }

    contextManager.get(key) match {
      case Some(ctx) =>
        qm.getOrElse("format", "") match {
          case "text" => ResponseFactory.text(ctx.getMd5)
          case _ => ResponseFactory.success(ctx.getMd5)
        }
      case None =>
        ResponseFactory.response(404, BusinessCode.COMMON_ERROR, "no context bound to key $key")
    }
  }

  override def doPost(exchange: HttpExchange): Response = {
    // multipart/form-data, key = ?, jar = ?
    val data = RequestHelper.multipartFormData(exchange)

    val key = data.firstValue("key") match {
      case Some(key) => key
      case None => throw new ParameterMissingException("key")
    }

    val bytes = data.firstFile("jar") match {
      case Some(bytes) => bytes
      case None => throw new ParameterMissingException("jar")
    }

    contextManager.register(key, bytes)

    Logger.info(s"reset class loader for key $key")
    ResponseFactory.success()
  }

}
