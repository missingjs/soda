package soda.kotlin.web.http

import com.sun.net.httpserver.HttpExchange
import soda.kotlin.web.WebUtils
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

class RequestHelper {
    companion object {

        fun queryString(exchange: HttpExchange): String {
            return exchange.requestURI.query
        }

        fun contentType(exchange: HttpExchange): String {
            return exchange.requestHeaders.getFirst("Content-Type") ?: ""
        }

        fun queryMap(exchange: HttpExchange): Map<String, String> {
            return queryStringToMap(queryString(exchange))
        }

        fun queryMultimap(exchange: HttpExchange): Map<String, List<String>> {
            return queryStringToMultimap(queryString(exchange))
        }

        fun body(exchange: HttpExchange): ByteArray {
            return WebUtils.toByteArray(exchange.requestBody)
        }

        fun bodyString(exchange: HttpExchange): String {
            return String(body(exchange), StandardCharsets.UTF_8)
        }

        fun formMap(exchange: HttpExchange): Map<String, String> {
            val prefix = "application/x-www-form-urlencoded"
            if (contentType(exchange).startsWith(prefix)) {
                return queryStringToMap(bodyString(exchange))
            }
            return mapOf()
        }

        fun formMultimap(exchange: HttpExchange): Map<String, List<String>> {
            val prefix = "application/x-www-form-urlencoded"
            if (contentType(exchange).startsWith(prefix)) {
                return queryStringToMultimap(bodyString(exchange))
            }
            return mapOf()
        }

        fun formParts(exchange: HttpExchange): List<Part> {
            return parseBoundary(contentType(exchange))?.let {
                MultipartParserEx(exchange.requestBody, it).parse()
            } ?: throw RuntimeException("no boundary found in content type header")
        }

        fun multipartFormData(exchange: HttpExchange): MultipartFormData {
            return MultipartFormData(formParts(exchange).groupBy { it.getName() })
        }

        private fun queryStringToMap(query: String): Map<String, String> {
            return query.split("&")
                .map { it.split("=".toRegex(), 2) }
                .filter { it.size == 2 }
                .associate { decode(it[0]) to decode(it[1]) }
        }

        private fun queryStringToMultimap(query: String): Map<String, List<String>> {
            return query.split("&")
                .map { it.split("=".toRegex(), 2) }
                .filter { it.size == 2 }
                .groupBy { decode(it[0]) }
                .mapValues { it.value.map { L -> decode(L[1]) } }
        }

        private fun decode(encodedText: String): String {
            return URLDecoder.decode(encodedText, StandardCharsets.UTF_8)
        }

        private fun parseBoundary(contentType: String): String? {
            return WebUtils.findOne(contentType, "boundary=\"(.+?)\"", 1)
                ?: WebUtils.findOne(contentType, "boundary=(\\S+)", 1)
        }

    }
}