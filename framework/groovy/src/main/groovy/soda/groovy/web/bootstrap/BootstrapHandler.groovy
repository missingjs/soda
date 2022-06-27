package soda.groovy.web.bootstrap

import com.sun.net.httpserver.HttpExchange
import soda.groovy.web.BaseHandler
import soda.groovy.web.BusinessCode
import soda.groovy.web.Logger
import soda.groovy.web.exception.ParameterMissingException
import soda.groovy.web.http.RequestHelper
import soda.groovy.web.resp.Response
import soda.groovy.web.resp.ResponseFactory

class BootstrapHandler extends BaseHandler {

    private final ContextManager contextManager

    BootstrapHandler(ContextManager cm) {
        contextManager = cm
    }

    @Override
    protected Response doGet(HttpExchange exchange) throws Exception {
        def qm = RequestHelper.queryMap(exchange)
        def key = qm["key"]
        if (key == null) {
            throw new ParameterMissingException("key")
        }

        return contextManager.get(key)
                .map { "text" == qm["format"]
                                ? ResponseFactory.text(it.getMd5())
                                : ResponseFactory.success(it.getMd5()) }
                .orElseGet { ResponseFactory.response(
                                404, BusinessCode.COMMON_ERROR,
                                "no context bound to key " + key) }
    }

    @Override
    protected Response doPost(HttpExchange exchange) throws Exception {
        // multipart/form-data, key = ?, script = ?
        def data = RequestHelper.multipartFormData(exchange)

        def key = data.firstValue("key").orElseThrow {
            new ParameterMissingException("key")
        }
        def bytes = data.firstFile("script").orElseThrow {
            new ParameterMissingException("script")
        }
        contextManager.register(key, bytes)

        Logger.info("reset class loader for key $key")
        return ResponseFactory.success()
    }

}
