package soda.groovy.web

import com.sun.net.httpserver.HttpExchange
import soda.groovy.web.http.RequestHelper
import soda.groovy.web.resp.Response
import soda.groovy.web.resp.ResponseFactory

class EchoHandler extends BaseHandler {

    @Override
    protected Response doGet(HttpExchange exchange) throws Exception {
        return ResponseFactory.success("echo ${RequestHelper.queryString(exchange)}")
    }

}
