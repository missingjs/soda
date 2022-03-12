package soda.groovy.web

import com.sun.net.httpserver.HttpExchange

public class EchoHandler extends BaseHandler {

    @Override
    protected String handleWork(HttpExchange exchange) throws Exception {
        return exchange.getRequestURI().getQuery();
    }

}
