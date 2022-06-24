package soda.kotlin.web

import com.sun.net.httpserver.HttpExchange
import com.sun.net.httpserver.HttpHandler
import soda.kotlin.web.exception.ServiceException
import soda.kotlin.web.resp.Response
import soda.kotlin.web.resp.ResponseFactory
import java.nio.charset.StandardCharsets

abstract class BaseHandler : HttpHandler {

    protected open fun doGet(exchange: HttpExchange?): Response {
        throw RuntimeException("not implemented")
    }

    protected open fun doPost(exchange: HttpExchange?): Response {
        throw RuntimeException("not implemented")
    }

    override fun handle(exchange: HttpExchange?) {
        if (exchange == null) {
            throw IllegalArgumentException("exchange may not be null")
        }

        val method = exchange.requestMethod
        val uri = exchange.requestURI
        var resp: Response

        try {
            val startNano = System.nanoTime()
            if (method == "GET") {
                resp = doGet(exchange)
            } else if (method == "POST") {
                resp = doPost(exchange)
            } else {
                throw RuntimeException("method $method not supported")
            }
            val endNano = System.nanoTime()
            val elapseMs = (endNano - startNano) / 1e6
            val code = resp.getHttpCode()
            Logger.info("$method $uri $code $elapseMs")
        } catch (ex: ServiceException) {
            val message = "$method $uri ${ex.httpCode} business error"
            Logger.exception(message, ex)
            resp = ResponseFactory.exception(ex)
        } catch (ex: Throwable) {
            val message = "$method $uri 500 internal error"
            Logger.exception(message, ex)
            resp = ResponseFactory.exception(ex)
        }

        writeResponse(exchange, resp)
    }

    private fun writeResponse(exchange: HttpExchange, resp: Response) {
        try {
            val headers = resp.getHeaders()
            for (key in headers.keys) {
                val values = headers[key]!!
                for (value in values) {
                    exchange.responseHeaders.add(key, value)
                }
            }

            val body = resp.getBody()
            exchange.responseHeaders.set("Content-Type", resp.getContentType())
            exchange.sendResponseHeaders(resp.getHttpCode(), body.size.toLong())

            exchange.responseBody.use {
                it.write(body)
            }
        } catch (ex: Exception) {
            Logger.exception("response error", ex)
        }
    }

}