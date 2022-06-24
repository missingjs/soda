package soda.kotlin.web.setup

import com.sun.net.httpserver.HttpExchange
import soda.kotlin.web.BaseHandler
import soda.kotlin.web.Logger
import soda.kotlin.web.exception.ParameterMissingException
import soda.kotlin.web.http.RequestHelper
import soda.kotlin.web.resp.Response
import soda.kotlin.web.resp.ResponseFactory
import java.io.FileOutputStream
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*

class SetupHandler: BaseHandler() {

    override fun doPost(exchange: HttpExchange?): Response {
        // multipart/form-data, key = ?, jar = ?
        val data = RequestHelper.multipartFormData(exchange!!)

        val key = data.firstValue("key") ?: throw ParameterMissingException("key")
        val bytes = data.firstFile("jar") ?: throw ParameterMissingException("jar")

        ClassLoaderCache.setupForJar(key, bytes)
        Logger.info("reset class loader for key $key")
        return ResponseFactory.success()
//        val q = parseQuery(readPostBody(exchange))
//
//        val key = q["key"] ?: ""
//        ClassLoaderCache.remove(key)
//
//        val jarB64 = q["jar"]!!
//        val bytes = Base64.getUrlDecoder().decode(jarB64)
//
//        val jarDir = "/tmp/soda-kotlin/works"
//        Files.createDirectories(Paths.get(jarDir))
//
//        val filename = key.replace('/', '-')
//        val jarFile = "$jarDir/$filename.jar"
//
//        FileOutputStream(jarFile).use { it.write(bytes) }
//        ClassLoaderCache.setupForJar(key, jarFile)
//        Logger.info("key: $key, jar file: $jarFile")
//
//        return "reset class loader with $key"
    }

}