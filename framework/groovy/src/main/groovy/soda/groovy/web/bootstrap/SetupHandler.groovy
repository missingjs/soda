package soda.groovy.web.bootstrap

import com.sun.net.httpserver.HttpExchange
import soda.groovy.web.BaseHandler
import soda.groovy.web.Logger
import soda.groovy.web.exception.ParameterMissingException
import soda.groovy.web.http.RequestHelper
import soda.groovy.web.resp.Response
import soda.groovy.web.resp.ResponseFactory

class SetupHandler extends BaseHandler {

    private final ClassLoaderManager mgr

    SetupHandler(ClassLoaderManager mgr) {
        this.mgr = mgr
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
        mgr.setupForScript(key, bytes)

        Logger.info("reset class loader for key $key")
        return ResponseFactory.success()
    }

}
