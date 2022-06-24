package soda.kotlin.web.work

import com.sun.net.httpserver.HttpExchange
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import soda.kotlin.web.BaseHandler
import soda.kotlin.web.Logger
import soda.kotlin.web.http.RequestHelper
import soda.kotlin.web.resp.Response
import soda.kotlin.web.resp.ResponseFactory
import soda.kotlin.web.setup.ClassLoaderCache
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException
import kotlin.reflect.full.memberFunctions
import kotlin.reflect.full.primaryConstructor

class WorkHandler : BaseHandler() {

    private val maxTimeoutMillis: Long = 9000

    private val timeoutMillis = maxTimeoutMillis

    override fun doPost(exchange: HttpExchange?): Response {
        val content = RequestHelper.bodyString(exchange!!)
        Logger.info("test input: $content")

        val req = Json.decodeFromString<WorkRequest>(content)
        val task = fun(): String {
            val classLoader = ClassLoaderCache.getForJar(req.classpath)
                ?: throw RuntimeException("no class loader for ${req.classpath}")
            val className = req.bootClass
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
}