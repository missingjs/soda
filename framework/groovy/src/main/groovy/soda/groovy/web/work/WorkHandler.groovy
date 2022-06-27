package soda.groovy.web.work

import com.sun.net.httpserver.HttpExchange
import groovy.json.JsonSlurper
import soda.groovy.web.BaseHandler
import soda.groovy.web.Logger
import soda.groovy.web.bootstrap.ContextManager
import soda.groovy.web.http.RequestHelper
import soda.groovy.web.resp.Response
import soda.groovy.web.resp.ResponseFactory

import java.util.concurrent.FutureTask
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

class WorkHandler extends BaseHandler {

    private final ContextManager contextManager

    private final long timeoutMillis

    WorkHandler(ContextManager mgr, long timeoutMillis) {
        contextManager = mgr
        this.timeoutMillis = timeoutMillis
    }

    @Override
    protected Response doPost(HttpExchange exchange) throws Exception {
        def content = RequestHelper.bodyString(exchange)
        def jr = new WorkRequest(new JsonSlurper().parseText(content))

        Logger.info("context key: ${jr.key}")
        Logger.info("boot class: ${jr.bootClass}")
        Logger.info("test case: ${jr.testCase}")

        ClassLoader loader = contextManager.get(jr.key).orElseThrow {
            new RuntimeException("no context found by key ${jr.key}")
        }.classLoader
        def klass = loader.loadClass(jr.bootClass)

        def workClosure = { ->
            def ctor = klass.getDeclaredConstructor()
            ctor.setAccessible(true)
            def obj = ctor.newInstance()
            return obj(jr.testCase)
        }

        TimeLimitedJob tLJob = new TimeLimitedJob(workClosure)
        FutureTask<String> future = tLJob.start()
        try {
            def result = future.get(timeoutMillis, TimeUnit.MILLISECONDS)
            Logger.info("test output: $result")
            return ResponseFactory.text(result)
        } catch (TimeoutException tex) {
            tLJob.kill()
            throw new RuntimeException("Job timeout", tex)
        }
    }

}
