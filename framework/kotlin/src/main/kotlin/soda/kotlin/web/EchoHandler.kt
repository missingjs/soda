package soda.kotlin.web

import com.sun.net.httpserver.HttpExchange
import soda.kotlin.web.http.RequestHelper
import soda.kotlin.web.resp.Response
import soda.kotlin.web.resp.ResponseFactory

class EchoHandler : BaseHandler() {
    override fun doGet(exchange: HttpExchange?): Response {
        return ResponseFactory.success("echo " + RequestHelper.queryString(exchange!!))
    }
}