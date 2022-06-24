package soda.groovy.web.http

import com.sun.net.httpserver.HttpExchange
import soda.groovy.web.WebUtils

import java.nio.charset.StandardCharsets

class RequestHelper {

    static String queryString(HttpExchange exchange) {
        return exchange.requestURI.query
    }

    static String contentType(HttpExchange exchange) {
        def ct = exchange.requestHeaders.getFirst("Content-Type")
        return ct != null ? ct : ""
    }

    static Map<String, String> queryMap(HttpExchange exchange) {
        return queryStringToMap(queryString(exchange))
    }

    static Map<String, List<String>> queryMultimap(HttpExchange exchange) {
        return queryStringToMultimap(queryString(exchange))
    }

    static byte[] body(HttpExchange exchange) throws IOException {
        return WebUtils.toByteArray(exchange.requestBody)
    }

    static String bodyString(HttpExchange exchange) throws IOException {
        return new String(body(exchange), StandardCharsets.UTF_8)
    }

    static Map<String, String> formMap(HttpExchange exchange) throws IOException {
        def cType = contentType(exchange)
        if (cType.startsWith("application/x-www-form-urlencoded")) {
            return queryStringToMap(bodyString(exchange))
        } else {
            return Collections.emptyMap()
        }
    }

    static Map<String, List<String>> formMultimap(HttpExchange exchange) throws IOException {
        def cType = contentType(exchange)
        if (cType.startsWith("application/x-www-form-urlencoded")) {
            return queryStringToMultimap(bodyString(exchange))
        } else {
            return Collections.emptyMap()
        }
    }

    static List<Part> formParts(HttpExchange exchange) throws IOException {
        def boundary = parseBoundary(contentType(exchange)).orElseThrow {
            new RuntimeException("no boundary found in content type header")
        }
        def parser = new MultipartParserEx(exchange.requestBody, boundary)
        return parser.parse()
    }

    static MultipartFormData multipartFormData(HttpExchange exchange) throws IOException {
        def m = formParts(exchange).groupBy { it.name }
        return new MultipartFormData(m)
    }

    private static Map<String, String> queryStringToMap(String query) {
        return query.split("&")
                .collect { it.split("=", 2) }
                .findAll { it.size() == 2 }
                .collectEntries { [decode(it[0]), decode(it[1])] }
    }

    private static Map<String, List<String>> queryStringToMultimap(String query) {
        return query.split("&")
                .collect { it.split("=", 2) as List }
                .findAll { it.size() == 2 }
                .groupBy { decode(it[0]) }
                .collectEntries { k, v ->
                    [(k): v.collect{ decode(it[1]) }]
                } as Map<String, List<String>>
    }

    private static String decode(String encodedText) {
        return URLDecoder.decode(encodedText, StandardCharsets.UTF_8)
    }

    private static Optional<String> parseBoundary(String contentType) {
        def match = { String pattern ->
            WebUtils.findOne(contentType, pattern, 1)
        }
        return match(/boundary="(.+?)"/) | { match(/boundary=(\S+)/) }
    }

}