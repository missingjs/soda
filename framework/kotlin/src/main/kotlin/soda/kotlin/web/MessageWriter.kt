package soda.kotlin.web

import com.sun.net.httpserver.HttpExchange
import java.nio.charset.StandardCharsets

interface MessageWriter {

    fun sendMessage(exchange: HttpExchange, code: Int, message: String) {
        val data = message.toByteArray(StandardCharsets.UTF_8)
        exchange.sendResponseHeaders(code, data.size.toLong())
        exchange.responseBody.write(data)
        exchange.responseBody.close()
    }

    fun sendMessageWithCatch(exchange: HttpExchange, code: Int, message: String) {
        try {
            sendMessage(exchange, code, message)
        } catch (ex: Throwable) {
            Logger.exception("message sending error, code - $code, msg - $message", ex)
        }
    }

}