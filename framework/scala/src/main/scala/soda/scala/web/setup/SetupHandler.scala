package soda.scala.web.setup

import com.sun.net.httpserver.HttpExchange
import soda.scala.web.{BaseHandler, BusinessCode, Logger, ServiceException}
import soda.scala.web.http.RequestHelper
import soda.scala.web.resp.{Response, ResponseFactory}

class SetupHandler extends BaseHandler {
  override def doPost(exchange: HttpExchange): Response = {
    // multipart/form-data, key = ?, jar = ?
    val data = RequestHelper.multipartFormData(exchange)

    data.firstValue("key") match {
      case Some(key) =>
        ClassLoaderCache.remove(key)
        data.firstFile("jar") match {
          case Some(bytes) =>
            ClassLoaderCache.setupForJar(key, bytes)
            Logger.info(s"reset class loader for key $key")
            ResponseFactory.success()
          case None => throw new ServiceException(BusinessCode.COMMON_ERROR, "missing parameter 'jar'", 400)
        }
      case None => throw new ServiceException(BusinessCode.COMMON_ERROR, "missing parameter 'key'", 400)
    }
  }
}
