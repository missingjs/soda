package soda.kotlin.web.setup

import com.sun.net.httpserver.HttpExchange
import soda.kotlin.web.BaseHandler
import soda.kotlin.web.Logger
import soda.kotlin.web.exception.ParameterMissingException
import soda.kotlin.web.http.RequestHelper
import soda.kotlin.web.resp.Response
import soda.kotlin.web.resp.ResponseFactory

class SetupHandler: BaseHandler() {

    override fun doPost(exchange: HttpExchange?): Response {
        // multipart/form-data, key = ?, jar = ?
        val data = RequestHelper.multipartFormData(exchange!!)

        val key = data.firstValue("key") ?: throw ParameterMissingException("key")
        val bytes = data.firstFile("jar") ?: throw ParameterMissingException("jar")

        ClassLoaderCache.setupForJar(key, bytes)
        Logger.info("reset class loader for key $key")
        return ResponseFactory.success()
    }

}