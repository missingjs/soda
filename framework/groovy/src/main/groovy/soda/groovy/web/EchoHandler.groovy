package soda.groovy.web

import com.sun.net.httpserver.HttpExchange

class EchoHandler extends BaseHandler {

    @Override
    protected String handleWork(HttpExchange exchange) throws Exception {
        return exchange.requestURI.query
    }

}
