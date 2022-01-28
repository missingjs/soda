package soda.scala.web
import com.sun.net.httpserver.HttpExchange

class ResetHandler extends BaseHandler {
  override def handleWork(exchange: HttpExchange): String = {
    val q = parseQuery(readPostBody(exchange))
    val classpath = q("classpath")
    ClassLoaderCache.remove(classpath)
    s"reset class loader for $classpath"
  }
}
