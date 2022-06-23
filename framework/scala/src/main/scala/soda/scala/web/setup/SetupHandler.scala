package soda.scala.web.setup

import com.sun.net.httpserver.HttpExchange
import soda.scala.web.{BaseHandler, BusinessCode, Logger, ParameterMissingException, ServiceException}
import soda.scala.web.http.RequestHelper
import soda.scala.web.resp.{Response, ResponseFactory}

class SetupHandler extends BaseHandler {
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

    ClassLoaderCache.setupForJar(key, bytes)
    Logger.info(s"reset class loader for key $key")
    ResponseFactory.success()
  }

}
