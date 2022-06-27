package soda.groovy.web.work

import com.sun.net.httpserver.HttpExchange
import groovy.json.JsonSlurper
import soda.groovy.web.BaseHandler
import soda.groovy.web.BusinessCode
import soda.groovy.web.Logger
import soda.groovy.web.bootstrap.ContextManager
import soda.groovy.web.exception.ParameterMissingException
import soda.groovy.web.exception.ServiceException
import soda.groovy.web.http.RequestHelper
import soda.groovy.web.resp.Response
import soda.groovy.web.resp.ResponseFactory

import java.nio.charset.StandardCharsets
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
        def req = parse(exchange)
        Logger.info("context key: ${req.key}")
        Logger.info("entry class: ${req.entryClass}")
        Logger.info("test case: ${req.testCase}")

        ClassLoader loader = contextManager.get(req.key).orElseThrow {
            new RuntimeException("no context found by key ${req.key}")
        }.classLoader
        def klass = loader.loadClass(req.entryClass)

        def workClosure = { ->
            def ctor = klass.getDeclaredConstructor()
            ctor.setAccessible(true)
            def obj = ctor.newInstance()
            return obj(req.testCase)
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

    private static WorkRequest parse(HttpExchange exchange) throws Exception {
        def contentType = exchange.getRequestHeaders().getFirst("Content-Type")
        if (contentType.contains("application/json")) {
            def body = RequestHelper.bodyString(exchange)
            return new WorkRequest(new JsonSlurper().parseText(body))
        } else if (contentType.contains("multipart/form-data")) {
            def formData = RequestHelper.multipartFormData(exchange)
            def key = formData.firstValue("key")
            def entryClass = formData.firstValue("entry_class")
            def caseBytes = formData.firstFile("test_case")
            def testCase = new String(caseBytes, StandardCharsets.UTF_8)
            def workReq = new WorkRequest([:])
            workReq.key = key
            workReq.entryClass = entryClass
            workReq.testCase = testCase
            return workReq
        } else {
            throw new ServiceException(BusinessCode.COMMON_ERROR, "unknown Content-Type: " + contentType, 400)
        }
    }

}
