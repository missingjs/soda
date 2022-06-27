package soda.kotlin.web.work

import com.sun.net.httpserver.HttpExchange
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import soda.kotlin.web.BaseHandler
import soda.kotlin.web.BusinessCode
import soda.kotlin.web.Logger
import soda.kotlin.web.bootstrap.ContextManager
import soda.kotlin.web.exception.ServiceException
import soda.kotlin.web.http.RequestHelper
import soda.kotlin.web.resp.Response
import soda.kotlin.web.resp.ResponseFactory
import java.nio.charset.StandardCharsets
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException
import kotlin.reflect.full.memberFunctions
import kotlin.reflect.full.primaryConstructor

class WorkHandler(private val contextManager: ContextManager, private val timeoutMillis: Long) : BaseHandler() {

    override fun doPost(exchange: HttpExchange?): Response {
        val req = parse(exchange!!)
        Logger.info("context key: ${req.key}")
        Logger.info("entry class: ${req.entryClass}")
        Logger.info("test case: ${req.testCase}")

        val task = fun(): String {
            val classLoader = contextManager.get(req.key)?.getClassLoader()
                ?: throw RuntimeException("no class loader for ${req.key}")
            val className = req.entryClass
            val kClass = classLoader.loadClass(className).kotlin
            val ctor = kClass.primaryConstructor
                ?: throw NoSuchMethodError("no constructor for class $className")
            val obj = ctor.call()
            val invoke = kClass.memberFunctions.find { it.name == "invoke" }
                ?: throw NoSuchMethodError("no method invoke found for class $className")
            return invoke.call(obj, req.testCase) as String
        }

        val job = TimeLimitedWork(task)
        val future = job.start()
        try {
            val result = future.get(timeoutMillis, TimeUnit.MILLISECONDS)
            Logger.info("test output: $result")
            return ResponseFactory.text(result)
        } catch (ex: TimeoutException) {
            job.kill()
            throw RuntimeException("work execution timeout", ex)
        }
    }

    companion object {
        private fun parse(exchange: HttpExchange): WorkRequest {
            val contentType = exchange.requestHeaders.getFirst("Content-Type")
            return if (contentType.contains("application/json")) {
                val content = RequestHelper.bodyString(exchange)
                Json.decodeFromString(content)
            } else if (contentType.contains("multipart/form-data")) {
                val formData = RequestHelper.multipartFormData(exchange)
                val key = formData.firstValue("key")
                val entryClass = formData.firstValue("entry_class")
                val caseBytes = formData.firstFile("test_case")
                val testCase = String(caseBytes, StandardCharsets.UTF_8)
                WorkRequest(key, entryClass, testCase)
            } else {
                throw ServiceException(BusinessCode.COMMON_ERROR, "unknown Content-Type: $contentType", 400)
            }
        }
    }
}