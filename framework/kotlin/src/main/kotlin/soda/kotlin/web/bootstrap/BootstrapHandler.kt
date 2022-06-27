package soda.kotlin.web.bootstrap

import com.sun.net.httpserver.HttpExchange
import soda.kotlin.web.BaseHandler
import soda.kotlin.web.BusinessCode
import soda.kotlin.web.Logger
import soda.kotlin.web.exception.ParameterMissingException
import soda.kotlin.web.http.RequestHelper
import soda.kotlin.web.resp.Response
import soda.kotlin.web.resp.ResponseFactory

class BootstrapHandler(private val contextManager: ContextManager): BaseHandler() {

    override fun doGet(exchange: HttpExchange?): Response {
        val qm = RequestHelper.queryMap(exchange!!)
        val key = qm["key"] ?: throw ParameterMissingException("key")
        return contextManager.get(key)?.let {
            when (qm.getOrDefault("format", "")) {
                "text" -> ResponseFactory.text(it.getMd5())
                else -> ResponseFactory.success(it.getMd5())
            }
        } ?: ResponseFactory.response(
            404, BusinessCode.COMMON_ERROR,
            "no context bound to key $key"
        )
    }

    override fun doPost(exchange: HttpExchange?): Response {
        // multipart/form-data, key = ?, jar = ?
        val data = RequestHelper.multipartFormData(exchange!!)

        val key = data.firstValue("key")
        val bytes = data.firstFile("binary")
        contextManager.register(key, bytes)

        Logger.info("reset class loader for key $key")
        return ResponseFactory.success()
    }

}