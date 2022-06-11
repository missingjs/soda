package soda.kotlin.web

import com.sun.net.httpserver.HttpExchange
import java.io.FileOutputStream
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*

class SetupHandler: BaseHandler() {

    override fun handleWork(exchange: HttpExchange): String {
        val q = parseQuery(readPostBody(exchange))
        val key = q["key"] ?: ""
        ClassLoaderCache.remove(key)

        val jarB64 = q["jar"]!!
        val bytes = Base64.getUrlDecoder().decode(jarB64)

        val jarDir = "/tmp/soda-kotlin/works"
        Files.createDirectories(Paths.get(jarDir))

        val filename = key.replace('/', '-')
        val jarFile = "$jarDir/$filename.jar"

        FileOutputStream(jarFile).use { it.write(bytes) }
        ClassLoaderCache.setupForJar(key, jarFile)
        Logger.info("key: $key, jar file: $jarFile")

        return "reset class loader with $key"
    }

}