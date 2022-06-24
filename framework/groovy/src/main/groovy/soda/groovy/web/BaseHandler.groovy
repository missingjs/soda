package soda.groovy.web

import com.sun.net.httpserver.HttpExchange
import com.sun.net.httpserver.HttpHandler
import soda.groovy.web.exception.ServiceException
import soda.groovy.web.resp.Response
import soda.groovy.web.resp.ResponseFactory

abstract class BaseHandler implements HttpHandler {

    protected Response doGet(HttpExchange exchange) throws Exception {
        throw new RuntimeException("not implemented")
    }

    protected Response doPost(HttpExchange exchange) throws Exception {
        throw new RuntimeException("not implemented")
    }

    @Override
    void handle(HttpExchange exchange) throws IOException {
        def method = exchange.requestMethod
        def uri = exchange.requestURI

        Response resp = null
        try {
            long startNano = System.nanoTime()

            if (method == "GET") {
                resp = doGet(exchange)
            } else if (method == "POST") {
                resp = doPost(exchange)
            } else {
                throw new RuntimeException("method $method not supported")
            }

            long endNano = System.nanoTime()
            double elapseMs = (endNano - startNano) / 1e6
            int code = resp.getHttpCode()
            Logger.info("$method $uri $code $elapseMs")
        } catch (ServiceException ex) {
            Logger.exception("$method $uri ${ex.httpCode}", ex)
            resp = ResponseFactory.exception(ex)
        } catch (Throwable ex) {
            Logger.exception("$method $uri 500 internal error", ex)
            resp = ResponseFactory.exception(ex)
        }

        writeResponse(exchange, resp)
    }

    private static void writeResponse(HttpExchange exchange, Response resp) throws IOException {
        var headers = resp.headers
        headers.each {key, values ->
            values.each { exchange.responseHeaders.add(key, it) }
        }

        var body = resp.body
        exchange.responseHeaders.set("Content-Type", resp.contentType)
        exchange.sendResponseHeaders(resp.httpCode, body.length)
        exchange.responseBody.write(body)
        exchange.responseBody.close()
    }

}
