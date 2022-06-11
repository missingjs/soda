package soda.kotlin.web

import com.sun.net.httpserver.HttpExchange
import java.io.BufferedReader
import java.nio.charset.StandardCharsets

interface MessageReader {

    fun parseQuery(exchange: HttpExchange): Map<String, String> {
        return parseQuery(exchange.requestURI.query)
    }

    fun parseQuery(query: String): Map<String, String> {
        return query.split("[&]")
            .map { it.split("=") }
            .filter { it.size == 2 }
            .associate { it[0] to it[1] }
    }

    fun readPostBody(exchange: HttpExchange): String {
        return exchange.requestBody.bufferedReader(StandardCharsets.UTF_8).use(BufferedReader::readText)
    }

}
