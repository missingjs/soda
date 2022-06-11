package soda.kotlin.web

import com.sun.net.httpserver.HttpExchange
import com.sun.net.httpserver.HttpHandler

abstract class BaseHandler : HttpHandler, MessageReader, MessageWriter {

    abstract fun handleWork(exchange: HttpExchange): String

    override fun handle(exchange: HttpExchange?) {
        if (exchange == null) {
            throw IllegalArgumentException("exchange may not be null")
        }
        val method = exchange.requestMethod
        val uri = exchange.requestURI
        try {
            val result = handleWork(exchange)
            sendMessage(exchange, 200, result)
            Logger.info("$method $uri 200 $result")
        } catch (ex: Exception) {
            Logger.exception("$method $uri 500 request handling error", ex)
            sendMessageWithCatch(exchange, 500, Utils.toString(ex))
        }
    }

}