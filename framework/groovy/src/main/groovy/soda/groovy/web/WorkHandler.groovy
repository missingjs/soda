package soda.groovy.web

import com.sun.net.httpserver.HttpExchange
import groovy.json.JsonSlurper

import java.util.concurrent.FutureTask
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

class WorkHandler extends BaseHandler {

    private final ClassLoaderManager mgr

    private final long timeoutMillis

    WorkHandler(ClassLoaderManager mgr, long timeoutMillis) {
        this.mgr = mgr
        this.timeoutMillis = timeoutMillis
    }

    @Override
    protected String handleWork(HttpExchange exchange) throws Exception {
        String content = getPostBody(exchange)
        def jr = new WorkRequest(new JsonSlurper().parseText(content))

        ClassLoader loader = mgr.get(jr.classpath)
        def gcl = new GroovyClassLoader(loader)
        gcl.parseClass(new File(jr.scriptFile))
        def klass = gcl.loadClass(jr.bootClass)

        def workClosure = { ->
            def ctor = klass.getDeclaredConstructor()
            ctor.setAccessible(true)
            def obj = ctor.newInstance()
            return obj(jr.testCase)
        }

        TimeLimitedJob tLJob = new TimeLimitedJob(workClosure)
        FutureTask<String> future = tLJob.start()
        try {
            return future.get(timeoutMillis, TimeUnit.MILLISECONDS)
        } catch (TimeoutException tex) {
            tLJob.kill()
            throw new RuntimeException("Job timeout", tex)
        }
    }

}
