package soda.scala.web
import com.sun.net.httpserver.HttpExchange

import java.io.FileOutputStream
import java.nio.file.{Files, Paths}
import java.util.Base64
import scala.util.Using

class SetupHandler extends BaseHandler {
  override def handleWork(exchange: HttpExchange): String = {
    val q = parseQuery(readPostBody(exchange))
    val key = q("key")
    ClassLoaderCache.remove(key)

    val jarB64 = q("jar")
    val bytes = Base64.getUrlDecoder.decode(jarB64)

    val jarDir = "/tmp/soda-scala/works"
    Files.createDirectories(Paths.get(jarDir))

    val filename = key.replace('/', '-')
    val jarFile = s"$jarDir/$filename.jar"

    Using(new FileOutputStream(jarFile)) { out =>
      out.write(bytes)
    }
    ClassLoaderCache.setupForJar(key, jarFile)
    Logger.info(s"key: $key, jar file: $jarFile")

    s"reset class loader with $key"
  }
}
