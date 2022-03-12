package soda.groovy.web

import com.sun.net.httpserver.HttpExchange
import com.sun.net.httpserver.HttpHandler

import java.nio.charset.StandardCharsets

abstract class BaseHandler implements HttpHandler {

    @Override
    void handle(HttpExchange exchange) throws IOException {
        def method = exchange.requestMethod
        def uri = exchange.requestURI
        try {
            String result = handleWork(exchange)
            sendMessage(exchange, 200, result)
            Logger.info("$method $uri 200 $result")
        } catch (Exception ex) {
            Logger.exception("$method $uri 500 request handling error", ex)
            sendMessageWithCatch(exchange, 500, WebUtils.toString(ex))
        }
    }

    protected abstract String handleWork(HttpExchange exchange) throws Exception

    protected void sendMessage(HttpExchange exch, int code, String message) throws IOException {
        byte[] data = message.getBytes(StandardCharsets.UTF_8)
        exch.sendResponseHeaders(code, data.length)
        exch.responseBody.write(data)
        exch.responseBody.close()
    }

    protected void sendMessageWithCatch(HttpExchange exch, int code, String message) {
        try {
            sendMessage(exch, code, message)
        } catch (IOException ex) {
            Logger.exception("message sending error, code - $code, msg - $message", ex)
        }
    }

    protected Map<String, String> parseQuery(HttpExchange exch) {
        parseQuery(exch.requestURI.query)
    }

    protected Map<String, String> parseQuery(String query) {
        query.split('&')
                .collect {it.split('=')}
                .findAll {it.length==2}
                .collectEntries {[it[0], it[1]]}
    }

    protected String getPostBody(HttpExchange exch) throws IOException {
        exch.requestBody.getText('UTF-8')
    }

}
