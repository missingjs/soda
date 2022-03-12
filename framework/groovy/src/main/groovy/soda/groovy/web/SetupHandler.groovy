package soda.groovy.web

import com.sun.net.httpserver.HttpExchange

class SetupHandler extends BaseHandler {

    private final ClassLoaderManager mgr;

    SetupHandler(ClassLoaderManager mgr) {
        this.mgr = mgr;
    }

    @Override
    protected String handleWork(HttpExchange exchange) throws Exception {
        String content = getPostBody(exchange);
        Map<String, String> params = parseQuery(content);
        String classpath = params["classpath"]
        mgr.remove(classpath)
        "reset class loader for $classpath"
    }

}
