package soda.scala.web
import com.sun.net.httpserver.HttpExchange

class SetupHandler extends BaseHandler {
  override def handleWork(exchange: HttpExchange): String = {
    // multipart/form-data, key = ?, jar = ?
    val parts = parseMultipart(exchange)
    val key: String = parts("key").bodyString
    ClassLoaderCache.remove(key)

    val bytes: Array[Byte] = parts("jar").payload
    ClassLoaderCache.setupForJar(key, bytes)

    s"reset class loader with $key"
  }
}
