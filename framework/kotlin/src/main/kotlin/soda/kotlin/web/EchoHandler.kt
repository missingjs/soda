package soda.kotlin.web

import com.sun.net.httpserver.HttpExchange

class EchoHandler : BaseHandler() {
    override fun handleWork(exchange: HttpExchange): String {
        return exchange.requestURI.query
    }
}