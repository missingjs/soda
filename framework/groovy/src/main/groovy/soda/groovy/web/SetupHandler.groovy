package soda.groovy.web

import com.sun.net.httpserver.HttpExchange

import java.nio.file.Files
import java.nio.file.Paths

class SetupHandler extends BaseHandler {

    private final ClassLoaderManager mgr

    SetupHandler(ClassLoaderManager mgr) {
        this.mgr = mgr
    }

    @Override
    protected String handleWork(HttpExchange exchange) throws Exception {
        // key=xxx&script=xxx
        def content = getPostBody(exchange)
        def params = parseQuery(content)
        def key = params["key"]
        mgr.remove(key)

        def scB64 = params["script"]
        def bytes = Base64.urlDecoder.decode(scB64)

        def scriptDir = "/tmp/soda-groovy/works"
        Files.createDirectories(Paths.get(scriptDir))

        def filename = key.replace('/', '-')
        def scriptFile = "$scriptDir/$filename" + ".groovy"
        try (def out = new FileOutputStream(scriptFile)) {
            out.write(bytes)
        }
        mgr.setupForScript(key, scriptFile)
        Logger.info("key: $key, script file: $scriptFile")

        return "reset class loader with $key"
    }

}
